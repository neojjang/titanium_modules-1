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

#import "Beintoo+Private.h"

@implementation Beintoo (Private)

static Beintoo* _sharedBeintoo = nil;
static NSString	*apiBaseUrl		= @"https://api.beintoo.com/api/rest";
static NSString	*sandboxBaseUrl = @"https://sandbox-elb.beintoo.com/api/rest";

NSString *BeintooAppOrientation = @"AppOrientation";
NSString *BeintooApplicationWindow = @"ApplicationWindow";

NSString *BNSDefLastLoggedPlayers	= @"beintooLastLoggedPlayers"; 
NSString *BNSDefLoggedPlayer		= @"beintooLoggedPlayer";
NSString *BNSDefLoggedUser			= @"beintooLoggedUser";
NSString *BNSDefIsUserLogged	    = @"beintooIsUserLogged";


+ (Beintoo *)sharedInstance{
	@synchronized([Beintoo class]){
		return _sharedBeintoo;
	}
}

+ (id)alloc{
	@synchronized([Beintoo class])
	{
		NSAssert(_sharedBeintoo == nil, @"Attempted to allocate a second instance of Beintoo singleton.");
		_sharedBeintoo = [super alloc];
		return _sharedBeintoo;
	}
	return nil;
}

+(id)init {
	if (self = [super init])
	{
	}
    return self;
}

+ (void)createSharedBeintoo{
	
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	
	[[Beintoo alloc] init];
	Beintoo *beintooInstance	= [Beintoo sharedInstance];
	
	beintooInstance->apiKey		= [[NSString alloc] init];
	beintooInstance->apiSecret	= [[NSString alloc] init];
	beintooInstance->locationManager	 = [[CLLocationManager alloc] init];
	beintooInstance->userLocation		 = [[CLLocation alloc]init];
	beintooInstance->lastGeneratedGood   = [[BVirtualGood alloc] init];
	beintooInstance->prizeView			 = [[BPrize alloc] init];
	beintooInstance->beintooPanelRootViewController = [[BeintooVC alloc] init];
	beintooInstance->notificationView	 = [[BMessageAnimated alloc]init];
	
	[pool release];
}

+ (BOOL)isBeintooInitialized{
	if ([Beintoo sharedInstance] != nil) {
		return YES;
	}
	return NO;
}

#pragma mark -
#pragma mark Initialization

+ (void)setApiKey:(NSString *)_apikey andApisecret:(NSString *)_apisecret{
	Beintoo *beintooInstance	= [Beintoo sharedInstance];
	beintooInstance->apiKey		= _apikey;
	beintooInstance->apiSecret	= _apisecret;
}

+ (void)initMainController{
	Beintoo *beintooInstance = [Beintoo sharedInstance];
	beintooInstance->mainController = [[BeintooMainController alloc] init];
	beintooInstance->mainController.view.alpha = 0;
}

+ (void)initMainNavigationController{
	Beintoo *beintooInstance = [Beintoo sharedInstance];
	beintooInstance->mainNavigationController = [[BeintooNavigationController alloc] initWithRootViewController:beintooInstance->beintooPanelRootViewController];
	[[beintooInstance->mainNavigationController navigationBar] setTintColor:[UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:1.0]];
}

+ (void)initVgoodNavigationController{
	// This navigation controller is not initialized with a Root Controller. 
	// The root controller will change based on the type of vgood (Single, Multiple, Recommendation)
	Beintoo *beintooInstance = [Beintoo sharedInstance];
	beintooInstance->vgoodNavigationController = [BeintooVgoodNavController alloc];
	[[beintooInstance->vgoodNavigationController navigationBar] setTintColor:[UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:1.0]];	
}

+ (void)initiPadController{
	Beintoo *beintooInstance = [Beintoo sharedInstance];
	beintooInstance->ipadController = [[BeintooiPadController alloc] init];
	beintooInstance->ipadController.view.alpha = 0;
}

+ (void)initVgoodService{
	
	if ([Beintoo sharedInstance]->beintooVgoodService != nil) {
		[[Beintoo sharedInstance]->beintooVgoodService release];
		//[Beintoo sharedInstance]->beintooVgoodService = nil;
	}
	[Beintoo sharedInstance]->beintooVgoodService = [[BeintooVgood alloc] init];
	NSLog(@"Vgood API service Initialized at URL: %@",[[Beintoo sharedInstance]->beintooVgoodService restResource]);
}

