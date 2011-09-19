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

#import "BeintooUser.h"
#import "Beintoo.h"

@implementation BeintooUser

@synthesize delegate,parser;

-(id)init {
	if (self = [super init])
	{
        parser = [[Parser alloc] init];
		parser.delegate = self;
		rest_resource = [[NSString alloc] initWithString:[NSString stringWithFormat:@"%@/user/",[Beintoo getRestBaseUrl]]];
	}
    return self;
}

#pragma mark -
#pragma mark API

- (void)getUser{
	
	NSString *userExt = [Beintoo getUserID];
	if (userExt == nil) {
        NSLog(@"[Info][Beintoo] no user logged.");
		return;
	}
	NSString *res		 = [NSString stringWithFormat:@"%@%@",rest_resource,userExt];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:USER_GUSER_CALLER_ID];
}

- (void)getUserByM:(NSString *)m andP:(NSString *)p{	
	NSString *res		 = [NSString stringWithFormat:@"%@byemail/",rest_resource];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",m,@"email",p,@"password", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:USER_GUSERBYMP_CALLER_ID];
}

- (void)showChallengesbyStatus:(int)status{
	NSString *res		 = [NSString stringWithFormat:@"%@challenge/show/%@/%@/",rest_resource,
							[Beintoo getUserID], [self getStatusCode:status]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:USER_SHOWCHALLENGES_CALLER_ID];
}
- (void)challengeRequestfrom:(NSString *)userIDFrom	to:(NSString *)userIDTo withAction:(NSString *)action forContest:(NSString *)contest{
	NSString *res		 = [NSString stringWithFormat:@"%@challenge/%@/%@/%@",rest_resource,userIDFrom,action,userIDTo];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",contest,@"codeID", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:USER_CHALLENGEREQ_CALLER_ID];
}


- (void)registerUserToGuid:(NSString *)_guid withEmail:(NSString *)_email nickname:(NSString *)_nick password:(NSString *)_pass name:(NSString *)_name
					  country:(NSString *)_country address:(NSString *)_address gender:(NSString *)_gender sendGreetingsEmail:(BOOL)_sendGreet{
    NSString *playerGuid = _guid;
	
    if (playerGuid == nil) {
        if ([Beintoo getPlayerID] == nil) {
            BLOG(@"* Beintoo * RegisterUser error: no guid provided or found. Please login an user before to obtain a playerID (guid).");
            return;
        }
        else{
            playerGuid = [Beintoo getPlayerID];
        }
	}
	if (_email == nil) {
		BLOG(@"* Beintoo * RegisterUser error: email not provided.");
		return;
	}
	int userGender;
	
	if([_gender isEqualToString:@"MALE"])
		userGender = 1;
	else if ([_gender isEqualToString:@"FEMALE"]) 
		userGender = 2;
	else
		userGender = 0;
	
	NSString *httpBody;
	if(!userGender)
		httpBody = [NSString stringWithFormat:@"email=%@",_email];
	else
		httpBody = [NSString stringWithFormat:@"email=%@&gender=%d",_email,userGender];
	
	if (_nick != nil) 
		httpBody = [httpBody stringByAppendingString:[NSString stringWithFormat:@"&nickname=%@",_nick]];
	if (_pass != nil) 
		httpBody = [httpBody stringByAppendingString:[NSString stringWithFormat:@"&password=%@",_pass]];
	if (_name != nil) 
		httpBody = [httpBody stringByAppendingString:[NSString stringWithFormat:@"&name=%@",_name]];
	if (_country != nil) 
		httpBody = [httpBody stringByAppendingString:[NSString stringWithFormat:@"&country=%@",_country]];
	if (_address != nil) 
		httpBody = [httpBody stringByAppendingString:[NSString stringWithFormat:@"&address=%@",_address]];
	if(_sendGreet)
		httpBody = [httpBody stringByAppendingString:@"&sendGreetingsEmail=true"];
	else
		httpBody = [httpBody stringByAppendingString:@"&sendGreetingsEmail=false"];
	
	NSString *res			 = [NSString stringWithFormat:@"%@set",rest_resource];
	NSDictionary *params	 = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",playerGuid,@"guid", nil];	
	[parser parsePageAtUrlWithPOST:res withHeaders:params withHTTPBody:httpBody fromCaller:USER_REGISTER_CALLER_ID];
}

- (void)updateUser:(NSString *)_userExt withNickname:(NSString *)_nick{
	if(_userExt == nil){
		return;
	}	
	NSString *httpBody = @"";
	if (_nick != nil){
		httpBody = [NSString stringWithFormat:@"nickname=%@",_nick];
	}
	
	NSString *res			 = [NSString stringWithFormat:@"%@update",rest_resource];
	NSDictionary *params	 = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey",_userExt,@"userExt", nil];	
	[parser parsePageAtUrlWithPOST:res withHeaders:params withHTTPBody:httpBody fromCaller:USER_NICKUPDATE_CALLER_ID];
}


#pragma mark -
#pragma mark parser delegate response

- (void)didFinishToParsewithResult:(NSDictionary *)result forCaller:(NSInteger)callerID{	
	switch (callerID){
		case USER_GUSER_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(didGetUser:)]) {
				[[self delegate] didGetUser:result];
			}
		}
			break;
			
		case USER_GUSERBYMP_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(didGetUserByMail:)]) {
				[[self delegate] didGetUserByMail:result];
			}
			//BLOG(@"getUserByUDID result: %@",result);
		}
			break;
					
		case USER_SHOWCHALLENGES_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(didShowChallengesByStatus:)]) {
				[[self delegate] didShowChallengesByStatus:(NSMutableArray *)result];
			}
			//BLOG(@"showChallenges result: %@",result);
		}
			break;
			
		case USER_CHALLENGEREQ_CALLER_ID:{
			if ([[self delegate] respondsToSelector:@selector(challengeRequestFinishedWithResult:)]) {
				[[self delegate] challengeRequestFinishedWithResult:result];
			}
			//BLOG(@"challenge req result: %@",result);
		}
			break;
			
			
		case USER_REGISTER_CALLER_ID:{
			if ([result objectForKey:@"message"] != nil) 
				NSLog(@"error set user %@",[result objectForKey:@"message"]);
			else {
				if ([[self delegate]respondsToSelector:@selector(didCompleteRegistration:)]) 
					[[self delegate]didCompleteRegistration:result];
			}
		}
			break;
			
		case USER_NICKUPDATE_CALLER_ID:{
			if ([result objectForKey:@"message"] != nil) 
				NSLog(@"error set user %@",[result objectForKey:@"message"]);
			else {
				if ([[self delegate]respondsToSelector:@selector(didCompleteUserNickUpdate:)]) 
					[[self delegate]didCompleteUserNickUpdate:result];
			}
		}
			break;
			
		default:{
			//statements
		}
			break;
	}	
}

- (NSString *)getStatusCode:(int)code{
	if (code == CHALLENGES_TO_BE_ACCEPTED) {
		return @"TO_BE_ACCEPTED";
	}else if (code == CHALLENGES_STARTED) {
		return @"STARTED";
	}else if (code == CHALLENGES_ENDED) {
		return @"ENDED";
	}else {
		return @"TO_BE_ACCEPTED";
	}	
}

- (void)dealloc {
    [super dealloc];
	[parser release];
	[rest_resource release];
}


@end
