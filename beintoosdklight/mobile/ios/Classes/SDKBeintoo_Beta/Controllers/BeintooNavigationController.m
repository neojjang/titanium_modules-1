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

#import "BeintooNavigationController.h"
#import "Beintoo.h"


@implementation BeintooNavigationController


- (void)show{
	self.view.alpha = 1;
	
	ipadView = [[UIView alloc] initWithFrame:[self.view bounds]];
	ipadView.backgroundColor = [UIColor colorWithWhite:1.0f alpha:0.6f];
	
	if ([BeintooDevice isiPad]) {
		// UTILIZZARE UNA VIEW GRIGia o totalmente trasparente
		// attaccarci sopra i vari popover
		// se uno tocca exit o tocca fuori dal popover rimuovere la view dalla window (e ovviamente anche il popover)
		self.view = ipadView;
	}
		
	CATransition *applicationLoadViewIn = [CATransition animation];
	[applicationLoadViewIn setDuration:0.5f];
	[applicationLoadViewIn setValue:@"load" forKey:@"name"];
	applicationLoadViewIn.removedOnCompletion = YES;
	[applicationLoadViewIn setType:kCATransitionMoveIn];
	applicationLoadViewIn.subtype = transitionEnterSubtype;
	applicationLoadViewIn.delegate = self;
	[applicationLoadViewIn setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self.view layer] addAnimation:applicationLoadViewIn forKey:@"Show"];
	
}

- (void)hide{
	CATransition *applicationUnloadViewIn = [CATransition animation];
	[applicationUnloadViewIn setDuration:0.5f];
	[applicationUnloadViewIn setValue:@"unload" forKey:@"name"];
	applicationUnloadViewIn.removedOnCompletion = YES;
	[applicationUnloadViewIn setType:kCATransitionReveal];
	applicationUnloadViewIn.subtype = transitionExitSubtype;
	applicationUnloadViewIn.delegate = self;
	[applicationUnloadViewIn setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self.view layer] addAnimation:applicationUnloadViewIn forKey:@"Show"];
	self.view.alpha = 0;
}

- (void)animationDidStop:(CAAnimation *)animation finished:(BOOL)flag{
	if ([[animation valueForKey:@"name"] isEqualToString:@"unload"]) {
		[self.view removeFromSuperview];
		[Beintoo beintooDidDisappear];
	}
	if ([[animation valueForKey:@"name"] isEqualToString:@"load"]) {
		[Beintoo beintooDidAppear];
	}
}


- (void)prepareBeintooPanelOrientation{
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeLeft) {
		self.view.transform = CGAffineTransformMakeRotation(DegreesToRadians(-90));
		self.view.frame = CGRectMake(0, 0, 320, 480);
		transitionEnterSubtype = kCATransitionFromRight;
		transitionExitSubtype  = kCATransitionFromLeft;
	}
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeRight) {
		self.view.transform = CGAffineTransformMakeRotation(DegreesToRadians(90));
		self.view.frame = CGRectMake(0, 0, 320, 480);
		transitionEnterSubtype = kCATransitionFromLeft;
		transitionExitSubtype  = kCATransitionFromRight;
	}
	if ([Beintoo appOrientation] == UIInterfaceOrientationPortrait) {
		self.view.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
		self.view.frame = CGRectMake(0, 0, 320, 480);
		transitionEnterSubtype = kCATransitionFromTop;
		transitionExitSubtype  = kCATransitionFromBottom;
	}
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return [Beintoo appOrientation];
}


- (void)didReceiveMemoryWarning {

    [super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
	[ipadView release];
}

@end