+ (void)initPlayerService{
	if ([Beintoo sharedInstance]->beintooPlayerService != nil) {
		[[Beintoo sharedInstance]->beintooPlayerService release];
		//[Beintoo sharedInstance]->beintooPlayerService = nil;
	}
	[Beintoo sharedInstance]->beintooPlayerService = [[BeintooPlayer alloc] init];
	NSLog(@"Player API service Initialized at URL: %@",[[Beintoo sharedInstance]->beintooPlayerService restResource]);
}

+ (void)initAchievementsService{
	if ([Beintoo sharedInstance]->beintooAchievementsService != nil) {
		[[Beintoo sharedInstance]->beintooAchievementsService release];
		//[Beintoo sharedInstance]->beintooAchievementsService = nil;
	}
	[Beintoo sharedInstance]->beintooAchievementsService = [[BeintooAchievements alloc] init];
	NSLog(@"Achievements API service Initialized at URL: %@",[[Beintoo sharedInstance]->beintooAchievementsService restResource]);
}

+ (void)initBeintooSettings:(NSDictionary *)_settings{
	
	[Beintoo setAppOrientation:(int)[[_settings objectForKey:BeintooAppOrientation] intValue]];	
	[Beintoo setApplicationWindow:(UIWindow *)[_settings objectForKey:BeintooApplicationWindow]];
}

+ (void)setAppOrientation:(int)_appOrientation{
	[Beintoo sharedInstance]->appOrientation = _appOrientation;
	NSLog(@"Beintoo: new App orientation set: %d",[Beintoo sharedInstance]->appOrientation);
}

+ (void)setApplicationWindow:(UIWindow *)_window{
	if (_window != nil) {
		[Beintoo sharedInstance]->applicationWindow = _window;
	}
}

+ (UIWindow *)getApplicationWindow{
	if ([Beintoo sharedInstance]->applicationWindow != nil) {
		return [Beintoo sharedInstance]->applicationWindow;
	}
	
	NSArray *developerApplicationWindows = [[UIApplication sharedApplication] windows];
	
	NSAssert([developerApplicationWindows count] > 0, @"Beintoo - To launch Beintoo your application needs at least one window!");
	UIWindow* appKeyWindow = [[UIApplication sharedApplication] keyWindow];
	if (!appKeyWindow){
		NSLog(@"Beintoo - No keyWindow found on this app. Beintoo will use the first UIWindow on the stack of app windows."); 
		appKeyWindow = [developerApplicationWindows objectAtIndex:0];
	}
	return appKeyWindow;
}

+ (void)initLocallySavedScoresArray{
	if ([[NSUserDefaults standardUserDefaults] objectForKey:@"locallySavedScores"]==nil) {
		[[NSUserDefaults standardUserDefaults] setObject:[NSMutableArray arrayWithCapacity:1] forKey:@"locallySavedScores"];
		[[NSUserDefaults standardUserDefaults] synchronize];
	}
}
+ (void)initLocallySavedAchievementsArray{
	if ([[NSUserDefaults standardUserDefaults] objectForKey:@"locallySavedAchievements"]==nil) {
		[[NSUserDefaults standardUserDefaults] setObject:[NSMutableArray arrayWithCapacity:1] forKey:@"locallySavedAchievements"];
		[[NSUserDefaults standardUserDefaults] synchronize];
	}
}

+ (void)initAPI{
	[Beintoo sharedInstance]->restBaseUrl = apiBaseUrl;
	[Beintoo sharedInstance]->isOnSandbox = NO;
}

// -------------------------------------------------------------------------
// Be sure to shutdown and re-initialize EVERY service already initialized 
// when you switch to sandbox. This is VERY important.
// -------------------------------------------------------------------------

+ (void)switchToSandbox{ 
	NSLog(@"Beintoo: Going to sandbox");
	[Beintoo sharedInstance]->restBaseUrl = sandboxBaseUrl;
	[Beintoo sharedInstance]->isOnSandbox = YES;
	[Beintoo initVgoodService];
	[Beintoo initPlayerService];
	[Beintoo initAchievementsService];
}

+ (void)_launchPrizeOnApp{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	
	BPrize	*_prizeView = [Beintoo sharedInstance]->prizeView;
	
	if ([_mainDelegate respondsToSelector:@selector(beintooPrizeAlertWillAppear)]) {
		[_mainDelegate beintooPrizeAlertWillAppear];
	}
	
	[_prizeView setPrizeContentWithWindowSize:[Beintoo getApplicationWindow].bounds.size];
	[[Beintoo getApplicationWindow] addSubview:_prizeView];
	[_prizeView show];
	
	if ([_mainDelegate respondsToSelector:@selector(beintooPrizeAlertDidAppear)]) {
		[_mainDelegate beintooPrizeAlertDidAppear];
	}	
}

