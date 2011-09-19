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

#import "BLoadingView.h"
#import <QuartzCore/QuartzCore.h>


@implementation BLoadingView

static BLoadingView *loading;

- (id)initWithFrame:(CGRect)frame 
{
    if (self = [super initWithFrame:frame]) 
    {
        self.opaque = NO;
	
		hudView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 60, 60)];
		hudView.backgroundColor = [UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:0.7];
		hudView.clipsToBounds = YES;
		hudView.layer.cornerRadius = 10.0;
		
		activityIndicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
		activityIndicatorView.frame = CGRectMake(11, 11, activityIndicatorView.bounds.size.width, activityIndicatorView.bounds.size.height);
		[hudView addSubview:activityIndicatorView];
		[activityIndicatorView startAnimating];
		[self addSubview:hudView];
    }
    return self;
}

+ (void)startActivity:(UIView *)callingView{
	
	loading = [[BLoadingView alloc] initWithFrame:CGRectMake((callingView.frame.size.width/2)-30, (callingView.frame.size.height/2)-30, 100, 100)];
	loading.alpha = 0.0;
	[callingView addSubview:loading];
	
	[UIView animateWithDuration:0.5
						  delay: 0.0
						options: UIViewAnimationOptionCurveEaseIn
					 animations:^{
						 loading.alpha = 1.0;
					 }
					 completion:nil];
}

+ (void)stopActivity{
	if (loading!=nil) {
		[loading setHidden:YES];
		[loading release];
		loading = nil;
	}
}

- (void)dealloc {
	[hudView release];
	[activityIndicatorView release];
	[captionLabel release];
	[super dealloc];
}

@end
