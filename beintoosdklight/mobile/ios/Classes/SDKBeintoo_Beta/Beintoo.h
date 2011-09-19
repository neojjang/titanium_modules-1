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

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <QuartzCore/QuartzCore.h>
#import "BeintooPlayer.h"
#import "BeintooVC.h"
#import "BeintooVgood.h"
#import "BeintooVGoodVC.h"
#import "BeintooMultipleVgoodVC.h"
#import "BLoadingView.h"
#import "BeintooMacros.h"
#import "BeintooUser.h"
#import "BeintooNetwork.h"
#import "BeintooDevice.h"
#import "BeintooVGoodShowVC.h"
#import "BImageDownload.h"
#import "BView.h"
#import "BGradientView.h"
#import "BButton.h"
#import "BScrollView.h"
#import "BPrize.h"
#import "BeintooMainDelegate.h"
#import "BVirtualGood.h"
#import "BeintooMainController.h"
#import "BeintooNavigationController.h"
#import "BeintooVgoodNavController.h"
#import "BeintooiPadController.h"
#import "BeintooAchievements.h"
#import "BMessageAnimated.h"
#import "BTableView.h"
#import "BTableViewCell.h"

#define DegreesToRadians(x) ((x) * M_PI / 180.0)

// BeintooActiveFeatures : NSArray
extern NSString *BeintooApplicationWindow;
extern NSString *BeintooAppOrientation;

@class BeintooVC;

extern NSString *BNSDefLastLoggedPlayers;
extern NSString *BNSDefLoggedPlayer;
extern NSString *BNSDefLoggedUser;
extern NSString *BNSDefIsUserLogged;

@interface Beintoo : NSObject {
	
	id <BeintooMainDelegate> mainDelegate;

	NSString			*apiKey;
	NSString			*apiSecret;
	NSString			*deviceID;
	NSString			*restBaseUrl;
	BOOL				isOnSandbox;
	CLLocation			*userLocation;
	int					appOrientation;
	CLLocationManager	*locationManager;
	UIWindow			*applicationWindow;
	BeintooVgood		*beintooVgoodService;
	BeintooPlayer		*beintooPlayerService;
	BeintooAchievements *beintooAchievementsService;
	
	BVirtualGood			*lastGeneratedGood;
	BPrize					*prizeView;
	BMessageAnimated	    *notificationView;
	BeintooMainController	*mainController;
	BeintooNavigationController	*mainNavigationController;
	BeintooVgoodNavController *vgoodNavigationController;
	BeintooiPadController	*ipadController;
	BeintooVC				*beintooPanelRootViewController;
}

+ (void)initWithApiKey:(NSString *)_apikey andApiSecret:(NSString *)_apisecret andBeintooSettings:(NSDictionary *)_settings andMainDelegate:(id<BeintooMainDelegate>)beintooMainDelegate;
+ (void)launchPrize;
+ (BOOL)isUserLogged;
+ (BOOL)isOnSandbox;
+ (BOOL)userHasAllowedLocationServices;
+ (void)setUserLogged:(BOOL)isLoggedValue;
+ (NSString *)currentVersion;
+ (NSDictionary *)getBeintooPlayer;
+ (NSDictionary *)getUserIfLogged;
+ (NSString *)getRestBaseUrl;
+ (NSString *)getApiKey;
+ (NSString *)getPlayerID;
+ (NSString *)getUserID;
+ (BVirtualGood *)getLastGeneratedVGood;
+ (BeintooVgood *)beintooVgoodService;
+ (BeintooPlayer *)beintooPlayerService;
+ (BeintooAchievements *)beintooAchievementService;
+ (UIViewController *)getMainController;
+ (BeintooNavigationController *)getMainNavigationController;
+ (BeintooVgoodNavController *)getVgoodNavigationController;
+ (UIWindow *)getAppWindow;
+ (void)setLastGeneratedVgood:(BVirtualGood *)_theVGood;
+ (int)appOrientation;
+ (void)setBeintooUser:(NSDictionary *)_user;
+ (void)setBeintooPlayer:(NSDictionary *)_player;
+ (void)switchBeintooToSandbox;
+ (void)dismissPrize;
+ (void)dismissRecommendation;
+ (void)beintooDidAppear;
+ (void)beintooDidDisappear;
+ (void)prizeDidAppear;
+ (void)prizeDidDisappear;
+ (void)updateUserLocation;
+ (void)playerLogout;
+ (void)notifyVGoodGenerationOnMainDelegate;
+ (void)changeBeintooOrientation:(int)_orientation;
+ (void)notifyVGoodGenerationErrorOnMainDelegate:(NSString *)_error;
+ (id<BeintooMainDelegate>)getMainDelegate;
+ (BeintooVC *)getBeintooPanelRootViewController;
+ (BMessageAnimated *)getNotificationView;
+ (CLLocation *)getUserLocation;
+ (void)shutdownBeintoo;

@end

