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

#import "BVirtualGood.h"
#import "Beintoo.h"

@implementation BVirtualGood

@synthesize theGood,theGoodsList,getItRealURL,vGoodDescription,vGoodImageData,vGoodName,vGoodEndDate,vGoodID,whoAlsoConverted;

-(id)init {
	if (self = [super init])
	{
		theGood = [[NSDictionary alloc] init];
		theGoodsList = [[NSArray alloc] init];
	}
    return self;
}

- (void)setVgoodContent:(NSDictionary *)_vgood{
	if (_vgood != nil && ([_vgood objectForKey:@"getRealURL"]!=nil) ) {
		@try {
			self.theGood			= _vgood;
			
			self.getItRealURL		= [_vgood objectForKey:@"getRealURL"];
			self.vGoodDescription	= [_vgood objectForKey:@"description"];
			self.vGoodName			= [_vgood objectForKey:@"name"];
			self.vGoodEndDate		= [_vgood objectForKey:@"enddate"];
			self.vGoodID			= [_vgood objectForKey:@"id"];
			self.vGoodImageData		= [BeintooNetwork getSynchImageWithUA:[_vgood objectForKey:@"imageUrl"]];
			self.whoAlsoConverted	= [_vgood objectForKey:@"whoAlsoConverted"];
			
			if ([_vgood objectForKey:@"isBanner"]!=nil){
				isRecommendation = TRUE;
			}else {
				isRecommendation = FALSE;
			}
			isMultipleVgood = FALSE;
		}
		@catch (NSException * e) {
			NSLog(@"BEINTOO ERROR: Unable to set the vgood: %@",e);
		}	
	}
}

- (BOOL)isMultiple{
	return isMultipleVgood;
}
- (void)setIsMultiple:(BOOL)_isMultiple{
	isMultipleVgood = _isMultiple;
}

- (BOOL)isRecommendation{
	return isRecommendation;
}

- (void)dealloc {
    [super dealloc];
	[theGood release];
	[theGoodsList release];
}

@end
