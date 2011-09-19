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
#import "QuartzCore/QuartzCore.h"
#import <UIKit/UIKit.h>


@interface BButton : UIButton {
	
	UIColor *_highColor;		// Top 
	UIColor *_mediumHighColor;	// Medium-top
	UIColor *_mediumLowColor;	// Medium-low
	UIColor *_lowColor;			// Low
	
	UIColor *_rollHighColor;		// Top 
	UIColor *_rollMediumHighColor;	// Medium-top
	UIColor *_rollMediumLowColor;	// Medium-low
	UIColor *_rollLowColor;			// Low
	
	BOOL	  isSelected;
	NSNumber  *textSize;
	
	CAGradientLayer *gradientLayer;
}

@property(nonatomic,retain) UIColor *_highColor;
@property(nonatomic,retain) UIColor *_mediumHighColor;
@property(nonatomic,retain) UIColor *_mediumLowColor;
@property(nonatomic,retain) UIColor *_lowColor;
@property(nonatomic,retain) UIColor *_rollHighColor;
@property(nonatomic,retain) UIColor *_rollMediumHighColor;
@property(nonatomic,retain) UIColor *_rollMediumLowColor;
@property(nonatomic,retain) UIColor *_rollLowColor;

@property(nonatomic,retain) CAGradientLayer *gradientLayer;
@property(nonatomic,retain) NSNumber *textSize;

- (void)setHighColor:(UIColor*)color andRollover:(UIColor *)rollover;
- (void)setLowColor:(UIColor*)color andRollover:(UIColor *)rollover;
- (void)setMediumHighColor:(UIColor*)color andRollover:(UIColor *)rollover;
- (void)setMediumLowColor:(UIColor*)color andRollover:(UIColor *)rollover;
- (void)setButtonTextSize:(int)size;


@end
