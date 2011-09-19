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

#import "BButton.h"


@implementation BButton

@synthesize _highColor, _rollHighColor;
@synthesize _lowColor, _rollLowColor;
@synthesize _mediumLowColor,_rollMediumLowColor;
@synthesize _mediumHighColor, _rollMediumHighColor;
@synthesize gradientLayer, textSize;;

- (id)initWithFrame:(CGRect)frame 
{
    if (self = [super initWithFrame:frame]) {
		// Initialize the gradient layer
		gradientLayer = [[CAGradientLayer alloc] init];
		
		// Set its bounds to be the same of its parent
		//[gradientLayer setBounds:[self bounds]];
		gradientLayer.frame = self.bounds;   // make the subview frame match its view

		// Center the layer inside the parent layer
		[gradientLayer setPosition:
		 CGPointMake([self bounds].size.width/2,
					 [self bounds].size.height/2)];
		
		// Insert the layer at position zero to make sure the 
		// text of the button is not obscured
		[[self layer] insertSublayer:gradientLayer atIndex:0];
		
		// Set the layer's corner radius
		[[self layer] setCornerRadius:0];
		// Turn on masking
		[[self layer] setMasksToBounds:YES];
		// Display a border around the button 
		// with a 1.0 pixel width
		[[self layer] setBorderWidth:1.0f];
		[[self layer] setBorderColor:[UIColor colorWithWhite:0 alpha:0.3].CGColor];
		
		isSelected = NO;
    }
    return self;
}

- (void)awakeFromNib{
    // Initialize the gradient layer
    gradientLayer = [[CAGradientLayer alloc] init];
    // Set its bounds to be the same of its parent
    [gradientLayer setBounds:[self bounds]];
    // Center the layer inside the parent layer
    [gradientLayer setPosition:
	 CGPointMake([self bounds].size.width/2,
				 [self bounds].size.height/2)];
	
    // Insert the layer at position zero to make sure the 
    // text of the button is not obscured
    [[self layer] insertSublayer:gradientLayer atIndex:0];
	
    // Set the layer's corner radius
    [[self layer] setCornerRadius:0];
    // Turn on masking
    [[self layer] setMasksToBounds:YES];
    // Display a border around the button 
    // with a 1.0 pixel width
    [[self layer] setBorderWidth:1.0f];
	[[self layer] setBorderColor:[UIColor colorWithWhite:0 alpha:0.3].CGColor];
		
	isSelected = NO;
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context{
    [self setNeedsDisplay];
}

- (void)drawRect:(CGRect)rect{
	self.titleLabel.adjustsFontSizeToFitWidth = YES;
	[self setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
	if (textSize == nil ) 
		self.titleLabel.font = [UIFont systemFontOfSize:22];
	else 
		self.titleLabel.font = [UIFont systemFontOfSize:[textSize floatValue]];

	self.titleLabel.shadowColor = [UIColor blackColor];
	self.titleLabel.shadowOffset = CGSizeMake(0, -1);
	
    if (_highColor && _lowColor && !isSelected)
    {
		[gradientLayer setLocations:[NSArray arrayWithObjects:
								[NSNumber numberWithFloat:0.0], 
								[NSNumber numberWithFloat:0.500], 
								[NSNumber numberWithFloat:0.500], 
								[NSNumber numberWithFloat:1.0], nil]];
        [gradientLayer setColors:
		 [NSArray arrayWithObjects:
		  (id)[_highColor CGColor],
		  (id)[_mediumLowColor CGColor],
		  (id)[_mediumHighColor CGColor],
		  (id)[_lowColor CGColor], nil]];
    }
	if (isSelected) {
		[gradientLayer setColors:
		[NSArray arrayWithObjects:
		(id)[_rollHighColor CGColor],
		(id)[_rollMediumLowColor CGColor],
		(id)[_rollMediumHighColor CGColor],
		(id)[_rollLowColor CGColor], nil]];
	}
    [super drawRect:rect];
}

- (void)hesitateUpdate{
    [self setNeedsDisplay];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [super touchesBegan:touches withEvent:event];
	isSelected = YES;
    [self setNeedsDisplay];
}
- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
    [super touchesCancelled:touches withEvent:event];
	isSelected = NO;
    [self setNeedsDisplay];
    [self performSelector:@selector(hesitateUpdate) withObject:nil afterDelay:0.1];
}
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event{
    [super touchesMoved:touches withEvent:event];
    [self setNeedsDisplay];
    
}
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
    [super touchesEnded:touches withEvent:event];
	isSelected = NO;
    [self setNeedsDisplay];
    [self performSelector:@selector(hesitateUpdate) withObject:nil afterDelay:0.1];
}

- (void)setHighColor:(UIColor*)color andRollover:(UIColor *)rollover{
    [self set_highColor:color];
	[self set_rollHighColor:rollover];
    [[self layer] setNeedsDisplay];
}

- (void)setLowColor:(UIColor*)color andRollover:(UIColor *)rollover{
    [self set_lowColor:color];
	[self set_rollLowColor:rollover];
    [[self layer] setNeedsDisplay];
}

- (void)setMediumHighColor:(UIColor*)color andRollover:(UIColor *)rollover{
    [self set_mediumLowColor:color];
	[self set_rollMediumLowColor:rollover];
    [[self layer] setNeedsDisplay];
}

- (void)setMediumLowColor:(UIColor*)color andRollover:(UIColor *)rollover{
    [self set_mediumHighColor:color];
	[self set_rollMediumHighColor:rollover];
    [[self layer] setNeedsDisplay];
}

- (void)setButtonTextSize:(int)size{
	[self setTextSize:[NSNumber numberWithInt:size]];
}

- (void)dealloc {
    [gradientLayer release];
    [super dealloc];
}

@end
