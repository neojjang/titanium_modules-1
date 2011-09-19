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

#import "BMessageAnimated.h"
#import "Beintoo.h"
#import <QuartzCore/QuartzCore.h>

@implementation BMessageAnimated

// ----------------- NEW -------------------------->

-(id)init {
	if (self = [super init])
	{
		current_achievement = [[NSDictionary alloc] init];
	}
    return self;
}

// ---- This is the last method called, when the notification for the achievement is ready and well oriented
- (void)showNotification{
	
	notificationCurrentlyOnScreen = YES;
	
	self.alpha = 0;
	
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

- (void)closeNotification{
	
	self.alpha = 0;
	CATransition *popupExitAnimation = [CATransition animation];
	[popupExitAnimation setDuration:0.5f];
	[popupExitAnimation setValue:@"unload" forKey:@"name"];
	popupExitAnimation.removedOnCompletion = YES;
	[popupExitAnimation setType:kCATransitionFade];
	popupExitAnimation.delegate = self;
	[popupExitAnimation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
	[[self layer] addAnimation:popupExitAnimation forKey:nil];
}

- (void)animationDidStop:(CAAnimation *)animation finished:(BOOL)flag{
	if ([[animation valueForKey:@"name"] isEqualToString:@"load"]) {
		[NSTimer scheduledTimerWithTimeInterval:2.0 target:self  selector:@selector(closeNotification)
									   userInfo:nil repeats:NO];	
	}
	
	if ([[animation valueForKey:@"name"] isEqualToString:@"unload"]) {
		
		for (UIView* child in self.subviews) {
			[child removeFromSuperview];
		}
		
		[self removeFromSuperview];
		notificationCurrentlyOnScreen = NO;
	}
}

- (void)prepareNotificationOrientation:(CGRect)startingFrame{
	self.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
	CGRect windowFrame	 = [[Beintoo getAppWindow] bounds];
	
	int notificationHeight = (notificationType == NOTIFICATION_TYPE_ACHIEV) ? NOTIFICATION_HEIGHT_ACHIEV : NOTIFICATION_HEIGHT_SSCORE;	
	
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeLeft) {
		self.frame = startingFrame;
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(-90.0));
		self.center = CGPointMake(windowFrame.size.width-(notificationHeight/2.f), windowFrame.size.height/2.f) ;
		
		transitionEnterSubtype = kCATransitionFromRight;
		transitionExitSubtype  = kCATransitionFromLeft;
	}
	if ([Beintoo appOrientation] == UIInterfaceOrientationLandscapeRight) {
		self.frame = startingFrame;
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(90.0));
		self.center = CGPointMake(1+(notificationHeight/2), windowFrame.size.height/2) ;
		transitionEnterSubtype = kCATransitionFromLeft;
		transitionExitSubtype  = kCATransitionFromRight;
	}
	if ([Beintoo appOrientation] == UIInterfaceOrientationPortrait) {
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
		self.frame = startingFrame;	
		self.center = CGPointMake(windowFrame.size.width/2, windowFrame.size.height-(notificationHeight/2.f));
		transitionEnterSubtype = kCATransitionFromTop;
		transitionExitSubtype  = kCATransitionFromBottom;
	}
	
	if ([Beintoo appOrientation] == UIInterfaceOrientationPortraitUpsideDown) {
		self.transform = CGAffineTransformMakeRotation(DegreesToRadians(0));
		self.frame = startingFrame;	
		self.center = CGPointMake(windowFrame.size.width/2, windowFrame.size.height-(notificationHeight/2.f));
		transitionEnterSubtype = kCATransitionFromTop;
		transitionExitSubtype  = kCATransitionFromBottom;
	}
	
	switch (notificationType) {
		case NOTIFICATION_TYPE_ACHIEV:
			[self drawAchievement];
			break;
		case NOTIFICATION_TYPE_SSCORE:
			[self drawSubmitScore];
			break;			
		default:
			break;
	}	
}

- (void)removeViews {
	for (UIView *subview in [self subviews]) {
		[subview removeFromSuperview];
	}
}

- (void)closeAchievement{
	self.alpha = 0;
	[self removeFromSuperview];
}



#pragma mark -
#pragma mark Achievement

- (void)setNotificationContentForAchievement:(NSDictionary *)_theAchievement WithWindowSize:(CGSize)windowSize{
	
	current_achievement = _theAchievement;
	notificationType = NOTIFICATION_TYPE_ACHIEV;

	CGSize callerFrameSize = windowSize;
	
	self.frame = CGRectZero;
	CGRect notification_frame = CGRectMake(0, callerFrameSize.height-NOTIFICATION_HEIGHT_ACHIEV, callerFrameSize.width, NOTIFICATION_HEIGHT_ACHIEV);
	[self setFrame:notification_frame];
	
	self.backgroundColor = [UIColor colorWithRed:0.0/255 green:0.0/255 blue:0.0/255 alpha:0.8  ];
	self.layer.cornerRadius = 0;
	self.layer.borderColor = [UIColor colorWithWhite:1 alpha:0.8].CGColor;
	self.layer.borderWidth = 1;
	
	[self prepareNotificationOrientation:notification_frame];
}

