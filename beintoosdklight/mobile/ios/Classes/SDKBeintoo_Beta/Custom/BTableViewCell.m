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

#import "BTableViewCell.h"
#import "BGradientView.h"

@implementation BTableViewCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier andGradientType:(int)gradientType {
    if (self = [super initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:reuseIdentifier]) {
        CGRect cellFrame = CGRectMake(0.0, 0.0, self.contentView.bounds.size.width,
									 self.contentView.bounds.size.height);

        beintooCellView = [[BGradientView alloc] initWithGradientType:gradientType];
		beintooCellView.frame = cellFrame;
        beintooCellView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        
		beintooSelectedCellView = [[BGradientView alloc] initWithGradientType:GRADIENT_CELL_SELECTED];
		beintooSelectedCellView.frame = cellFrame;
		beintooSelectedCellView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

		self.backgroundView = beintooCellView;
		self.selectedBackgroundView = beintooSelectedCellView;
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {	
    [super setSelected:selected animated:animated];
    self.textLabel.textColor = [UIColor blackColor];
    self.detailTextLabel.textColor = [UIColor colorWithRed:113.0/255 green:116.0/255 blue:121.0/255 alpha:1];	
}

- (void)dealloc {
    [super dealloc];
}

@end
