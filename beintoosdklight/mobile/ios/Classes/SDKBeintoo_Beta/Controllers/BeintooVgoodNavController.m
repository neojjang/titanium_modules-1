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

#import "BeintooVgoodNavController.h"
#import "Beintoo.h"

@implementation BeintooVgoodNavController

- (void)show{
	self.view.alpha = 1;
	
	ipadView = [[UIView alloc] initWithFrame:[self.view bounds]];
	ipadView.backgroundColor = [UIColor colorWithWhite:1.0f alpha:0.6f];
	
	CATransition *applicationLoadViewIn = [CATransition animation];
	[applicationLoadViewIn setDuration:0.5f];
	[applicationLoadViewIn setValue:@"load" forKey:@"name"];
	applicationLoadViewIn.removedOnCompletion = YES;
	[applicationLoadViewIn setType:kCATransitionMoveIn];
	applicationLoadViewIn.subtype = kCATransitionFromTop;
	applicationLoadViewIn.delegate = self;
	[applicationLoadViewIn setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self.view layer] addAnimation:applicationLoadViewIn forKey:@"Show"];
}

- (void)hide{
	self.view.alpha = 0;
	CATransition *applicationUnloadViewIn = [CATransition animation];
	[applicationUnloadViewIn setDuration:0.5f];
	[applicationUnloadViewIn setValue:@"unload" forKey:@"name"];
	applicationUnloadViewIn.removedOnCompletion = YES;
	[applicationUnloadViewIn setType:kCATransitionReveal];
	applicationUnloadViewIn.subtype = kCATransitionFromBottom;
	applicationUnloadViewIn.delegate = self;
	[applicationUnloadViewIn setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self.view layer] addAnimation:applicationUnloadViewIn forKey:@"Show"];
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

- (void)didReceiveMemoryWarning {

    [super didReceiveMemoryWarning];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	
    return YES;
}

- (void)dealloc {
    [super dealloc];
	[ipadView release];
	[prizeBanner release];
}

@end
