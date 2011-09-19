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

#import "Beintoo.h"
#import "Beintoo+Private.h"


@implementation Beintoo

NSString *BNSDefIsUserLogged;

+ (void)initWithApiKey:(NSString *)_apikey andApiSecret:(NSString *)_apisecret 
										   andBeintooSettings:(NSDictionary *)_settings 
										   andMainDelegate:(id<BeintooMainDelegate>)beintooMainDelegate{
	
	if ([Beintoo sharedInstance]) { 
		return; // -- Beintoo already initialized -- //
	}
	
	[Beintoo createSharedBeintoo];
	Beintoo *beintooInstance = [Beintoo sharedInstance];
	
	beintooInstance->mainDelegate		 = beintooMainDelegate;
	
	[beintooInstance initDelegates];
	
	[Beintoo initAPI];
	[Beintoo setApiKey:_apikey andApisecret:_apisecret];
	[Beintoo initLocallySavedScoresArray];
	[Beintoo initLocallySavedAchievementsArray];
	[Beintoo initPlayerService];
	[Beintoo initVgoodService];
	[Beintoo initAchievementsService];
	
	// Settings initialization
	[Beintoo initBeintooSettings:_settings];

	// Beintoo main controllers initialization
	[Beintoo initMainNavigationController];
	[Beintoo initMainController];
	[Beintoo initVgoodNavigationController];
	[Beintoo initiPadController];
    
     // Why here the user-agent? Because titanium is not able to handle this on a separate thread.
    [self performSelectorOnMainThread:@selector(storeUserAgent) withObject:nil waitUntilDone:false];
}

+ (void)launchPrize{
	[Beintoo _launchPrizeOnApp];
}

+ (int)appOrientation{
	return [Beintoo sharedInstance]->appOrientation;
}

+ (void)changeBeintooOrientation:(int)_orientation{
	if ([Beintoo isBeintooInitialized]) {
		[Beintoo setAppOrientation:_orientation];
	}
}

+ (NSString *)getApiKey{
	return [Beintoo sharedInstance]->apiKey;
}
								
+ (NSString *)currentVersion{
	return @"2.8.3titanium-beta-ios";
}

+ (BOOL)isUserLogged{
	return [[NSUserDefaults standardUserDefaults] boolForKey:BNSDefIsUserLogged];
}

+ (BOOL)isOnSandbox{
	if ([Beintoo sharedInstance]) {
		return [Beintoo sharedInstance]->isOnSandbox;
	}
	return NO;
}

+ (BOOL)userHasAllowedLocationServices{
	CLLocationManager *_locationManager = [Beintoo sharedInstance]->locationManager;
	BOOL isLocationServicesEnabled;
	
	if ([_locationManager respondsToSelector:@selector(locationServicesEnabled)]) {
		isLocationServicesEnabled = [CLLocationManager locationServicesEnabled];	
	}else {
		isLocationServicesEnabled = _locationManager.locationServicesEnabled;
	}
	return isLocationServicesEnabled;
}

+ (void)setUserLogged:(BOOL)isLoggedValue{
	[[NSUserDefaults standardUserDefaults] setBool:isLoggedValue forKey:BNSDefIsUserLogged];
	if (!isLoggedValue) {
		[Beintoo _playerLogout];
	}
}

+ (UIViewController *)getMainController{
	return [Beintoo sharedInstance]->mainController;
}
+ (BeintooNavigationController *)getMainNavigationController{
	return [Beintoo sharedInstance]->mainNavigationController;
}
+ (BeintooVgoodNavController *)getVgoodNavigationController{
	return [Beintoo sharedInstance]->vgoodNavigationController;
}

// -----------------------------------
// Beintoo Player
// -----------------------------------
+ (NSDictionary *)getBeintooPlayer{
	return [[NSUserDefaults standardUserDefaults]objectForKey:BNSDefLoggedPlayer];
}

+ (void)setBeintooPlayer:(NSDictionary *)_player{
	[self _setBeintooPlayer:_player];
}

+ (void)playerLogout{
	[self _playerLogout];
}

// -----------------------------------
// Beintoo User
// -----------------------------------
+ (NSDictionary *)getUserIfLogged{
	return [[NSUserDefaults standardUserDefaults]objectForKey:BNSDefLoggedUser];
}
+ (void)setBeintooUser:(NSDictionary *)_user{
	[self _setBeintooUser:_user];
}

// -----------------------------------
// Virtual Good
// -----------------------------------

+ (BVirtualGood *)getLastGeneratedVGood{
	@synchronized(self){
		return [[Beintoo sharedInstance]->lastGeneratedGood retain];
	}
}

+ (BeintooVgood *)beintooVgoodService{
	return [Beintoo sharedInstance]->beintooVgoodService;
}
+ (BeintooPlayer *)beintooPlayerService{
	return [Beintoo sharedInstance]->beintooPlayerService;
}
+ (BeintooAchievements *)beintooAchievementService{
	return [Beintoo sharedInstance]->beintooAchievementsService;
}

+ (void)setLastGeneratedVgood:(BVirtualGood *)_theVGood{
	@synchronized(self){
		[self _setLastVgood:_theVGood];
	}
}

