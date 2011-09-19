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
#import "BPrize.h"

@class BeintooVGoodVC,BeintooMultipleVgoodVC,BeintooVGoodShowVC;

@interface BeintooMainController : UIViewController<BeintooPrizeDelegate>{

	BPrize *prizeBanner;
	
	NSString *transitionEnterSubtype;
	NSString *transitionExitSubtype;
	
	BeintooVGoodVC *singleVgoodVC;
	BeintooMultipleVgoodVC *multipleVgoodVC;
	BeintooVGoodShowVC *recommendationVC;	
}

@property(nonatomic,retain) BeintooVGoodVC *singleVgoodVC;
@property(nonatomic,retain) BeintooMultipleVgoodVC *multipleVgoodVC;
@property(nonatomic,retain) BeintooVGoodShowVC *recommendationVC;

- (void)hide;
- (void)initAndShowPrizeView;
- (void)showVgoodNavigationController;
- (void)hideVgoodNavigationController;
- (void)launchVGoodFrom:(id)sender withVgood:(NSDictionary *)vGood;
- (void)prepareBeintooVgoodOrientation;

@end
