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

#import "BPrize.h"
#import <QuartzCore/QuartzCore.h>
#import "BButton.h"
#import "Beintoo.h"

@implementation BPrize

@synthesize beintooLogo,prizeImg,prizeThumb,textLabel,detailedTextLabel,delegate,prizeType;

-(id)init {
	if (self = [super init])
	{
		
	}
    return self;
}

- (void)setPrizeContentWithWindowSize:(CGSize)windowSize{
	firstTouch = YES;
	
	self.backgroundColor = [UIColor colorWithRed:0.0/255 green:0.0/255 blue:0.0/255 alpha:0.7];
	self.layer.cornerRadius = 0;
	self.layer.borderColor = [UIColor colorWithWhite:1 alpha:0.8].CGColor;
	self.layer.borderWidth = 1;
	
	BVirtualGood *lastVgood = [Beintoo getLastGeneratedVGood];
	
	self.frame = CGRectZero;
	
	// Banner frame initialization: a vgood frame is a little bit smaller than a recommendation
	int _prizeType = PRIZE_GOOD;
	CGRect vgoodFrame = CGRectMake((ALERT_MARGIN_VGOOD/2), windowSize.height-ALERT_HEIGHT_VGOOD, 300/*windowSize.width-ALERT_MARGIN_VGOOD*/, ALERT_HEIGHT_VGOOD);
	if ([lastVgood isRecommendation]) {
		_prizeType = PRIZE_RECOMMENDATION;
		vgoodFrame = CGRectMake((ALERT_MARGIN_RECOMMENDATION/2), windowSize.height-ALERT_HEIGHT_RECOMMENDATION, 300 /*windowSize.width-ALERT_MARGIN_RECOMMENDATION*/, ALERT_HEIGHT_RECOMMENDATION);
	}
	prizeType = _prizeType;
	
	[self setFrame:vgoodFrame];

	[self preparePrizeAlertOrientation:vgoodFrame];
}

- (void)show{
	
	self.alpha = 0;

	if ([BeintooDevice isiPad]) {
		// UTILIZZARE UNA VIEW GRIGia o totalmente trasparente
		// attaccarci sopra i vari popover
		// se uno clicca exit o fuori dal popover rimuovere la view dalla window
	}
		
	CATransition *applicationLoadViewIn = [CATransition animation];
	[applicationLoadViewIn setDuration:0.7f];
	[applicationLoadViewIn setValue:@"load" forKey:@"name"];
	applicationLoadViewIn.removedOnCompletion = YES;
	[applicationLoadViewIn setType:kCATransitionMoveIn];
	applicationLoadViewIn.subtype = transitionEnterSubtype;
	applicationLoadViewIn.delegate = self;
	[applicationLoadViewIn setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self layer] addAnimation:applicationLoadViewIn forKey:@"Show"];
	
	self.alpha = 1;
}

- (void)animationDidStop:(CAAnimation *)animation finished:(BOOL)flag{
	if ([[animation valueForKey:@"name"] isEqualToString:@"load"]) {
	}
}


- (void)drawPrize{
	
	BVirtualGood *lastVgood = [Beintoo getLastGeneratedVGood];

	[self removeViews];
	[self setThumbnail:lastVgood.vGoodImageData];	
	

	
	// --- CLOSE BUTTON --- //
	UIImage *closeImg = [UIImage imageNamed:@"bar_close.png"];
	closeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
	[closeBtn setImage:closeImg forState:UIControlStateNormal];
	closeBtn.frame = CGRectMake([self bounds].size.width-27,0,closeImg.size.width+15, closeImg.size.height+12);
	[closeBtn addTarget:self action:@selector(closeBanner) forControlEvents:UIControlEventTouchUpInside];
	closeBtn.contentMode = UIViewContentModeRedraw;
	[self addSubview:closeBtn];
	[self bringSubviewToFront:closeBtn];
	// -------------------- //
	
	self.textLabel = [[UILabel alloc] initWithFrame:CGRectMake(68, 5, [self bounds].size.width-90, 35)];
	self.textLabel.text = NSLocalizedStringFromTable(@"vgoodMessageBanner",@"BeintooLocalizable",@"Pending");
	self.textLabel.font = [UIFont systemFontOfSize:13];
	self.textLabel.numberOfLines = 2;
	self.textLabel.textAlignment = UITextAlignmentCenter;
	self.textLabel.backgroundColor = [UIColor clearColor];
	self.textLabel.textColor = [UIColor colorWithWhite:1 alpha:1];
	[self.textLabel release];
	
	self.detailedTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(68, 36, [self bounds].size.width-90, 25)];
	self.detailedTextLabel.text = NSLocalizedStringFromTable(@"tapHereToRedeem",@"BeintooLocalizable",@"Pending");
	self.detailedTextLabel.font = [UIFont systemFontOfSize:13];
	self.detailedTextLabel.textAlignment = UITextAlignmentCenter;
	self.detailedTextLabel.backgroundColor = [UIColor clearColor];
	self.detailedTextLabel.textColor = [UIColor colorWithWhite:1 alpha:1];
	[self.detailedTextLabel release];
	
	if (prizeType == PRIZE_GOOD) {
		[self addSubview:self.textLabel];
		[self addSubview:self.detailedTextLabel];
	}
}

