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

#import "BScrollView.h"
#import <Quartzcore/QuartzCore.h>


@implementation BScrollView

-(void) touchesEnded: (NSSet *) touches withEvent: (UIEvent *) event {	
	
	if (!self.dragging) {
		[self.nextResponder touchesEnded: touches withEvent:event]; 
	}		
	
	[super touchesEnded: touches withEvent: event];
}

-(void)setGradientType:(int)_gradientType{
	gradientType = _gradientType;
}

- (void) removeViews {
	[[self viewWithTag:22] removeFromSuperview];
	[[self viewWithTag:33] removeFromSuperview];
}

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
	self.contentMode = UIViewContentModeRedraw;
	
	CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGColorSpaceRef colorSpace  = CGColorSpaceCreateDeviceRGB();
	
#pragma mark -
#pragma mark GRADIENT_BODY
	
	switch (gradientType) {
		case GRADIENT_BODY:{
			CGFloat components[8] = {
				250.0/255, 250.0/255, 250.0/255, 1.0f,
				220.0/255, 220.0/255, 220.0/255, 1.0f,
			};
			
			[self removeViews];
			
			CGFloat locations[2] = {0.0 , 1};
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace, components,locations,(size_t)2);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			CGGradientRelease(gradient);
		}
			break;
			
#pragma mark -
#pragma mark GRADIENT_HEADER
			
		case GRADIENT_HEADER:{
			CGFloat components[8] = {
				226.0/255, 238.0/255, 254.0/255, 1.0f,
				159.0/255, 182.0/255, 212.0/255, 1.0f,
			};
			
			[self removeViews];
			
			CGFloat locations[2] = {0.0  , 1};
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace,components,locations,(size_t)2);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			
			UIView *upperBorder = [[UIView alloc]initWithFrame:CGRectMake(0, 0, [self bounds].size.width, 1)];
			UIView *lowerBorder = [[UIView alloc]initWithFrame:CGRectMake(0, [self bounds].size.height-1, self.frame.size.width, 1)];
			
			upperBorder.backgroundColor = [UIColor colorWithWhite:1 alpha:0.7];
			upperBorder.tag		= 22;
			lowerBorder.backgroundColor = [UIColor colorWithRed:107.0/255 green:109.0/255 blue:112.0/255 alpha:1.0];
			lowerBorder.tag		= 33;
			
			[self addSubview:upperBorder];
			[self addSubview:lowerBorder];
			
			[upperBorder release];
			[lowerBorder release];
			CGGradientRelease(gradient);
		}
			break;
			
#pragma mark -
#pragma mark GRADIENT_FOOTER
			
		case GRADIENT_FOOTER:{
			CGFloat components[12] = {
				108.0/255, 128.0/255, 154.0/255, 1.0f,
				108.0/255, 128.0/255, 154.0/255, 1.0f,
				138.0/255, 158.0/255, 184.0/255, 1.0f,
			};
			
			CGFloat locations[3] = {0.0 , 0.5  , 1};
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace,components,locations,(size_t)3);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			
			CGGradientRelease(gradient);
		}
			break;	
			
#pragma mark -
#pragma mark GRADIENT_BUTTONS
			
		case GRADIENT_BUTTONS:{
			CGFloat components[12] = {
				184.0/255, 188.0/255, 193.0/255, 1.0f,
				151.0/255, 165.0/255, 175.0/255, 1.0f,
				134.0/255, 157.0/255, 168.0/255, 1.0f
			};
			CGFloat locations[3] = {0.0 , 0.5 , 1};
			
			self.layer.cornerRadius  = 5.0;
			self.layer.masksToBounds = YES;
			self.layer.borderWidth   = 1.0;
			//self.layer.borderColor	 = [UIColor colorWithRed:1 green:0 blue:1 alpha:1].CGColor;
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace,components,locations,(size_t)3);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			
			CGGradientRelease(gradient);
		}
			break;
			