- (void)drawAchievement{
	[self removeViews];
	
	NSString *msg = NSLocalizedStringFromTable(@"unlockAchievMsg",@"BeintooLocalizable",@"");
	NSString *achievementName = [current_achievement objectForKey:@"name"];

	beintooLogo		= [[UIImageView alloc] initWithFrame:CGRectMake(7, 10, 28, 28)];	
	[beintooLogo setImage:[UIImage imageNamed:@"beintoo_icon.png"]];
	[self addSubview:beintooLogo];
	
	captionLabel = [[UILabel alloc] initWithFrame:CGRectMake(45, 5, [self bounds].size.width-50, 20)];
	captionLabel.backgroundColor = [UIColor clearColor];
	captionLabel.textColor = [UIColor whiteColor];
	captionLabel.font = [UIFont systemFontOfSize:14];
	captionLabel.textAlignment = UITextAlignmentLeft;
	captionLabel.text = achievementName;
	[self addSubview:captionLabel];
	
	achievNameLabel	= [[UILabel alloc] initWithFrame:CGRectMake(45, 21, [self bounds].size.width-50, 20)];
	achievNameLabel.backgroundColor = [UIColor clearColor];
	achievNameLabel.textColor = [UIColor whiteColor];
	achievNameLabel.font = [UIFont systemFontOfSize:12];
	achievNameLabel.textAlignment = UITextAlignmentLeft;
	achievNameLabel.text = msg;	
	[self addSubview:achievNameLabel];
	
	[captionLabel release];
	[beintooLogo release];
	[achievNameLabel release];
}	


#pragma mark -
#pragma mark Submit_Score

- (void)setNotificationContentForSubmitScore:(NSDictionary *)_theScore WithWindowSize:(CGSize)windowSize{
	
	notificationType = NOTIFICATION_TYPE_SSCORE;

	CGSize callerFrameSize = windowSize;
	
	self.frame = CGRectZero;
	CGRect notification_frame = CGRectMake(0, callerFrameSize.height-NOTIFICATION_HEIGHT_SSCORE, callerFrameSize.width, NOTIFICATION_HEIGHT_SSCORE);
	[self setFrame:notification_frame];
	
	self.backgroundColor = [UIColor colorWithRed:0.0/255 green:0.0/255 blue:0.0/255 alpha:0.8  ];
	self.layer.cornerRadius = 0;
	self.layer.borderColor = [UIColor colorWithWhite:1 alpha:0.8].CGColor;
	self.layer.borderWidth = 1;
	
	[self prepareNotificationOrientation:notification_frame];
}

- (void)drawSubmitScore{
	[self removeViews];
	
	NSString *lastSubmittedScore = [[NSUserDefaults standardUserDefaults]objectForKey:@"lastSubmittedScore"];
	NSString *msg = [NSString stringWithFormat:NSLocalizedStringFromTable(@"submitScoreMsg",@"BeintooLocalizable",@""),lastSubmittedScore];
	
	beintooLogo		= [[UIImageView alloc] initWithFrame:CGRectMake(7, 5, 24, 24)];	
	[beintooLogo setImage:[UIImage imageNamed:@"beintoo_icon.png"]];
	[self addSubview:beintooLogo];
	
	captionLabel = [[UILabel alloc] initWithFrame:CGRectMake(45, 5, [self bounds].size.width-50, 20)];
	captionLabel.backgroundColor = [UIColor clearColor];
	captionLabel.textColor = [UIColor whiteColor];
	captionLabel.font = [UIFont fontWithName:@"TrebuchetMS-Bold" size:13.0];//[UIFont systemFontOfSize:13];
	captionLabel.textAlignment = UITextAlignmentLeft;
	captionLabel.text = msg;
	[self addSubview:captionLabel];
		
	[captionLabel release];
	[beintooLogo release];
}

// --------------- OLD -------------------->

+ (NSString *)getMessageFromCode:(int)code{
	if (code==WELCOME_MESSAGE) {
		NSString *playerNick = [[Beintoo getUserIfLogged] objectForKey:@"nickname"];
		if (playerNick!=nil) {
			return [NSString stringWithFormat:@"Welcome back %@",playerNick];
		}
		return @"NO_MESSAGE";

	}else if (code==SUBMIT_SCORE_MESSAGGE) {
		return [NSString stringWithFormat:@"You earned %@ point(s).",[[NSUserDefaults standardUserDefaults]objectForKey:@"lastSubmittedScore"]];
	}
	else if (code==NO_CONNECTION_MESSAGE){
		return @"No connection available.";
	}
	else if(code==BEINTOO_ERROR_MESSAGE){
		return @"An errorr occurred.";
	}
	else 
		return @"NO_MESSAGE";
}

- (void)dealloc {
	[current_achievement release];
    [super dealloc];
}

@end