+ (void)_dismissPrize{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;

	if ([_mainDelegate respondsToSelector:@selector(beintooPrizeWillDisappear)]) {
		[_mainDelegate beintooPrizeWillDisappear];
	}
	
	if (![BeintooDevice isiPad]) { // iPhone-iPodTouch
		BeintooMainController *_mainController = [Beintoo sharedInstance]->mainController;
		[_mainController hideVgoodNavigationController];
	}
	else {  // ----------- iPad
		
		BeintooiPadController *_iPadController = [Beintoo sharedInstance]->ipadController;
		[_iPadController.vgoodPopoverController dismissPopoverAnimated:NO];
		[_iPadController hideVgoodPopover];
	}
	
	
	// IF IS IPAD
	// BLABLABLA
}

+ (void)_dismissRecommendation{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	
	if (![BeintooDevice isiPad]) { // iPhone-iPodTouch
		
		if ([_mainDelegate respondsToSelector:@selector(beintooPrizeWillDisappear)]) {
			[_mainDelegate beintooPrizeWillDisappear];
		}
		
		BeintooMainController *_mainController = [Beintoo sharedInstance]->mainController;
		_mainController.view.alpha = 0;
		[_mainController.view removeFromSuperview];
		
		if ([_mainDelegate respondsToSelector:@selector(beintooPrizeDidDisappear)]) {
			[_mainDelegate beintooPrizeDidDisappear];
		}
	}
	
	// IF IS IPAD
	// BLABLABLA	
}


+ (void)_beintooDidAppear{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	if ([_mainDelegate respondsToSelector:@selector(beintooDidAppear)]) {
		[_mainDelegate beintooDidAppear];
	}	
}

+ (void)_beintooDidDisappear{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	if ([_mainDelegate respondsToSelector:@selector(beintooDidDisappear)]) {
		[_mainDelegate beintooDidDisappear];
	}		
}

+ (void)_prizeDidAppear{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	if ([_mainDelegate respondsToSelector:@selector(beintooPrizeDidAppear)]) {
		[_mainDelegate beintooPrizeDidAppear];
	}	
}

+ (void)_prizeDidDisappear{
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	if ([_mainDelegate respondsToSelector:@selector(beintooPrizeDidDisappear)]) {
		[_mainDelegate beintooPrizeDidDisappear];
	}		
}

#pragma mark -
#pragma mark Player - user

+ (void)_setBeintooPlayer:(NSDictionary *)_player{
	
	if (_player != nil && [_player objectForKey:@"guid"]!=nil) {
		[[NSUserDefaults standardUserDefaults] setObject:_player forKey:BNSDefLoggedPlayer];
		
		// If the player is connected to a user, we also set the user
		if ([_player objectForKey:@"user"]) {
			[[NSUserDefaults standardUserDefaults] setObject:[_player objectForKey:@"user"] forKey:BNSDefLoggedUser];
			[[NSUserDefaults standardUserDefaults] setBool:YES forKey:BNSDefIsUserLogged];
		}
		else {
			[[NSUserDefaults standardUserDefaults] setBool:NO forKey:BNSDefIsUserLogged];
		}
		[[NSUserDefaults standardUserDefaults] synchronize];
	}
}

+ (void)_playerLogout{
	[[NSUserDefaults standardUserDefaults] setBool:NO forKey:BNSDefIsUserLogged];
	[[NSUserDefaults standardUserDefaults] setObject:nil forKey:BNSDefLoggedPlayer];
	[[NSUserDefaults standardUserDefaults] setObject:nil forKey:BNSDefLoggedUser];
	[[NSUserDefaults standardUserDefaults] synchronize];
}

+ (void)_setBeintooUser:(NSDictionary *)_user{
	if (_user != nil && [_user objectForKey:@"id"]) {
		[[NSUserDefaults standardUserDefaults] setObject:_user forKey:BNSDefLoggedUser];
		[[NSUserDefaults standardUserDefaults] setBool:YES forKey:BNSDefIsUserLogged];
		[[NSUserDefaults standardUserDefaults] synchronize];
	}
}

+ (void)_setLastVgood:(BVirtualGood *)_vgood{
	
	[Beintoo sharedInstance]->lastGeneratedGood = _vgood;
}

