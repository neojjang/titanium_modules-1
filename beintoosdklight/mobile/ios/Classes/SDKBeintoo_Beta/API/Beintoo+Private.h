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
#import "Beintoo.h"



@interface Beintoo (Private) <CLLocationManagerDelegate,UIPopoverControllerDelegate,BeintooPrizeDelegate> 

+ (Beintoo *)sharedInstance;
+ (UIWindow *)getApplicationWindow;
+ (void)setApiKey:(NSString *)_apikey andApisecret:(NSString *)_apisecret;
+ (void)createSharedBeintoo;
+ (void)setAppOrientation:(int)_appOrientation;
+ (void)initAPI;
+ (void)initPlayerService;
+ (void)initVgoodService;
+ (void)initAchievementsService;
+ (void)initMainController;
+ (void)initVgoodNavigationController;
+ (void)initMainNavigationController;
+ (void)initiPadController;
+ (void)switchToSandbox;
+ (void)_launchPrizeOnApp;
+ (void)_dismissPrize;
+ (void)_dismissRecommendation;
+ (void)_updateUserLocation;
+ (void)initBeintooSettings:(NSDictionary *)_settings;
+ (void)initLocallySavedScoresArray;
+ (void)initLocallySavedAchievementsArray;
+ (void)_setBeintooPlayer:(NSDictionary *)_player;
+ (void)_setBeintooUser:(NSDictionary *)_user;
+ (void)_setLastVgood:(BVirtualGood *)_vgood;
+ (void)_playerLogout;
+ (void)_beintooDidAppear;
+ (void)_beintooDidDisappear;
+ (void)_prizeDidAppear;
+ (void)_prizeDidDisappear;
+ (void)setApplicationWindow:(UIWindow *)_window;
+ (BOOL)isBeintooInitialized;

- (void)initDelegates;


@end
