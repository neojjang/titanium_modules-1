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

#import "BeintooAlerts.h"

@implementation BeintooAlerts

- (id)init {
	if (self = [super init])
	{
	}
    return self;
}

+ (void)showNoUserAlert{
	UIAlertView *av = [[UIAlertView alloc] initWithTitle:@"Beintoo" 
												 message:NSLocalizedStringFromTable(@"noUserMessage",@"BeintooLocalizable",@"")
												delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
	[av show];
	[av release];
}


- (void)dealloc {
    [super dealloc];
}

@end
