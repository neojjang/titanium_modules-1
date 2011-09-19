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

@class BeintooVGoodVC,BeintooMultipleVgoodVC,BeintooVGoodShowVC;

@interface BeintooiPadController : UIViewController<UIPopoverControllerDelegate>{

	UIPopoverController *popoverController;
	UIPopoverController *loginPopoverController;
	UIPopoverController *vgoodPopoverController;
	
	CGRect startingRect;
	
	BOOL		isVgoodPopoverVisible;
	BOOL		isMainPopoverVisible;

	NSString *transitionEnterSubtype;
	NSString *transitionExitSubtype;
	
}

@property(nonatomic,retain) UIPopoverController *popoverController;
@property(nonatomic,retain) UIPopoverController *loginPopoverController;
@property(nonatomic,retain) UIPopoverController *vgoodPopoverController;
@property(nonatomic,assign) BOOL isLoginOngoing;

- (void)hide;
- (void)showBeintooPopover;
- (void)hideBeintooPopover;
- (void)showVgoodPopoverWithVGoodController:(UINavigationController *)_vgoodNavController;
- (void)hideVgoodPopover;
- (void)preparePopoverOrientation;

@end
