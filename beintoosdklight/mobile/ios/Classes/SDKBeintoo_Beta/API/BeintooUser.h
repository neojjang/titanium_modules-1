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

#define CHALLENGES_TO_BE_ACCEPTED	50
#define CHALLENGES_STARTED			51	
#define CHALLENGES_ENDED			52	
#define	USER_REFUSE_FRIENDSHIP		53
#define USER_ACCEPT_FRIENDSHIP		54

#import <Foundation/Foundation.h>
#import "Parser.h"

@protocol BeintooUserDelegate;

@interface BeintooUser : NSObject <BeintooParserDelegate> {

	id <BeintooUserDelegate> delegate;
	Parser *parser;
	
	NSString *rest_resource;
}

- (void)getUser;
- (void)getUserByM:(NSString *)m andP:(NSString *)p;
- (void)showChallengesbyStatus:(int)status;
- (void)challengeRequestfrom:(NSString *)userIDFrom	to:(NSString *)userIDTo withAction:(NSString *)action forContest:(NSString *)contest;

/*
 *	REGISTER USER
 *	Certain parameters are optional: if not provided, pass nil
 *	- email				 (mandatory)
 *	- nickname			 (optional)
 *	- password			 (optional - if not provided will be automatically generated)
 *	- name				 (optional)
 *	- country			 (optional - provide ISO Country Names http://www.iso.org/iso/english_country_names_and_code_elements )
 *	- address			 (optional)
 *	- gender			 (optional - provide exactly MALE or FEMALE)
 *	- imageUrl			 (optional - provide, if exists, an URL with the profile picture for this user)
 *	- sendGreetingsEmail (optional - YES if you want this user to receive a welcome email from Beintoo)
 */
- (void)registerUserToGuid:(NSString *)_guid withEmail:(NSString *)_email nickname:(NSString *)_nick password:(NSString *)_pass name:(NSString *)_name
					  country:(NSString *)_country address:(NSString *)_address gender:(NSString *)_gender sendGreetingsEmail:(BOOL)_sendGreet;

- (void)updateUser:(NSString *)_userExt withNickname:(NSString *)_nick;




- (NSString *)getStatusCode:(int)code;

@property(nonatomic, assign) id <BeintooUserDelegate> delegate;
@property(nonatomic,retain) Parser *parser;

@end

@protocol BeintooUserDelegate <NSObject>

@optional
- (void)didGetUser:(NSDictionary *)result;
- (void)didGetUserByMail:(NSDictionary *)result;
- (void)didShowChallengesByStatus:(NSArray *)result;
- (void)challengeRequestFinishedWithResult:(NSDictionary *)result;
- (void)didCompleteRegistration:(NSDictionary *)result;
- (void)didCompleteUserNickUpdate:(NSDictionary *)result;

@end


