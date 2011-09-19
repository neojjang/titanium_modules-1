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

#import "BeintooMainController.h"
#import "Beintoo.h"

@implementation BeintooMainController

@synthesize singleVgoodVC, multipleVgoodVC, recommendationVC;

-(id)init {
	if (self = [super init]){
		// Everyone of this controllers has to be initialized with the correspondent good to be shown
        //NSString *bundlePath = [NSString stringWithFormat:@"modules/ti.beintoo/"];
		singleVgoodVC = [[BeintooVGoodVC alloc] initWithNibName:@"BeintooVGoodVC" bundle:[NSBundle mainBundle]];
		multipleVgoodVC = [BeintooMultipleVgoodVC alloc];
		recommendationVC = [BeintooVGoodShowVC alloc];
		[recommendationVC setIsFromWallet:NO];
	}
    return self;
}

- (void)hide{
	[self.view removeFromSuperview];
	self.view.alpha = 0;
}

- (void)viewDidLoad{
}

- (void)viewWillAppear:(BOOL)animated{
	//NSLog(@"apper :home nav %@ - size: %@",[Beintoo getMainNavigationController],NSStringFromCGRect(self.view.frame));
	//[self.view addSubview:[Beintoo getMainNavigationController].topViewController.view];
	
}

- (void)viewWillDisappear:(BOOL)animated{
	[super viewWillDisappear:animated];
}

#pragma mark -
#pragma mark Vgood-Show-Hide-FromCaller

- (void)showVgoodNavigationController{
	BeintooVgoodNavController *vgoodNavController = [Beintoo getVgoodNavigationController];
	self.view = vgoodNavController.view;
		
	self.view.alpha = 1;

	CATransition *applicationLoadViewIn = [CATransition animation];
	[applicationLoadViewIn setDuration:0.5f];
	[applicationLoadViewIn setValue:@"loadVgood" forKey:@"name"];
	applicationLoadViewIn.removedOnCompletion = YES;
	[applicationLoadViewIn setType:kCATransitionMoveIn];
	applicationLoadViewIn.subtype = transitionEnterSubtype;
	applicationLoadViewIn.delegate = self;
	[applicationLoadViewIn setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self.view layer] addAnimation:applicationLoadViewIn forKey:@"Show"];
}

- (void)hideVgoodNavigationController{

	self.view.alpha = 0;
	CATransition *applicationUnloadViewIn = [CATransition animation];
	[applicationUnloadViewIn setDuration:0.5f];
	[applicationUnloadViewIn setValue:@"unloadVgood" forKey:@"name"];
	applicationUnloadViewIn.removedOnCompletion = YES;
	[applicationUnloadViewIn setType:kCATransitionReveal];
	applicationUnloadViewIn.subtype = transitionExitSubtype;
	applicationUnloadViewIn.delegate = self;
	[applicationUnloadViewIn setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self.view layer] addAnimation:applicationUnloadViewIn forKey:@"Show"];
	
}

#pragma mark -
#pragma mark animationFinish

- (void)animationDidStop:(CAAnimation *)animation finished:(BOOL)flag{
	if ([[animation valueForKey:@"name"] isEqualToString:@"unloadVgood"]) {
		[self.view removeFromSuperview];
		[Beintoo prizeDidDisappear];
	}
	if ([[animation valueForKey:@"name"] isEqualToString:@"loadVgood"]) {
		[Beintoo prizeDidAppear];
	}
}

- (void)prepareBeintooVgoodOrientation{
	BeintooVgoodNavController *vgoodNavController = [Beintoo getVgoodNavigationController];
	
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeLeft) {
		vgoodNavController.view.transform = CGAffineTransformMakeRotation(DegreesToRadians(-90));
		vgoodNavController.view.frame = CGRectMake(0, 0, 320, 480);
		transitionEnterSubtype = kCATransitionFromRight;
		transitionExitSubtype  = kCATransitionFromLeft;
	}
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeRight) {
		vgoodNavController.view.transform = CGAffineTransformMakeRotation(DegreesToRadians(90));
		vgoodNavController.view.frame = CGRectMake(0, 0, 320, 480);
		transitionEnterSubtype = kCATransitionFromLeft;
		transitionExitSubtype  = kCATransitionFromRight;
	}
	if (([Beintoo appOrientation] == UIInterfaceOrientationPortrait) || ([Beintoo appOrientation] == UIInterfaceOrientationPortraitUpsideDown)) {
		vgoodNavController.view.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
		vgoodNavController.view.frame = CGRectMake(0, 0, 320, 480);
		transitionEnterSubtype = kCATransitionFromTop;
		transitionExitSubtype  = kCATransitionFromBottom;
	}
}

#pragma mark -

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return [Beintoo appOrientation];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
    [super viewDidUnload];
}

- (void)dealloc {
    [super dealloc];
	[singleVgoodVC release];
	[multipleVgoodVC release];
	[recommendationVC release];	
}

@end
