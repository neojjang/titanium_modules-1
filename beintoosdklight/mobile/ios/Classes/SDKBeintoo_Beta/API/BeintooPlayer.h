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

#define LOGIN_NO_PLAYER	  2
#define LOGIN_NO_ERROR	  0


@class BeintooUser,BScore;

@protocol BeintooPlayerDelegate;

@interface BeintooPlayer : NSObject <BeintooParserDelegate>{
	
	id <BeintooPlayerDelegate> delegate;
	int loginError;
	Parser *parser;
	
	NSString *rest_resource;
	NSString *app_rest_resource;
		
	id callingDelegate;
}

// --------------------------------- Developer API Calls ---------------------------------
+ (void)setPlayerDelegate:(id)_caller;
/* 
 * PLAYER LOGIN: 
 *	
 * Delegate callback response on:
 * - (void)playerDidLoginWithResult:(NSDictionary *)result{
 * - (void)playerDidFailLoginWithResult:(NSString *)error{
 */
+ (void)login;
+ (void)loginWithUserID:(NSString *)userid;

/* 
 * PLAYER SUBMITSCORE: 
 *	
 * Delegate callback response on:
 * - (void)playerDidSumbitScoreWithResult:(NSString *)result;
 * - (void)playerDidFailSubmitScoreWithError:(NSString *)error;
 */
+ (void)submitScore:(int)_score;
+ (void)submitScore:(int)_score forContest:(NSString *)_contestName;

/* 
 * PLAYER GETSCORE: 
 *	
 * Delegate callback response on:
 * - (void)playerDidGetScoreWithResult:(NSString *)result;
 * - (void)playerDidFailGetScoreWithError:(NSString *)error;
 */
+ (void)getScore;

/* 
 * PLAYER SETBALANCE: 
 *	
 * Delegate callback response on:
 * - (void)playerDidSetBalanceWithResult:(NSString *)result;
 * - (void)playerDidFailSetBalanceWithError:(NSString *)error;
 */
+ (void)setBalance:(int)_playerBalance forContest:(NSString *)_contest;

// ---------------------------------------------------------------------------------------

+ (void)notifyPlayerLoginSuccessWithResult:(NSDictionary *)result;
+ (void)notifyPlayerLoginErrorWithResult:(NSString *)result;
+ (void)notifySubmitScoreSuccessWithResult:(NSString *)result;
+ (void)notifySubmitScoreErrorWithResult:(NSString *)error;
+ (void)notifyPlayerGetScoreSuccessWithResult:(NSDictionary *)result;
+ (void)notifyPlayerGetScoreErrorWithResult:(NSString *)error;	
+ (void)notifyPlayerSetBalanceSuccessWithResult:(NSString *)result;
+ (void)notifyPlayerSetBalanceErrorWithResult:(NSString *)error;	

// ----------- Internal API -------------------
- (void)login:(NSString *)userid;
- (NSDictionary *)blockingLogin:(NSString *)userid;
- (void)getPlayerByGUID:(NSString *)guid;
- (void)getPlayerByUserID:(NSString *)userID;
- (void)getAllScores;

// APP REST
- (void)topScore:(NSString *)userExt;
- (void)showContestList;
- (void)logException:(NSString *)exception;

- (int)loginError;
- (NSString *)restResource;

// SubmitScore notification
+ (void)showNotificationForSubmitScore;


// Offline SubmitScore handlers
+ (void)addScoreToLocallySavedScores:(NSString *)scoreValue forContest:(NSString *)codeID;
+ (void)flushLocallySavedScore;
+ (void)submitScoreForOfflineScores:(NSString *)scores;

@property(nonatomic, assign) id <BeintooPlayerDelegate> delegate;
@property(nonatomic, assign) id  callingDelegate;
@property(nonatomic,retain) Parser *parser;

@end

@protocol BeintooPlayerDelegate <NSObject>

@optional

// Developer Callbacks
- (void)playerDidLoginWithResult:(NSDictionary *)result;
- (void)playerDidFailLoginWithResult:(NSString *)error;
- (void)playerDidSumbitScoreWithResult:(NSString *)result;
- (void)playerDidFailSubmitScoreWithError:(NSString *)error;
- (void)playerDidGetScoreWithResult:(NSDictionary *)result;
- (void)playerDidFailGetScoreWithError:(NSString *)error;
- (void)playerDidSetBalanceWithResult:(NSString *)result;
- (void)playerDidFailSetBalanceWithError:(NSString *)error;


// Internal Callbacks
- (void)playerDidLogin:(BeintooPlayer *)player;
- (void)player:(BeintooPlayer *)player didSubmitScorewithResult:(NSString *)result andPoints:(NSString *)points;
- (void)appDidGetTopScoreswithResult:(NSDictionary *)result;
- (void)appDidGetContestsForApp:(NSArray *)result;
- (void)didgetPlayerByUser:(NSDictionary *)result;
- (void)player:(BeintooPlayer *)player getPlayerByGUID:(NSDictionary *)result;

// Called on user profile to retrieve all the scores for a certain user
- (void)player:(BeintooPlayer *)player didGetAllScores:(NSDictionary *)result;

@end

