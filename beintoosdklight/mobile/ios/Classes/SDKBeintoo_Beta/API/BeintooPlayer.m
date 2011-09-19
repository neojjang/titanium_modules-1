/*******************************************************************************
 * Copyright 2011 Beintoo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
		
#import "BeintooPlayer.h"
#import "Beintoo.h"
	
@implementation BeintooPlayer

@synthesize delegate,parser,callingDelegate;

-(id)init {
	if (self = [super init])
	{	
		parser = [[Parser alloc] init];
		parser.delegate = self;
		
		rest_resource = [[NSString alloc] initWithString:[NSString stringWithFormat:@"%@/player/",[Beintoo getRestBaseUrl]]];
		app_rest_resource = [[NSString alloc] initWithString:[NSString stringWithFormat:@"%@/app/",[Beintoo getRestBaseUrl]]];
	}
    return self;
}

- (NSString *)restResource{
	return rest_resource;
}

+ (void)setPlayerDelegate:(id)_caller{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	playerService.callingDelegate = _caller;
}

#pragma mark -
#pragma mark SubmitScore Notification

+ (void)showNotificationForSubmitScore{
	
	// The main delegate is not called: a notification is shown by Beintoo on top of the app window and then automatically hidden
	// After the -showAchievementNotification, an animation is triggered and on complete the view is removed
	BMessageAnimated *_notification = [Beintoo getNotificationView];
	UIWindow *appWindow = [Beintoo getAppWindow];
	
	[_notification setNotificationContentForSubmitScore:nil WithWindowSize:appWindow.bounds.size];
	
	[appWindow addSubview:_notification];
	[_notification showNotification];
}

#pragma mark -
#pragma mark PLAYER API

// -------------------------------------------------------------------------------------
// Player Login. 
// -------------------------------------------------------------------------------------
+ (void)login{
	
	NSString *currentGuid	= [Beintoo getPlayerID];
	NSString *userId		= [Beintoo getUserID];
	[Beintoo updateUserLocation];
	
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	NSString *res		    = [NSString stringWithFormat:@"%@login/",[playerService restResource]];
	NSDictionary *params;
	
	if (currentGuid==nil) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
				  [Beintoo getApiKey], @"apikey", 
				  [BeintooDevice getUDID], @"deviceUUID", nil];
	}else if ( (userId==nil) && (currentGuid!=nil) ) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", 
				  [BeintooDevice getUDID], @"deviceUUID", 
				  currentGuid, @"guid", nil];
	}else if (userId!=nil) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", 
				  userId, @"userExt",[BeintooDevice getUDID], @"deviceUUID", nil];
	} 
	[playerService.parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_LOGINwDELEG_CALLER_ID];		
}

// Player login with playerID
+ (void)loginWithPlayerID:(NSString *)_playerID{
	
	NSString *currentGuid	= _playerID;
	[Beintoo updateUserLocation];
	
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	NSString *res		    = [NSString stringWithFormat:@"%@login/",[playerService restResource]];
	NSDictionary *params;
	
	if (currentGuid==nil) {
		NSLog(@"Beintoo Player Login - Error: no playerID provided");
		return;
	}
	params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", 
						[BeintooDevice getUDID], @"deviceUUID", 
						currentGuid, @"guid", nil];
			  
	[playerService.parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_LOGINwDELEG_CALLER_ID];		
}


// Player login with userID
+ (void)loginWithUserID:(NSString *)userid{
	
	NSString *currentGuid	= [Beintoo getPlayerID];
	[Beintoo updateUserLocation];
	
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	NSString *res		    = [NSString stringWithFormat:@"%@login/",[playerService restResource]];
	NSDictionary *params;
	
	if ( ([userid isEqualToString:@""]) && (currentGuid==nil)) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
				  [Beintoo getApiKey], @"apikey", 
				  [BeintooDevice getUDID], @"deviceUUID", nil];
		
	}else if ( ([userid isEqualToString:@""]) && (currentGuid!=nil) ) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
				  [Beintoo getApiKey], @"apikey", 
				  [BeintooDevice getUDID], @"deviceUUID", 
				  currentGuid, @"guid", nil];
	}else {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
				  [Beintoo getApiKey], @"apikey", 
				  userid, @"userExt",
				  [BeintooDevice getUDID], @"deviceUUID", nil];
	} 
	[playerService.parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_LOGINwDELEG_CALLER_ID];		
}


+ (void)notifyPlayerLoginSuccessWithResult:(NSDictionary *)result{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidLoginWithResult:)]) {
		[_callingDelegate playerDidLoginWithResult:result];
	}	
}	

+ (void)notifyPlayerLoginErrorWithResult:(NSString *)error{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidFailLoginWithResult:)]) {
		[_callingDelegate playerDidFailLoginWithResult:error];
	}	
}	

// -------------------------------------------------------------------------------------
// Player SubmitScore with contest.
// -------------------------------------------------------------------------------------
+ (void)submitScore:(int)_score forContest:(NSString *)_contestName{
	CLLocation *loc = [Beintoo getUserLocation];
	NSString *res;
	
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	if (loc == nil || (loc.coordinate.latitude <= 0.01f && loc.coordinate.latitude >= -0.01f) 
		|| (loc.coordinate.longitude <= 0.01f && loc.coordinate.longitude >= -0.01f)) {
		res = [NSString stringWithFormat:@"%@submitscore/?lastScore=%d",[playerService restResource],_score];
	}
	else	
		res	= [NSString stringWithFormat:@"%@submitscore/?lastScore=%d&latitude=%f&longitude=%f&radius=%f",[playerService restResource],_score,loc.coordinate.latitude,loc.coordinate.longitude,loc.horizontalAccuracy];	
	
	NSDictionary *params;	
	if ([_contestName isEqualToString:@""] || _contestName == nil) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",[Beintoo getPlayerID], @"guid", nil];
	}else {
		params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",[Beintoo getPlayerID], @"guid",_contestName, @"codeID", nil];
	}
			
	// Check for internet connection: if available proceed with the submitScore, otherwise save the score locally
	if ([BeintooNetwork connectedToNetwork]) {
		[[NSUserDefaults standardUserDefaults]setObject:[NSString stringWithFormat:@"%d",_score] forKey:@"lastSubmittedScore"];
		[[NSUserDefaults standardUserDefaults]synchronize];
		[playerService.parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_SSCORE_CONT_CALLER_ID];
	}
	else {
		[BeintooPlayer addScoreToLocallySavedScores:[NSString stringWithFormat:@"%d",_score] forContest:_contestName];
	}
}

// -------------------------------------------------------------------------------------
// Player SubmitScore no contest. 
// -------------------------------------------------------------------------------------
+ (void)submitScore:(int)_score{
	CLLocation *loc = [Beintoo getUserLocation];
	NSString *res;
	
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	
	if (loc == nil || (loc.coordinate.latitude <= 0.01f && loc.coordinate.latitude >= -0.01f) 
		|| (loc.coordinate.longitude <= 0.01f && loc.coordinate.longitude >= -0.01f)) {
		res = [NSString stringWithFormat:@"%@submitscore/?lastScore=%d",[playerService restResource],_score];
	}
	else	
		res	= [NSString stringWithFormat:@"%@submitscore/?lastScore=%d&latitude=%f&longitude=%f&radius=%f",[playerService restResource],_score,loc.coordinate.latitude,loc.coordinate.longitude,loc.horizontalAccuracy];	
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",[Beintoo getPlayerID], @"guid", nil];
	
	// Check for internet connection: if available proceed with the submitScore, otherwise save the score locally
	if ([BeintooNetwork connectedToNetwork]) {
		[[NSUserDefaults standardUserDefaults]setObject:[NSString stringWithFormat:@"%d",_score] forKey:@"lastSubmittedScore"];
		[[NSUserDefaults standardUserDefaults]synchronize];
		[playerService.parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_SSCORE_NOCONT_CALLER_ID];
	}
	else {
		[BeintooPlayer addScoreToLocallySavedScores:[NSString stringWithFormat:@"%d",_score] forContest:nil];
	}
}

	
+ (void)notifySubmitScoreSuccessWithResult:(NSString *)result{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidSumbitScoreWithResult:)]) {
		[_callingDelegate playerDidSumbitScoreWithResult:result];
	}	
}
+ (void)notifySubmitScoreErrorWithResult:(NSString *)error{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidFailSubmitScoreWithError:)]) {
		[_callingDelegate playerDidFailSubmitScoreWithError:error];
	}	
}

// -------------------------------------------------------------------------------------
// Player GetScore. 
// -------------------------------------------------------------------------------------

+ (void)getScore{

	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	NSString *res		 = [NSString stringWithFormat:@"%@byguid/%@",[playerService restResource],[Beintoo getPlayerID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[playerService.parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_GSCOREFORCONT_CALLER_ID];
}

+ (void)notifyPlayerGetScoreSuccessWithResult:(NSDictionary *)result{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidLoginWithResult:)]) {
		[_callingDelegate playerDidGetScoreWithResult:result];
	}	
}	

+ (void)notifyPlayerGetScoreErrorWithResult:(NSString *)error{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidFailLoginWithResult:)]) {
		[_callingDelegate playerDidFailGetScoreWithError:error];
	}	
}	

// -------------------------------------------------------------------------------------
// Player SetBalance. The response will be sent to a custom delegate
// -------------------------------------------------------------------------------------

+ (void)setBalance:(int)_playerBalance forContest:(NSString *)_contest{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	if (_playerBalance < 0) { // A balance should always be positive, or not?!?
		return;
	}
	
	NSString *res		 = [NSString stringWithFormat:@"%@submitscore/?lastScore=0&balance=%d",[playerService restResource],_playerBalance];
	NSDictionary *params;
	
	if ([_contest isEqualToString:@""]|| _contest == nil) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",[Beintoo getPlayerID], @"guid", nil];
	}else {
		params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",[Beintoo getPlayerID], @"guid",_contest, @"codeID", nil];
	}
	[playerService.parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_SETBALANCE_CALLER_ID];
}

+ (void)notifyPlayerSetBalanceSuccessWithResult:(NSString *)result{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidSetBalanceWithResult:)]) {
		[_callingDelegate playerDidSetBalanceWithResult:result];
	}		
}
+ (void)notifyPlayerSetBalanceErrorWithResult:(NSString *)error{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	id _callingDelegate = playerService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(playerDidFailSetBalanceWithError:)]) {
		[_callingDelegate playerDidFailSetBalanceWithError:error];
	}
}

#pragma mark -
#pragma mark Internal API

-(void)login:(NSString *)userid{	
	
	NSString *res		  = [NSString stringWithFormat:@"%@login/",rest_resource];
	NSString *currentGuid = [Beintoo getPlayerID]; 
	[Beintoo updateUserLocation];
	NSDictionary *params;
		
	if ( ([userid isEqualToString:@""]) && (currentGuid==nil)) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
								[Beintoo getApiKey], @"apikey", 
								[BeintooDevice getUDID], @"deviceUUID", nil];
		
	}else if ( ([userid isEqualToString:@""]) && (currentGuid!=nil) ) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
									[Beintoo getApiKey], @"apikey", 
									[BeintooDevice getUDID], @"deviceUUID", 
									currentGuid, @"guid", nil];
	}else {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
								[Beintoo getApiKey], @"apikey", 
								userid, @"userExt",
								[BeintooDevice getUDID], @"deviceUUID", nil];
	}
	[parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_LOGIN_CALLER_ID];
}

- (NSDictionary *)blockingLogin:(NSString *)userid{	
	
	NSString *res = [NSString stringWithFormat:@"%@login/",rest_resource];
	NSDictionary *params;
	
	if ([userid isEqualToString:@""]) {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
							[Beintoo getApiKey], @"apikey", 
							[BeintooDevice getUDID], @"deviceUUID", nil];
	}else {
		params = [NSDictionary dictionaryWithObjectsAndKeys:
							[Beintoo getApiKey], @"apikey", 
							userid, @"userExt",
							[BeintooDevice getUDID], @"deviceUUID", nil];
	}	
	NSDictionary *result = [parser blockerParsePageAtUrl:res withHeaders:params];
	
	loginError = LOGIN_NO_ERROR;
	[Beintoo setBeintooPlayer:result];
	[[self delegate]playerDidLogin:self];
	return result;
}

- (void)getAllScores{
	NSString *res		 = [NSString stringWithFormat:@"%@byguid/%@",rest_resource,[Beintoo getPlayerID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	
	[parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_GALLSCORES_CALLER_ID];
}

- (void)getPlayerByGUID:(NSString *)guid{
	NSString *res		 = [NSString stringWithFormat:@"%@byguid/%@",rest_resource,guid];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_GPLAYERBYGUID_CALLER_ID];
}

- (void)getPlayerByUserID:(NSString *)userID{
	NSString *res		 = [NSString stringWithFormat:@"%@byuser/%@",rest_resource,userID];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:PLAYER_GPLAYERBYUSER_CALLER_ID];	
}

#pragma mark -
#pragma mark /APP API

- (void)topScore:(NSString *)userExt{	
	NSString *res;
	if ([userExt isEqualToString:@""]) {
		res		 = [NSString stringWithFormat:@"%@topscore/",app_rest_resource];
	}else {
		res		 = [NSString stringWithFormat:@"%@topscore/%@",app_rest_resource,userExt];
	}

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:APP_GTOPSCORES_CALLER_ID];
}

- (void)showContestList{
	NSString *res		 = [NSString stringWithFormat:@"%@contest/show/?onlyPublic=true",app_rest_resource];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:APP_GCONTESTFORAPP_CALLER_ID];
}

- (void)logException:(NSString *)exception{
	NSString *res		 = [NSString stringWithFormat:@"%@logging",app_rest_resource];
	NSString *httpBody   = [NSString stringWithFormat:@"sdk=ios&text=%@",exception];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrlWithPOST:res withHeaders:params withHTTPBody:httpBody fromCaller:APP_LOG_EXCEPTION];
}

#pragma mark -
#pragma mark parser delegate

- (void)didFinishToParsewithResult:(NSDictionary *)result forCaller:(NSInteger)callerID{	
	switch (callerID){
		case PLAYER_LOGINwDELEG_CALLER_ID:{  // ------------------------------- PLAYER LOGIN WITH DELEGATE
			if (![[result objectForKey:@"kind"] isEqualToString:@"error"]) {
				if ([result objectForKey:@"guid"]!=nil) {
                    					
					NSString *playerGUID	= [result objectForKey:@"guid"];
					NSString *playerUser	= [result objectForKey:@"user"];
					
					if (playerUser!=nil && playerGUID!=nil) {	
						[Beintoo setUserLogged:YES];
					}
					loginError = LOGIN_NO_ERROR;
					[Beintoo setBeintooPlayer:result];
					
					[BeintooPlayer notifyPlayerLoginSuccessWithResult:result];
				}
			}
			else {
				[BeintooPlayer notifyPlayerLoginErrorWithResult:[result objectForKey:@"message"]];
			}
		}
			break;
			
		case PLAYER_SSCORE_CONT_CALLER_ID:{
			NSString *resultMessage = [result objectForKey:@"message"];
			if ([resultMessage isEqualToString:@"OK"]) {
				[BeintooPlayer notifySubmitScoreSuccessWithResult:[NSString stringWithFormat:@"Beintoo SubmitScore Result: %@",resultMessage]];
				[BeintooPlayer flushLocallySavedScore];
			}
			else {
				[BeintooPlayer notifySubmitScoreErrorWithResult:[NSString stringWithFormat:@"Beintoo SubmitScore Error: %@",resultMessage]];
			}
		}
			 break;
			
		case PLAYER_SSCORE_NOCONT_CALLER_ID:{
			NSString *resultMessage = [result objectForKey:@"message"];
			if ([resultMessage isEqualToString:@"OK"]) {
				[BeintooPlayer notifySubmitScoreSuccessWithResult:[NSString stringWithFormat:@"Beintoo SubmitScore Result: %@",resultMessage]];
				[BeintooPlayer flushLocallySavedScore];
			}
			else {
				[BeintooPlayer notifySubmitScoreErrorWithResult:[NSString stringWithFormat:@"Beintoo SubmitScore Error: %@",resultMessage]];
			}
		}
			break;			 
		
		case PLAYER_GSCOREFORCONT_CALLER_ID:{
			NSDictionary *getScoreResult;
			if ([result objectForKey:@"playerScore"]!=nil) {
				getScoreResult = [result objectForKey:@"playerScore"];
				[BeintooPlayer notifyPlayerGetScoreSuccessWithResult:getScoreResult];
			}else {
				[BeintooPlayer notifyPlayerGetScoreErrorWithResult:@"No score for this player."];
			}
		}
			break;
			
		case PLAYER_SETBALANCE_CALLER_ID:{
			NSString *resultMessage = [result objectForKey:@"message"];
			if ([resultMessage isEqualToString:@"OK"]) {
				[BeintooPlayer notifyPlayerSetBalanceSuccessWithResult:[NSString stringWithFormat:@"Beintoo Set Balance Result: %@",resultMessage]];
			}
			else {
				[BeintooPlayer notifyPlayerSetBalanceErrorWithResult:[NSString stringWithFormat:@"Beintoo SetBalance Error: %@",resultMessage]];
			}

		}
			break;
			
		// -------------------------   INTERNAL PLAYER API   --------------------------------------
		// ----------------------------------------------------------------------------------------
		case PLAYER_LOGIN_CALLER_ID:{
			if (![[result objectForKey:@"kind"] isEqualToString:@"error"]) {
				if ([result objectForKey:@"guid"]!=nil) {
					
					NSString *playerGUID	= [result objectForKey:@"guid"];
					NSString *playerUser	= [result objectForKey:@"user"];
					
					if (playerUser!=nil && playerGUID!=nil) {	
						[Beintoo setUserLogged:YES];
					}
					loginError = LOGIN_NO_ERROR;
					[Beintoo setBeintooPlayer:result];
				
					if ([[self delegate] respondsToSelector:@selector(playerDidLogin:)]) {
						[[self delegate] playerDidLogin:self];			
					}
				}
			}
			else {
				BLOG(@"Beintoo: Login over quota for this user. Retry in 10 sencods.");
				if ([[self delegate] respondsToSelector:@selector(playerDidLogin:)]) 
					[[self delegate] playerDidLogin:self];			
			}
		}
			break;
				
		case PLAYER_SSCORE_OFFLINE_CALLER_ID:{
			if ([[result objectForKey:@"message"] isEqualToString:@"OK"]) {
				[[NSUserDefaults standardUserDefaults] removeObjectForKey:@"locallySavedScores"];
			}
		}
			break;
			
		case PLAYER_FORCE_SSCORE_CALLER_ID:{
			//NSLog(@"forceSubmitScore result: %@",result);
		}
			break;
			
		case PLAYER_GALLSCORES_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(player:didGetAllScores:)]) {
				[[self delegate] player:self didGetAllScores:[result objectForKey:@"playerScore"]];
			}
			//NSLog(@"getAllScores result: %@",[result objectForKey:@"playerScore"]);
		}
			break;
			
		case PLAYER_GPLAYERBYGUID_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(player:getPlayerByGUID:)]) {
				[[self delegate]player:self getPlayerByGUID:result];
			}
			//BLOG(@"getPlayerByGuid result: %@",result);
		}
			break;
			
		case PLAYER_GPLAYERBYUSER_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(didgetPlayerByUser:)]) {
				[[self delegate] didgetPlayerByUser:result];
			}
			//BLOG(@"getPlayerByGuid result: %@",result);
		}
			break;
			
		case APP_GTOPSCORES_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(appDidGetTopScoreswithResult:)]) {
				[[self delegate]appDidGetTopScoreswithResult:result];
			}
			//NSLog(@"getTopScores result: %@",result);
		}
			break;
		case APP_GCONTESTFORAPP_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(appDidGetContestsForApp:)]) {
				[[self delegate] appDidGetContestsForApp:(NSArray *)result];
			}
			//BLOG(@"getContestForApp result: %@",result);
		}
			break;
		case APP_LOG_EXCEPTION:{
			//NSLog(@"app_log_exception result: %@",result);
		}
			break;
			
		default:{
				//statements
			}
			break;
	}	
}

#pragma mark -
#pragma mark LocallySavedScores Handler

+ (void)addScoreToLocallySavedScores:(NSString *)scoreValue forContest:(NSString *)codeID{
		
	@try {
		NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
		NSMutableArray *currentArrayOfScores = [NSMutableArray arrayWithArray:[defaults objectForKey:@"locallySavedScores"]];
		
		if (currentArrayOfScores!=nil) {
			NSMutableDictionary *currentElem = [[NSMutableDictionary alloc]init];
			[currentElem setObject:scoreValue forKey:@"lastScore"];
			if (codeID!=nil)
				[currentElem setObject:codeID forKey:@"codeID"];
			[currentArrayOfScores addObject:currentElem];
			[currentElem release];
			[[NSUserDefaults standardUserDefaults] setObject:currentArrayOfScores forKey:@"locallySavedScores"];
			//NSLog(@"currentArray %@ - size: %d",currentArrayOfScores,[currentArrayOfScores count]);
		}
	}
	@catch (NSException * e) {
		NSLog(@"Beintoo - SUBMIT SCORE LOCALLY exception: %@",e);
	}
}

+ (void)flushLocallySavedScore{
	if ([BeintooNetwork connectedToNetwork]) {
		@try {
			
			NSMutableArray *currentArrayOfScores = [NSMutableArray arrayWithArray:[[NSUserDefaults standardUserDefaults] objectForKey:@"locallySavedScores"]];
			BeintooPlayer *playerService = [Beintoo beintooPlayerService];
			
			if ([currentArrayOfScores count]==0) 
				return;
			NSString *jsonScore = [playerService.parser createJSONFromObject:currentArrayOfScores];
			[BeintooPlayer submitScoreForOfflineScores:jsonScore];
			//NSLog(@"jsonToSend :%@",jsonScore);
		}
		@catch (NSException * e) {
			NSLog(@"FLUSH EXCEPTION : %@",e);
		}
	}
}

+ (void)submitScoreForOfflineScores:(NSString *)scores{
	BeintooPlayer *playerService = [Beintoo beintooPlayerService];
	
	NSString *res		 = [NSString stringWithFormat:@"%@submitscore/",[playerService restResource]];
	NSString *httpBody   = [NSString stringWithFormat:@"json=%@",scores];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",[Beintoo getPlayerID], @"guid", nil];
	[playerService.parser parsePageAtUrlWithPOST:res withHeaders:params withHTTPBody:httpBody fromCaller:PLAYER_SSCORE_OFFLINE_CALLER_ID];	
}


#pragma mark -
#pragma mark Getter methods

- (int)loginError{return loginError;}

- (void)dealloc {
	[parser release];
	[app_rest_resource release];
	[rest_resource release];
	[super dealloc];
}

@end