+ (NSString *)getRestBaseUrl{
	return [Beintoo sharedInstance]->restBaseUrl;
}

+ (NSString *)getPlayerID{
	NSDictionary *_beintooPlayer = [Beintoo getBeintooPlayer];
	if(_beintooPlayer!= nil){
		return [_beintooPlayer objectForKey:@"guid"];
	}
	return nil;
}
+ (NSString *)getUserID{
	NSDictionary *_beintooUser = [Beintoo getUserIfLogged];
	if(_beintooUser!= nil){
		return [_beintooUser objectForKey:@"id"];
	}
	return nil;	
}

+ (void)switchBeintooToSandbox{
	[Beintoo switchToSandbox];
	NSLog(@"------------------------------------- Beintoo Sandbox ON -------------------------------------");
}

+ (void)notifyVGoodGenerationOnMainDelegate	{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	
	if ([_mainDelegate respondsToSelector:@selector(didBeintooGenerateAVirtualGood:)]) {
		[_mainDelegate didBeintooGenerateAVirtualGood:[Beintoo sharedInstance]->lastGeneratedGood];
	}	
}

+ (void)notifyVGoodGenerationErrorOnMainDelegate:(NSString *)_error{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	
	if ([_mainDelegate respondsToSelector:@selector(didBeintooFailToGenerateAVirtualGoodWithError:)]) {
		[_mainDelegate didBeintooFailToGenerateAVirtualGoodWithError:_error];
	}	
}

+ (BeintooVC *)getBeintooPanelRootViewController{
	return [Beintoo sharedInstance]->beintooPanelRootViewController;
}
+ (UIWindow *)getAppWindow{
	return [Beintoo getApplicationWindow];
}
+ (BMessageAnimated *)getNotificationView{
	return [Beintoo sharedInstance]->notificationView;
}

+ (id<BeintooMainDelegate>)getMainDelegate{
	return [Beintoo sharedInstance]->mainDelegate;
}

+ (void)updateUserLocation{
	[self _updateUserLocation];
}
+ (CLLocation *)getUserLocation{
	return [Beintoo sharedInstance]->userLocation;
}

+ (void)storeUserAgent{
    NSString *userAgent;
    UIWebView* webView = [[UIWebView alloc] initWithFrame:CGRectZero];
    userAgent = [webView stringByEvaluatingJavaScriptFromString:@"navigator.userAgent"];
    [[NSUserDefaults standardUserDefaults] setObject:userAgent forKey:@"userAgent"];
    [webView release];
}

+ (void)dismissPrize{
	[Beintoo _dismissPrize];
}

+ (void)dismissRecommendation{
	[Beintoo _dismissRecommendation];
}

+ (void)beintooDidAppear{
	[Beintoo _beintooDidAppear];
}
+ (void)beintooDidDisappear{
	[Beintoo _beintooDidDisappear];
}
+ (void)prizeDidAppear{
	[Beintoo _prizeDidAppear];
}
+ (void)prizeDidDisappear{
	[Beintoo _prizeDidDisappear];
}

+ (void)shutdownBeintoo{
	
	Beintoo *beintooInstance = [Beintoo sharedInstance];

	if (beintooInstance == nil){
		return;
	}
	/* --------------------------------------------- */
	[beintooInstance->apiKey release];
	beintooInstance->apiKey = nil;
	/* --------------------------------------------- */
	[beintooInstance->apiSecret release];
	beintooInstance->apiSecret = nil;
	/* --------------------------------------------- */
	[beintooInstance->locationManager release];
	beintooInstance->locationManager = nil;
	/* --------------------------------------------- */
	[beintooInstance->userLocation release];
	beintooInstance->userLocation = nil;
	/* --------------------------------------------- */
	[beintooInstance->lastGeneratedGood release];
	beintooInstance->lastGeneratedGood = nil;
	/* --------------------------------------------- */
	[beintooInstance->prizeView release];
	beintooInstance->prizeView = nil;
	/* --------------------------------------------- */
	[beintooInstance->notificationView release];
	beintooInstance->notificationView = nil;
	/* --------------------------------------------- */
	[beintooInstance->mainNavigationController release];
	beintooInstance->mainNavigationController = nil;
	/* --------------------------------------------- */
	[beintooInstance->vgoodNavigationController release];
	beintooInstance->vgoodNavigationController = nil;
	/* --------------------------------------------- */
	[beintooInstance->mainController release];
	beintooInstance->mainController = nil;
	/* --------------------------------------------- */  
	[beintooInstance->beintooPanelRootViewController release]; 
	beintooInstance->beintooPanelRootViewController = nil;
	/* --------------------------------------------- */
	[[Beintoo sharedInstance]->beintooPlayerService release];
	[Beintoo sharedInstance]->beintooPlayerService = nil;
	
	/* --------------------------------------------- */
	[beintooInstance->beintooVgoodService release];
	beintooInstance->beintooVgoodService = nil;	
	
	/* --------------------------------------------- */
	[beintooInstance->beintooAchievementsService release];
	beintooInstance->beintooAchievementsService = nil;
	
	[beintooInstance release];
	beintooInstance = nil;
}


- (void)dealloc{	
	[super dealloc];
}


@end
