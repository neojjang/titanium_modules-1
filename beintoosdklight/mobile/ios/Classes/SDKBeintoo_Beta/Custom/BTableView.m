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

#import "BTableView.h"


@implementation BTableView

//@synthesize delegate;

- (void)commonInit
{
    self.userInteractionEnabled = YES;
	self.separatorColor = [UIColor clearColor];
	self.backgroundColor = [UIColor clearColor];
}

- (id)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        [self commonInit];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    if (self = [super initWithCoder:aDecoder])
    {
        [self commonInit];
    }
    return self;
}

- (void)reloadData{	
    [super reloadData];
	
	if ([self numberOfRowsInSection:0]>0) {
		if ([[self customDelegate] respondsToSelector:@selector(didEndLoadingTableData)]) 
			[[self customDelegate] didEndLoadingTableData];
	}
	
}

- (id<BeintooTableViewDelegate>) customDelegate {
	if ([[self delegate] conformsToProtocol:@protocol(BeintooTableViewDelegate)]) {
		return (id<BeintooTableViewDelegate>)[self delegate];
	}
	return nil;
}

@end
