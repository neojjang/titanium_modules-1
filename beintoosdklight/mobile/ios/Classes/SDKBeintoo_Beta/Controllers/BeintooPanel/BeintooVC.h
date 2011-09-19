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
#import "Beintoo.h"
#import "BeintooUser.h"
#import "BeintooPlayer.h"

@class BView,BeintooNavigationController,BButton,BTableView,BeintooVgood;

@interface BeintooVC : UIViewController <UITableViewDelegate,UITableViewDataSource,BeintooPlayerDelegate, BeintooParserDelegate, BeintooUserDelegate> {
	
	IBOutlet BView			*tryBeintooView;
	IBOutlet UILabel		*pressTheButtonLabel;
	IBOutlet UILabel		*titleLabel1;
	IBOutlet UILabel		*titleLabel2;
	IBOutlet UILabel		*titleLabel3;	
	IBOutlet BButton		*button1;
	IBOutlet BButton		*button2;
	
	IBOutlet BView			*homeView;
	IBOutlet BView			*unreadMessagesView;
	IBOutlet UILabel		*userNick;
	IBOutlet BTableView		*homeTable;
	IBOutlet UILabel		*bedollars;
	UILabel					*unreadMessagesLabel;
	UIButton				*unreadMessagesButton;
	
	BOOL isBeintoo;
	BOOL homeTableAnimationPerformed;
		
	NSMutableArray			*retrievedPlayersArray;
	NSMutableArray			*featuresArray;
	UIViewController		*homeSender;
	BeintooNavigationController	*homeNavController;
	UINavigationController	*loginNavController;
	UIPopoverController		*popOverController;
	UIPopoverController		*loginPopoverController;
	
	BeintooPlayer		 *beintooPlayer;
	BeintooUser			 *_user;
}

+ (UIButton *)closeButton;
   
- (IBAction)tryBeintoo;
- (IBAction)close;

@property(nonatomic,retain) BeintooPlayer *beintooPlayer;
@property(nonatomic,retain) UINavigationController *loginNavController;
@property(nonatomic,retain) NSMutableArray *retrievedPlayersArray;
@property(nonatomic,retain) UIPopoverController *popOverController;
@property(nonatomic,retain) UIPopoverController *loginPopoverController;
@property(nonatomic,retain) NSMutableArray	*featuresArray;

@end