#pragma mark -
#pragma mark GRADIENT_CELLS
			
		case GRADIENT_CELL_HEAD:{
			CGFloat components[8] = {
				226.0/255, 238.0/255, 254.0/255, 1.0f,
				159.0/255, 182.0/255, 212.0/255, 1.0f,
			};
			
			CGFloat locations[2] = {0.0 , 1};
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace, components,locations,(size_t)2);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			
			UIView *upperBorder = [[UIView alloc]initWithFrame:CGRectMake(0, 0, [self bounds].size.width, 1)];
			UIView *lowerBorder = [[UIView alloc]initWithFrame:CGRectMake(0, [self bounds].size.height-1, self.frame.size.width, 1)];
			
			upperBorder.backgroundColor = [UIColor colorWithWhite:1 alpha:1];
			lowerBorder.backgroundColor = [UIColor colorWithRed:107.0/255 green:109.0/255 blue:112.0/255 alpha:1.0];
			
			[self addSubview:upperBorder];
			[self addSubview:lowerBorder];
			
			[upperBorder release];
			[lowerBorder release];
			CGGradientRelease(gradient);
		}
			break;
			
		case GRADIENT_CELL_BODY:{
			CGFloat components[8] = {
				250.0/255, 250.0/255, 250.0/255, 1.0f,
				220.0/255, 220.0/255, 220.0/255, 1.0f,
			};
			
			CGFloat locations[2] = {0.0 , 1};
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace, components,locations,(size_t)2);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			
			UIView *upperBorder = [[UIView alloc]initWithFrame:CGRectMake(0, 0, [self bounds].size.width, 1)];
			UIView *lowerBorder = [[UIView alloc]initWithFrame:CGRectMake(0, [self bounds].size.height-1, self.frame.size.width, 1)];
			
			upperBorder.backgroundColor = [UIColor colorWithWhite:1 alpha:1];
			lowerBorder.backgroundColor = [UIColor colorWithRed:107.0/255 green:109.0/255 blue:112.0/255 alpha:1.0];
			
			[self addSubview:upperBorder];
			[self addSubview:lowerBorder];
			
			[upperBorder release];
			[lowerBorder release];
			CGGradientRelease(gradient);
		}
			break;
			
		case GRADIENT_CELL_SELECTED:{
			CGFloat components[8] = {
				198.0/255, 202.0/255, 206.0/255, 1.0f,
				158.0/255, 166.0/255, 175.0/255, 1.0f,
			};
			
			CGFloat locations[2] = {0.0 , 1};
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace, components,locations,(size_t)2);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			
			UIView *upperBorder = [[UIView alloc]initWithFrame:CGRectMake(0, 0, [self bounds].size.width, 1)];
			UIView *lowerBorder = [[UIView alloc]initWithFrame:CGRectMake(0, [self bounds].size.height-1, self.frame.size.width, 1)];
			
			upperBorder.backgroundColor = [UIColor colorWithWhite:1 alpha:1];
			lowerBorder.backgroundColor = [UIColor colorWithRed:107.0/255 green:109.0/255 blue:112.0/255 alpha:1.0];
			
			[self addSubview:upperBorder];
			[self addSubview:lowerBorder];
			
			[upperBorder release];
			[lowerBorder release];
			CGGradientRelease(gradient);
		}
			break;
			
		case GRADIENT_TOOLBAR:{
			CGFloat components[16] = {
				156.0/255, 168.0/255, 184.0/255, 1.0f,
				116.0/255, 135.0/255, 159.0/255, 1.0f,
				108.0/255, 128.0/255, 154.0/255, 1.0f,
				89.0/255,  112.0/255, 142.0/255, 1.0f,
			};
			
			CGFloat locations[4] = {0.0 ,0.5, 0.5, 1};
			
			CGGradientRef gradient = CGGradientCreateWithColorComponents(colorSpace, components,locations,(size_t)4);
			
			CGPoint topCenter = CGPointMake(CGRectGetMidX(self.bounds), 0.0f);
			CGPoint midCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMaxY(self.bounds));
			
			CGContextDrawLinearGradient(ctx, gradient, topCenter, midCenter, (CGGradientDrawingOptions)NULL);
			
			CGGradientRelease(gradient);
		}
			break;
	}
	
	CGColorSpaceRelease(colorSpace);
}

- (void)dealloc {
    [super dealloc];
}


@end
