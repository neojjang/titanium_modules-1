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

#define PRIZE_GOOD				1
#define PRIZE_RECOMMENDATION	2

#define ALERT_HEIGHT_RECOMMENDATION		50
#define ALERT_MARGIN_RECOMMENDATION		10
#define ALERT_HEIGHT_VGOOD				69
#define ALERT_MARGIN_VGOOD				20

@class BButton,BVirtualGood;

@protocol BeintooPrizeDelegate;

@interface BPrize : UIView {
	
	UIImageView		*prizeThumb;
	UILabel			*textLabel;
	UILabel			*detailedTextLabel;
	UIButton		*closeBtn;
	BOOL			firstTouch;
	
	NSString		*transitionEnterSubtype;
	NSString		*transitionExitSubtype;
	
	int				prizeType;
	
	id <BeintooPrizeDelegate> delegate;
}

@property(nonatomic,retain) UIImageView *beintooLogo;
@property(nonatomic,retain) UIImageView *prizeThumb;
@property(nonatomic,retain) UIImageView *prizeImg;
@property(nonatomic,retain) UILabel *textLabel;
@property(nonatomic,retain) UILabel *detailedTextLabel;
@property(nonatomic, assign) id <BeintooPrizeDelegate> delegate;
@property(nonatomic) int prizeType;

+ (void)showBannerIn:(UIViewController *)callingViewController withMessage:(BPrize *)_message;

- (id)initWithFrame:(CGRect)frame andPrizeType:(int)_prizeType;
- (void)setThumbnail:(NSData *)imgData;
- (void)removeViews;
- (void)show;
- (void)hide;
- (void)drawPrize;
- (void)setPrizeContentWithWindowSize:(CGSize)windowSize;
- (void)preparePrizeAlertOrientation:(CGRect)startingFrame;


@end

@protocol BeintooPrizeDelegate <NSObject>

@optional
-(void)userDidTapOnThePrize;
-(void)userDidTapOnClosePrize;
@end

