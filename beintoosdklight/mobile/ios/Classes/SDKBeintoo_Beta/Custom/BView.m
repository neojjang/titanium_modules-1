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

#import "BView.h"
#import "BGradientView.h"

@implementation BView

@synthesize topView,bodyView,footerView,topHeight,bodyHeight,isScrollView;

- (id)initWithFrame:(CGRect)frame{
    if (self = [super initWithFrame:frame]){
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder{
    if (self = [super initWithCoder:aDecoder]){
		[self setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"tile.png"]]];
	}
    return self;
}

- (void)drawRect:(CGRect)rect {
	self.contentMode = UIViewContentModeRedraw;
	[self removeViews];

	self.topView = [[BGradientView alloc] initWithGradientType:GRADIENT_HEADER];
	self.topView.frame =  CGRectMake(0, 0, [self bounds].size.width, self.topHeight);
	self.topView.tag = 1;
	[self addSubview:self.topView];
	
	self.bodyView = [[BGradientView alloc] initWithGradientType:GRADIENT_BODY];
	if (isScrollView) 
		self.bodyView.frame = CGRectMake(0, self.topHeight, [self bounds].size.width, self.bodyHeight-self.topHeight);
	else
		self.bodyView.frame = CGRectMake(0, self.topHeight, [self bounds].size.width, ([self bounds].size.height-self.topHeight)-9);
	self.bodyView.tag = 2;
	[self addSubview:self.bodyView];
	
	self.footerView = [[BGradientView alloc] initWithGradientType:GRADIENT_FOOTER];
	self.footerView.frame = CGRectMake(0, [self bounds].size.height-9,[self bounds].size.width, 9);
	self.footerView.tag = 3;
	if (!isScrollView) {
		[self addSubview:self.footerView];
	}

	[self sendSubviewToBack:self.bodyView];
	[self sendSubviewToBack:self.topView];
	[self sendSubviewToBack:self.footerView];
}

- (void) removeViews {
	[[self viewWithTag:1] removeFromSuperview];
	[[self viewWithTag:2] removeFromSuperview];
	[[self viewWithTag:3] removeFromSuperview];
}

- (void)dealloc {
    [super dealloc];
	[self.topView release];
	[self.bodyView release];
	[self.footerView release];
}	
@end
