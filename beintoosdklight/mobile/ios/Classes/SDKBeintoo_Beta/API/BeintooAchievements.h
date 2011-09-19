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
#import "Parser.h"

@protocol BeintooAchievementsDelegate;

@interface BeintooAchievements : NSObject <BeintooParserDelegate>{
	
	id <BeintooAchievementsDelegate> delegate;
	Parser *parser;
	
	id callingDelegate;
	NSString *rest_resource;
	
	// current Achievement informations
	NSString *currentAchievementID;
	int currentPercentage;
	int currentScore;
}

- (NSString *)restResource;
- (void)getAchievementsForCurrentUser;

+ (void)submitAchievementUpdate:(NSString *)_achievementID withPercentage:(int)_percentageFromZeroTo100;
+ (void)submitAchievementUpdate:(NSString *)_achievementID withTargetScore:(int)_score;
+ (void)notifyAchievementSubmitSuccessWithResult:(NSDictionary *)result;
+ (void)notifyAchievementSubmitErrorWithResult:(NSString *)error;
+ (void)setAchievementDelegate:(id)_caller;

+ (void)saveUnlockedAchievementLocally:(NSDictionary *)_theAchievement;
+ (NSMutableArray *)getAllLocalAchievements;
+ (BOOL)checkIfAchievementIsSavedLocally:(NSString *)_achievementID;

// Achievement notification
+ (void)showNotificationForUnlockedAchievement:(NSDictionary *)_achivement;

@property(nonatomic, assign) id <BeintooAchievementsDelegate> delegate;
@property(nonatomic, assign) id  callingDelegate;
@property(nonatomic,retain) Parser *parser;
@property(nonatomic,retain) NSString *currentAchievementID;
@property(nonatomic,assign) int currentPercentage;
@property(nonatomic,assign) int currentScore;

@end

@protocol BeintooAchievementsDelegate <NSObject>

@optional
- (void)didGetAllUserAchievementsWithResult:(NSArray *)result;
- (void)didSubmitAchievementWithResult:(NSDictionary *)result;
- (void)didFailToSubmitAchievementWithError:(NSString *)error;
@end