- (void)initDelegates{
	[Beintoo sharedInstance]->prizeView.delegate = self;
}

#pragma mark -
#pragma mark PrizeDelegate

-(void)userDidTapOnThePrize{
	
	BVirtualGood *lastVgood = [Beintoo getLastGeneratedVGood];
	BeintooVgoodNavController *vgoodNavController = [Beintoo getVgoodNavigationController];
	BeintooMainController *_vgoodController = [Beintoo sharedInstance]->mainController;
	id<BeintooMainDelegate>	  _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	
	if ([lastVgood isRecommendation]) { // ----------  RECOMMENDATION ------------- //
		// Initialization of the Recommendation Controller with the recommendation URL
		[_vgoodController.recommendationVC initWithNibName:@"BeintooVGoodShowVC" bundle:[NSBundle mainBundle] urlToOpen:lastVgood.getItRealURL];
		[vgoodNavController initWithRootViewController:_vgoodController.recommendationVC];
	}
	else if ([lastVgood isMultiple]) {  // ----------  MULTIPLE VGOOD ------------- //
		// Initialize the Multiple vgood Controller with the list of options
		NSArray *vgoodList = [Beintoo getLastGeneratedVGood].theGoodsList;
		
		[_vgoodController.multipleVgoodVC initWithNibName:@"BeintooMultipleVgoodVC" bundle:[NSBundle mainBundle] 
							  andOptions:[NSDictionary dictionaryWithObjectsAndKeys:vgoodList,@"vgoodArray",/*self.popoverVgoodController,@"popoverController",*/nil]];
		[vgoodNavController initWithRootViewController:_vgoodController.multipleVgoodVC];
	}
	else {								// ----------  SINGLE VGOOD ------------- //
		// Initialize the Single vgood Controller with the generated vgood
		_vgoodController.singleVgoodVC.theVirtualGood = [Beintoo getLastGeneratedVGood];
		[vgoodNavController initWithRootViewController:_vgoodController.singleVgoodVC];
	}
	
	if ([_mainDelegate respondsToSelector:@selector(beintooPrizeWillAppear)]) {
		[_mainDelegate beintooPrizeWillAppear];
	}	
	
	if (![BeintooDevice isiPad]) { // --- iPhone,iPod
		[_vgoodController prepareBeintooVgoodOrientation];
		[[Beintoo getApplicationWindow] addSubview:vgoodNavController.view];
		[_vgoodController showVgoodNavigationController];
		
	}
	else {  // --- iPad
		BeintooiPadController *_iPadController = [Beintoo sharedInstance]->ipadController;
		
		[_iPadController preparePopoverOrientation]; 
		[[Beintoo getApplicationWindow] addSubview:_iPadController.view];
		[_iPadController showVgoodPopoverWithVGoodController:vgoodNavController];
	}
}

- (void)userDidTapOnClosePrize{
	BVirtualGood *lastVgood = [Beintoo getLastGeneratedVGood];
	id<BeintooMainDelegate> _mainDelegate = [Beintoo sharedInstance]->mainDelegate;
	if ([lastVgood isMultiple]) {
		
		@synchronized(self){
			BeintooVgood *_vgoodService = [Beintoo beintooVgoodService];
			[_vgoodService acceptGoodWithId:lastVgood.vGoodID];
		}
	}
	[Beintoo sharedInstance]->mainController.view.alpha = 0;
	
	if ([_mainDelegate respondsToSelector:@selector(beintooPrizeAlertDidDisappear)]) {
		[_mainDelegate beintooPrizeAlertDidDisappear];
	}
}


#pragma mark -
#pragma mark GPS

+ (void)_updateUserLocation{
	BOOL isLocationServicesEnabled;
	CLLocationManager *_locationManager = [Beintoo sharedInstance]->locationManager;
	_locationManager.delegate = self;
	
	if ([_locationManager respondsToSelector:@selector(locationServicesEnabled)]) {
		isLocationServicesEnabled = [CLLocationManager locationServicesEnabled];	
	}else {
		isLocationServicesEnabled = _locationManager.locationServicesEnabled;
	}
	if(!isLocationServicesEnabled){	
		NSLog(@"Beintoo - User has not accepted to use location services.");
		return;
	}
	[_locationManager startUpdatingLocation];
}

+ (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
	[Beintoo sharedInstance]->userLocation = [newLocation retain];
	NSLog(@"Location updated: %@", [newLocation description]);

	[manager stopUpdatingLocation];
}
+ (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
	//NSLog(@"Error: %@", [error description]);
}



@end
