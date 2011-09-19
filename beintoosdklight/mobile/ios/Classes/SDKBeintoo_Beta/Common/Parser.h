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

// PLAYER:from 1 to 29
#define PLAYER_LOGIN_CALLER_ID			1
#define PLAYER_SSCORE_NOCONT_CALLER_ID	2
#define PLAYER_SSCORE_CONT_CALLER_ID	3
#define PLAYER_GSCORE_ALL_CALLER_ID		4
#define PLAYER_GSCORE_CONT_CALLER_ID	5
#define PLAYER_LOGINwDELEG_CALLER_ID	6
#define PLAYER_GPLAYERBYUSER_CALLER_ID	7
#define PLAYER_GALLSCORES_CALLER_ID		8
#define PLAYER_GPLAYERBYGUID_CALLER_ID	9
#define PLAYER_GSCOREFORCONT_CALLER_ID	10
#define PLAYER_SETBALANCE_CALLER_ID		11
#define PLAYER_FORCE_SSCORE_CALLER_ID	12
#define PLAYER_SSCORE_OFFLINE_CALLER_ID 13

// USER:from 30 to 59
#define USER_GUSERBYMP_CALLER_ID		30
#define USER_GUSERBYUDID_CALLER_ID		31
#define USER_SHOWCHALLENGES_CALLER_ID	32
#define USER_CHALLENGEREQ_CALLER_ID		33
#define USER_GFRIENDS_CALLER_ID			34
#define USER_REMOVEUDID_CALLER_ID		35
#define USER_GUSER_CALLER_ID			36
#define USER_GETBALANCE_CALLER_ID		37
#define USER_GETBYQUERY_CALLER_ID		38
#define USER_SENDFRIENDSHIP_CALLER_ID	39
#define USER_GETFRIENDSHIP_CALLER_ID	40
#define	USER_REGISTER_CALLER_ID			41
#define	USER_NICKUPDATE_CALLER_ID		42

// VGOOD:from 60 to 79
#define VGOOD_SHOWBYUSER_CALLER_ID		60
#define VGOOD_GET_CALLER_ID				61
#define VGOOD_SENDGIFT_CALLER_ID		62
#define VGOOD_ACCEPT_CALLER_ID			63
#define VGOOD_SINGLE_CALLER_ID			64
#define VGOOD_SINGLEwDELEG_CALLER_ID	65
#define VGOOD_MULTIPLE_CALLER_ID		66
#define VGOOD_MULTIPLEwDELEG_CALLER_ID	67

// MARKET:from 80 to 89
#define MARKET_SELLVGOOD_CALLER_ID		80
#define MARKET_GOODSTOBUY_CALLER_ID		81
#define MARKET_BUYVGOOD_CALLER_ID		82

// APP:from 90 to 99
#define APP_GTOPSCORES_CALLER_ID		90
#define APP_GCONTESTFORAPP_CALLER_ID	91
#define APP_LOG_EXCEPTION				92

// MESSAGEfrom 100 to 109
#define MESSAGE_SHOW_CALLER_ID			100
#define MESSAGE_SEND_CALLER_ID			101
#define MESSAGE_SET_READ_CALLER_ID		102
#define MESSAGE_DELETE_CALLER_ID		103

// ACHIEVEMENTS:from 110 to 119
#define ACHIEVEMENTS_GET_CALLER_ID				110
#define ACHIEVEMENTS_SUBMIT_PERCENT_ID			111
#define ACHIEVEMENTS_SUBMIT_SCORE_ID			112
#define ACHIEVEMENTS_GETSUBMITPERCENT_CALLER_ID 113
#define ACHIEVEMENTS_GETSUBMITSCORE_CALLER_ID	114


@protocol BeintooParserDelegate;


@interface Parser : NSObject {
	NSString		*webPage;
	NSMutableData	*responseData;
	NSMutableURLRequest *request;
	NSInteger callerID;
	NSString *webpage;
	id result;
	
	id <BeintooParserDelegate> delegate;
}

- (void)parsePageAtUrl:(NSString *)URL withHeaders:(NSDictionary *)headers fromCaller:(int)caller;
- (void)parsePageAtUrlWithPOST:(NSString *)URL withHeaders:(NSDictionary *)headers fromCaller:(int)caller;
- (void)parsePageAtUrlWithPOST:(NSString *)URL withHeaders:(NSDictionary *)headers withHTTPBody:(NSString *)httpBody fromCaller:(int)caller;
- (id)blockerParsePageAtUrl:(NSString *)URL withHeaders:(NSDictionary *)headers;
- (NSString *)createJSONFromObject:(id)object;

@property(nonatomic, assign) id <BeintooParserDelegate> delegate;
@property(nonatomic,retain) NSMutableURLRequest *request;
@property(readwrite, assign,nonatomic) NSInteger callerID;
@property(nonatomic,retain) NSString *webpage;

@end

@protocol BeintooParserDelegate <NSObject>
@optional
- (void)didFinishToParsewithResult:(NSDictionary *)result forCaller:(NSInteger)callerID;
@end
