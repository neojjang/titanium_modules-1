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

#import <UIKit/UIKit.h>
#import "BeintooVgood.h"

@class BView,BTableView,BeintooFriendsListVC,BeintooPlayer,BeintooVgood,BeintooPlayer,BeintooVGoodVC,BeintooVGoodShowVC;

@interface BeintooMultipleVgoodVC : UIViewController<UITableViewDelegate,BeintooVgoodDelegate,UIActionSheetDelegate> {
	
	IBOutlet BView			*multipleVgoodsView;
	IBOutlet BTableView		*multipleVgoodTable;
	IBOutlet UILabel		*selectVgoodLabel;
	NSMutableArray			*vgoodArrayList;
	NSMutableArray			*vgoodImages;
	NSDictionary			*selectedVgood;
	NSDictionary			*startingOptions;
	UIPopoverController		*callerPopoverController;

	BeintooPlayer			*_player;
	
	BeintooVGoodVC			*singleVgoodVC;
	BeintooVGoodShowVC		*vgoodShowVC;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil andOptions:(NSDictionary *)options;
- (void)getItReal;

@property(nonatomic,retain) IBOutlet BTableView *multipleVgoodTable;
@property(nonatomic,retain)	NSMutableArray *vgoodArrayList;
@property(nonatomic,retain) NSMutableArray *vgoodImages;
@property(nonatomic,retain)	NSDictionary *selectedVgood;
@property(nonatomic,retain)	NSDictionary *startingOptions;
@property(nonatomic,retain) BeintooVgood *vGood;

@end
