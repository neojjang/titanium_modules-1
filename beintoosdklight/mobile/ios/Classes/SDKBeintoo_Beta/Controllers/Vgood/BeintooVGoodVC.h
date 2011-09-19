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
#import "Parser.h"
#import "BPrize.h"
#import "BeintooVgood.h"

@class BeintooVGoodShowVC,BeintooFriendsListVC,BView,BButton,BTableView,BGradientView,BeintooPlayer,BeintooMultipleVgoodVC,BVirtualGood;

@interface BeintooVGoodVC : UIViewController<BeintooVgoodDelegate,UITableViewDelegate> {
	
	UINavigationController *recommendationNavigationController;
	
	id					 _caller;
	NSDictionary		 *generatedVGood;
	NSDictionary		 *startingOptions;
	BVirtualGood		 *theVirtualGood;
	
	BOOL				 isRecommendation;
	BOOL			     isThisVgoodConverted;
	
	UIPopoverController  *popoverVgoodController;
	UIPopoverController  *popoverRecomController;
	UIViewController	 *homeSender;
	
	BeintooVGoodShowVC	   *registrationVC;
	BeintooPlayer		   *beintooPlayer;
	BPrize				   *prizeBanner;

	IBOutlet UIScrollView	*scrollView;
	IBOutlet BView			*vgoodView;
	IBOutlet BGradientView *descView;
	IBOutlet UITextView  *vgoodNameText;
	IBOutlet UILabel	 *vgoodEndDateLbl;
	IBOutlet BTableView  *vgoodTable;
	IBOutlet UIImageView *vgoodImageView;
	IBOutlet UITextView  *vgoodDescrTextView;
	IBOutlet BButton	 *getCouponButton;
	IBOutlet BButton	 *sendAsAGiftButton;
	IBOutlet UILabel	 *endLabelTitle;
	IBOutlet UILabel	 *whoAlsoConvertedTitle;
	IBOutlet UILabel	 *titleLabel1;
}

- (IBAction)getItReal;

- (UIButton *)closeButton;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil;

@property(nonatomic,retain) UIViewController *homeSender;
@property(nonatomic,retain) NSDictionary *generatedVGood;
@property(nonatomic,retain) NSDictionary *startingOptions;
@property(nonatomic,retain) BVirtualGood *theVirtualGood;

@end


