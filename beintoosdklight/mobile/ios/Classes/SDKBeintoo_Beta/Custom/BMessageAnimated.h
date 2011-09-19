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

#define NO_CONNECTION_MESSAGE			9
#define WELCOME_MESSAGE					1
#define SUBMIT_SCORE_MESSAGGE			2
#define CHALLENGE_ACCEPTED_MESSAGE		3
#define CHALLENGE_REJECTED_MESSAGE		4
#define CHALLENGE_TOBEACCEPTED_MESSAGE	5
#define GIFT_SEND_MESSAGE				6
#define GIFT_NOTSEND_MESSAGE			7

#define NOTIFICATION_HEIGHT_ACHIEV	50
#define NOTIFICATION_HEIGHT_SSCORE	35
#define NOTIFICATION_MARGIN		10

#define NOTIFICATION_TYPE_SSCORE 123
#define NOTIFICATION_TYPE_ACHIEV 124

#define BEINTOO_ERROR_MESSAGE 99

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface BMessageAnimated : UIView{
	
	UIImageView		*beintooLogo;
	UILabel			*captionLabel;
	UILabel			*achievNameLabel;
	NSDictionary	*current_achievement;
	BOOL			notificationCurrentlyOnScreen;
	int				notificationType;
	
	NSString		*transitionEnterSubtype;
	NSString		*transitionExitSubtype;
}


- (void)showNotification;
- (void)closeNotification;
- (void)prepareNotificationOrientation:(CGRect)startingFrame;
- (void)closeAchievement;
- (void)removeViews;

- (void)drawAchievement;
- (void)drawSubmitScore;


- (void)setNotificationContentForAchievement:(NSDictionary *)_theAchievement WithWindowSize:(CGSize)windowSize;
- (void)setNotificationContentForSubmitScore:(NSDictionary *)_theScore WithWindowSize:(CGSize)windowSize;


+ (NSString *)getMessageFromCode:(int)code;

@end