- (void)removeViews {
	for (UIView *subview in [self subviews]) {
		[subview removeFromSuperview];
	}
}

- (void)closeBanner{
	self.alpha = 0;
	[self removeFromSuperview];
	[[self delegate] userDidTapOnClosePrize];
}

- (void)setThumbnail:(NSData *)imgData{
	if (prizeType == PRIZE_GOOD) {
		self.prizeThumb = [[UIImageView alloc] initWithFrame:CGRectMake(8, 8, 53, 53)];
		[self.prizeThumb setImage:[UIImage imageWithData:imgData]];
		[self addSubview:self.prizeThumb];
		[self.prizeThumb release];	
	}
	if (prizeType == PRIZE_RECOMMENDATION) {
		prizeThumb.alpha = 1;
		self.prizeThumb = [[UIImageView alloc] initWithFrame:CGRectMake(1, 1, [self bounds].size.width-2, [self bounds].size.height-2)];
		[self.prizeThumb setImage:[UIImage imageWithData:imgData]];
		[self addSubview:self.prizeThumb];
		[self.prizeThumb release];			
	}
}

- (void)preparePrizeAlertOrientation:(CGRect)startingFrame{
	self.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
	CGRect windowFrame	 = [[Beintoo getAppWindow] bounds];
		
	int alertHeight = (prizeType == PRIZE_RECOMMENDATION) ? ALERT_HEIGHT_RECOMMENDATION : ALERT_HEIGHT_VGOOD;	
		
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeLeft) {
		self.frame = startingFrame;
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(-90.0));
		self.center = CGPointMake(windowFrame.size.width-(alertHeight/2.f), windowFrame.size.height/2.f) ;

		transitionEnterSubtype = kCATransitionFromRight;
		transitionExitSubtype  = kCATransitionFromLeft;
	}
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeRight) {
		self.frame = startingFrame;
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(90.0));
		self.center = CGPointMake(1+(alertHeight/2), windowFrame.size.height/2) ;
		transitionEnterSubtype = kCATransitionFromLeft;
		transitionExitSubtype  = kCATransitionFromRight;
	}
	if ([Beintoo appOrientation] == UIInterfaceOrientationPortrait) {
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
		self.frame = startingFrame;	
		self.center = CGPointMake(windowFrame.size.width/2, windowFrame.size.height-(alertHeight/2.f));
		transitionEnterSubtype = kCATransitionFromTop;
		transitionExitSubtype  = kCATransitionFromBottom;
	}
	
	if ([Beintoo appOrientation] == UIInterfaceOrientationPortraitUpsideDown) {
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
		self.frame = startingFrame;	
		self.center = CGPointMake(windowFrame.size.width/2, windowFrame.size.height-(alertHeight/2.f));
		transitionEnterSubtype = kCATransitionFromTop;
		transitionExitSubtype  = kCATransitionFromBottom;
	}
	
	[self drawPrize];
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{   
	if (firstTouch) {
		[self setBackgroundColor:[UIColor colorWithRed:50.0/255 green:50.0/255 blue:50.0/255 alpha:0.7]];
		if (prizeType == PRIZE_RECOMMENDATION) {
			self.prizeThumb.alpha = 0.7;
		}
	}
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
	[self setBackgroundColor:[UIColor colorWithRed:0.0/255 green:0.0/255 blue:0.0/255 alpha:0.7]];
	[[self delegate] userDidTapOnThePrize];
	self.alpha  = 0;

	firstTouch = YES;
	[self removeFromSuperview];

}

- (void)dealloc {
    [super dealloc];
}

@end
