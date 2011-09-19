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

#import "BeintooVgood.h"
#import "Beintoo.h"

@implementation BeintooVgood

@synthesize delegate,parser,generatedVGood,callingDelegate,_player;

-(id)init {
	if (self = [super init])
	{	
        parser = [[Parser alloc] init];
		parser.delegate = self;
		rest_resource = [[NSString alloc] initWithString:[NSString stringWithFormat:@"%@/vgood/",[Beintoo getRestBaseUrl]]];

		_player = [[BeintooPlayer alloc] init];
	}
    return self;
}

- (NSString *)restResource{
	return rest_resource;
}

// -------------------------------------------------------------------------------------
// Get SINGLE vgood, no delegate. The response will be sent to the main delegate
// -------------------------------------------------------------------------------------
+ (void)getSingleVirtualGood{
	
	NSString *guid = [Beintoo getPlayerID];
	CLLocation *loc	 = [Beintoo getUserLocation];
	
	if (guid == nil) {
		NSLog(@"Beintoo: unable to generate a vgood. No user logged.");
		return;
	}
	
	NSString *res;
	BeintooVgood *vgoodService = [Beintoo beintooVgoodService];

	if (loc == nil || (loc.coordinate.latitude <= 0.01f && loc.coordinate.latitude >= -0.01f)) {
		res = [NSString stringWithFormat:@"%@byguid/%@?allowBanner=true&rows=1",[vgoodService restResource],guid];
	}
	else
		res	= [NSString stringWithFormat:@"%@byguid/%@?latitude=%f&longitude=%f&radius=%f&allowBanner=true&rows=1",
				[vgoodService restResource],guid,loc.coordinate.latitude,loc.coordinate.longitude,loc.horizontalAccuracy];
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[vgoodService.parser parsePageAtUrl:res withHeaders:params fromCaller:VGOOD_SINGLE_CALLER_ID];
	NSLog(@"singleNODeleg %@",res);
}

// -------------------------------------------------------------------------------------
// Get SINGLE vgood, with delegate. The response will be sent to a custom delegate
// -------------------------------------------------------------------------------------
+ (void)getSingleVirtualGoodWithDelegate:(id)_delegate{
	NSString *guid = [Beintoo getPlayerID];
	CLLocation *loc	 = [Beintoo getUserLocation];
	

	if (guid == nil) {
		NSLog(@"Beintoo: unable to generate a vgood. No user logged.");
		return;
	}
	
	NSString *res;
	BeintooVgood *vgoodService = [Beintoo beintooVgoodService];

	if (loc == nil || (loc.coordinate.latitude <= 0.01f && loc.coordinate.latitude >= -0.01f)) {
		res = [NSString stringWithFormat:@"%@byguid/%@?allowBanner=true&rows=1",[vgoodService restResource],guid];
	}
	else
		res	= [NSString stringWithFormat:@"%@byguid/%@?latitude=%f&longitude=%f&radius=%f&allowBanner=false&rows=1",
			   [vgoodService restResource],guid,loc.coordinate.latitude,loc.coordinate.longitude,loc.horizontalAccuracy];
	

	vgoodService.callingDelegate = _delegate;
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[vgoodService.parser parsePageAtUrl:res withHeaders:params fromCaller:VGOOD_SINGLEwDELEG_CALLER_ID];
}

// -------------------------------------------------------------------------------------
// Get MULTIPLE vgood, no delegate. The response will be sent to the main delegate
// -------------------------------------------------------------------------------------
+ (void)getMultipleVirtualGood{
	NSString *guid = [Beintoo getPlayerID];	
	CLLocation *loc	 = [Beintoo getUserLocation];
		
	if (guid == nil) {
		NSLog(@"Beintoo: unable to generate a vgood. No user logged.");
		return;
	}
	NSString *res;
	BeintooVgood *vgoodService = [Beintoo beintooVgoodService];
	
	if (loc == nil || (loc.coordinate.latitude <= 0.01f && loc.coordinate.latitude >= -0.01f) 
				   || (loc.coordinate.longitude <= 0.01f && loc.coordinate.longitude >= -0.01f)) {
		res = [NSString stringWithFormat:@"%@byguid/%@?allowBanner=true&rows=3",[vgoodService restResource],guid];
	}
	else
		res	= [NSString stringWithFormat:@"%@byguid/%@?latitude=%f&longitude=%f&radius=%f&allowBanner=true&rows=3",
			   [vgoodService restResource],guid,loc.coordinate.latitude,loc.coordinate.longitude,loc.horizontalAccuracy];

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[vgoodService.parser parsePageAtUrl:res withHeaders:params fromCaller:VGOOD_MULTIPLE_CALLER_ID];
	NSLog(@"multipleNODeleg %@ and params %@",res,params);
}

// -------------------------------------------------------------------------------------
// Get MULTIPLE vgood, with delegate. The response will be sent to a custom delegate
// -------------------------------------------------------------------------------------
+ (void)getMultipleVirtualGoodWithDelegate:(id)_delegate{
	NSString *guid = [Beintoo getPlayerID];	
	CLLocation *loc	 = [Beintoo getUserLocation];
	
	if (guid == nil) {
		NSLog(@"Beintoo: unable to generate a vgood. No user logged.");
		return;
	}
	NSString *res;
	BeintooVgood *vgoodService = [Beintoo beintooVgoodService];
	
	if (loc == nil || (loc.coordinate.latitude <= 0.01f && loc.coordinate.latitude >= -0.01f)) {
		res = [NSString stringWithFormat:@"%@byguid/%@?allowBanner=true&rows=3",[vgoodService restResource],guid];
	}
	else
		res	= [NSString stringWithFormat:@"%@byguid/%@?latitude=%f&longitude=%f&radius=%f&allowBanner=true&rows=3",
			   [vgoodService restResource],guid,loc.coordinate.latitude,loc.coordinate.longitude,loc.horizontalAccuracy];
	
	vgoodService.callingDelegate = _delegate;
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[vgoodService.parser parsePageAtUrl:res withHeaders:params fromCaller:VGOOD_MULTIPLEwDELEG_CALLER_ID];
	NSLog(@"multipleDeleg %@",res);
}


+ (void)notifyVGoodGenerationOnUserDelegate{
	BeintooVgood *vgoodService = [Beintoo beintooVgoodService];
	id _callingDelegate = vgoodService.callingDelegate;
	
	if ([_callingDelegate respondsToSelector:@selector(didBeintooGenerateAVirtualGood:)]) {
		[_callingDelegate didBeintooGenerateAVirtualGood:[Beintoo getLastGeneratedVGood]];
	}	
}

+ (void)notifyVGoodGenerationErrorOnUserDelegate:(NSString *)_error{
	BeintooVgood *vgoodService = [Beintoo beintooVgoodService];
	id _callingDelegate = vgoodService.callingDelegate;

	if ([_callingDelegate respondsToSelector:@selector(didBeintooFailToGenerateAVirtualGoodWithError:)]) {
		[_callingDelegate didBeintooFailToGenerateAVirtualGoodWithError:_error];
	}	
}


#pragma mark -
#pragma mark API


- (void)showGoodsByUserForState:(int)state{
	NSString *res;
	if (state == TO_BE_CONVERTED) 
		res  = [NSString stringWithFormat:@"%@show/byuser/%@",rest_resource,[Beintoo getUserID]];
	if (state == CONVERTED) {
		res = [NSString stringWithFormat:@"%@show/byuser/%@/?onlyConverted=true",rest_resource,[Beintoo getUserID]];
	}
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:VGOOD_SHOWBYUSER_CALLER_ID];
}

- (void)sendGoodWithID:(NSString *)good_id asGiftToUser:(NSString *)ext_id_to{
	NSString *res  = [NSString stringWithFormat:@"%@sendasgift/%@/%@/%@",rest_resource,good_id,[Beintoo getUserID],ext_id_to];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:VGOOD_SENDGIFT_CALLER_ID];
}

- (void)acceptGoodWithId:(NSString *)good_id{
	NSString *res  = [NSString stringWithFormat:@"%@accept/%@/%@",rest_resource,good_id,[Beintoo getUserID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrl:res withHeaders:params fromCaller:VGOOD_ACCEPT_CALLER_ID];
}

#pragma mark -
#pragma mark MARKETPLACE

- (void)sellVGood:(NSString *)vGood_Id{
	NSString *res  = [NSString stringWithFormat:@"%@/marketplace/sell/%@/%@",rest_resource,vGood_Id,[Beintoo getUserID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrlWithPOST:res withHeaders:params fromCaller:MARKET_SELLVGOOD_CALLER_ID];
	NSLog(@"CALL %@ with params %@",res,params);
}	

- (void)showGoodsToBuy{
	NSString *res  = [NSString stringWithFormat:@"%@/marketplace/show/",rest_resource];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrlWithPOST:res withHeaders:params fromCaller:MARKET_GOODSTOBUY_CALLER_ID];
	NSLog(@"CALL %@ with params %@",res,params);
}
- (void)showGoodsToBuyFeatured{
	NSString *res  = [NSString stringWithFormat:@"%@/marketplace/show/?featured=true",rest_resource];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrlWithPOST:res withHeaders:params fromCaller:MARKET_GOODSTOBUY_CALLER_ID];
	NSLog(@"CALL %@ with params %@",res,params);
}
- (void)buyGoodFromUser:(NSString *)vGood_Id{
	NSString *res  = [NSString stringWithFormat:@"%@/marketplace/buy/%@/%@",rest_resource,vGood_Id,[Beintoo getUserID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrlWithPOST:res withHeaders:params fromCaller:MARKET_BUYVGOOD_CALLER_ID];
	NSLog(@"CALL %@ with params %@",res,params);
}
- (void)buyGoodFeatured:(NSString *)vGood_Id{
	NSString *res  = [NSString stringWithFormat:@"%@/marketplace/featured/buy/%@/%@",rest_resource,vGood_Id,[Beintoo getUserID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[Beintoo getApiKey], @"apikey", nil];
	[parser parsePageAtUrlWithPOST:res withHeaders:params fromCaller:MARKET_BUYVGOOD_CALLER_ID];
	NSLog(@"CALL %@ with params %@",res,params);
}


#pragma mark -
#pragma mark parser delegate

- (void)didFinishToParsewithResult:(NSDictionary *)result forCaller:(NSInteger)callerID{	
	switch (callerID){
			
		case VGOOD_SINGLE_CALLER_ID:{  // -------------------- SINGLE NO DELEGATE
			@try {
				//BLOG(@"multiple vgood result %@",result);
				if ([result objectForKey:@"messageID"]) {
					// No vgood is generated. a notification is sent to the main delegate
					[Beintoo notifyVGoodGenerationErrorOnMainDelegate:[result objectForKey:@"message"]];
					return;
				}
				NSArray *vgoodList = [result objectForKey:@"vgoods"];

				if (vgoodList == nil || [vgoodList count]<1) {
					[Beintoo notifyVGoodGenerationErrorOnMainDelegate:@"Beintoo Vgood Generation: Connection error or no Vgood retrieved."];
					return;
				}
				
				self.generatedVGood = [vgoodList objectAtIndex:0];
										
				BVirtualGood *vgood = [[BVirtualGood alloc] init];
				[vgood setVgoodContent:self.generatedVGood];
				[vgood setTheGood:self.generatedVGood];
				[Beintoo setLastGeneratedVgood:[vgood retain]];
				[vgood release];
						
				[Beintoo notifyVGoodGenerationOnMainDelegate];
			}
			@catch (NSException * e) {
				[_player logException:[NSString stringWithFormat:@"STACK: %@\n\nException: %@",[NSThread callStackSymbols],e]];
			}
		}
			break;
			
		case VGOOD_SINGLEwDELEG_CALLER_ID:{ // -------------------- SINGLE WITH DELEGATE
			@try {				
				//BLOG(@"multiple vgood result %@",result);
				if ([result objectForKey:@"messageID"]) {
					// No vgood is generated. a notification is sent to the main delegate
					[BeintooVgood notifyVGoodGenerationErrorOnUserDelegate:[result objectForKey:@"message"]];
					return;
				}
				NSArray *vgoodList = [result objectForKey:@"vgoods"];
				
				if (vgoodList == nil || [vgoodList count]<1) {
					[BeintooVgood notifyVGoodGenerationErrorOnUserDelegate:@"Beintoo Vgood Generation: Connection error or no Vgood retrieved."];
					return;
				}
				
				self.generatedVGood = [vgoodList objectAtIndex:0];
								
				BVirtualGood *vgood = [[BVirtualGood alloc] init];
				[vgood setVgoodContent:self.generatedVGood];
				[vgood setTheGood:self.generatedVGood];
				[Beintoo setLastGeneratedVgood:[vgood retain]];
				[vgood release];
				
				[BeintooVgood notifyVGoodGenerationOnUserDelegate];
			}
			@catch (NSException * e) {
				[_player logException:[NSString stringWithFormat:@"STACK: %@\n\nException: %@",[NSThread callStackSymbols],e]];
			}
		}
			break;
		
		case VGOOD_MULTIPLE_CALLER_ID:{  // -------------------- MULTIPLE NO DELEGATE
			@try {
				//BLOG(@"multiple vgood result %@",result);
				if ([result objectForKey:@"messageID"]) {
					// No vgood is generated. a notification is sent to the main delegate
					[Beintoo notifyVGoodGenerationErrorOnMainDelegate:[result objectForKey:@"message"]];
					return;
				}
				NSArray *vgoodList = [result objectForKey:@"vgoods"];
				
				if (vgoodList == nil || [vgoodList count]<1) {
					[Beintoo notifyVGoodGenerationErrorOnMainDelegate:@"Beintoo Vgood Generation: Connection error or no Vgood retrieved."];
					return;
				}

				if ([vgoodList count]==1 || ([[vgoodList objectAtIndex:0] objectForKey:@"isBanner"]!=nil) ) {
					// ------ We received only one vgood or a recommendation
					self.generatedVGood = [vgoodList objectAtIndex:0];
					
					BVirtualGood *vgood = [[BVirtualGood alloc] init];
					[vgood setVgoodContent:self.generatedVGood];
					[vgood setTheGood:self.generatedVGood];
					[Beintoo setLastGeneratedVgood:[vgood retain]];
					[vgood release];
					
					[Beintoo notifyVGoodGenerationOnMainDelegate];
				}
				if ([vgoodList count]>1 && ([[vgoodList objectAtIndex:0] objectForKey:@"isBanner"]==nil) ) { // ------ We received a list of vgood: this is a real multiple vgood
					
					BVirtualGood *vgood = [[BVirtualGood alloc] init];
					[vgood setTheGoodsList:vgoodList];
					[vgood setVgoodContent:[vgoodList objectAtIndex:0]];
					[vgood setIsMultiple:YES];
					[Beintoo setLastGeneratedVgood:[vgood retain]];
					[vgood release];
					
					[Beintoo notifyVGoodGenerationOnMainDelegate];
				}
			}
			@catch (NSException * e) {
				[_player logException:[NSString stringWithFormat:@"STACK: %@\n\nException: %@",[NSThread callStackSymbols],e]];
			}
		}
			break;
		
		case VGOOD_MULTIPLEwDELEG_CALLER_ID:{  // -------------------- MULTIPLE WITH DELEGATE
			@try {
				//BLOG(@"multiple vgood result %@",result);
				if ([result objectForKey:@"messageID"]) {
					// No vgood is generated. a notification is sent to the main delegate
					[BeintooVgood notifyVGoodGenerationErrorOnUserDelegate:[result objectForKey:@"message"]];
					return;
				}
				NSArray *vgoodList = [result objectForKey:@"vgoods"];
				
				if (vgoodList == nil || [vgoodList count]<1) {
					[BeintooVgood notifyVGoodGenerationErrorOnUserDelegate:@"Beintoo Vgood Generation: Connection error or no Vgood retrieved."];
					return;
				}
				
				if ([vgoodList count]==1 || ([[vgoodList objectAtIndex:0] objectForKey:@"isBanner"]!=nil) ) {
					// ------ We received only one vgood or a recommendation
					self.generatedVGood = [vgoodList objectAtIndex:0];
					
					BVirtualGood *vgood = [[BVirtualGood alloc] init];
					[vgood setVgoodContent:self.generatedVGood];
					[vgood setTheGood:self.generatedVGood];
					[Beintoo setLastGeneratedVgood:[vgood retain]];
					[vgood release];
					
					[BeintooVgood notifyVGoodGenerationOnUserDelegate];
				}
				if ([vgoodList count]>1 && ([[vgoodList objectAtIndex:0] objectForKey:@"isBanner"]==nil) ) { // ------ We received a list of vgood: this is a real multiple vgood
					
					BVirtualGood *vgood = [[BVirtualGood alloc] init];
					[vgood setTheGoodsList:vgoodList];
					[vgood setVgoodContent:[vgoodList objectAtIndex:0]];
					[vgood setIsMultiple:YES];
					[Beintoo setLastGeneratedVgood:[vgood retain]];
					[vgood release];
					
					[BeintooVgood notifyVGoodGenerationOnUserDelegate];
				}
			}	
			@catch (NSException * e) {
				[_player logException:[NSString stringWithFormat:@"STACK: %@\n\nException: %@",[NSThread callStackSymbols],e]];
			}
		}
			break;
			
				
		// ----------------------------------------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------------	
						
			
		case VGOOD_SHOWBYUSER_CALLER_ID:{
			[[self delegate]didGetAllvGoods:(NSArray *)result];
		}
			break;
			
		case VGOOD_ACCEPT_CALLER_ID:{
			//NSLog(@"ACCEPT RES: %@",result);
			if ([[self delegate] respondsToSelector:@selector(didAcceptVgood)]) {
				[[self delegate] didAcceptVgood];
			}
		}
			break;
			
		case VGOOD_SENDGIFT_CALLER_ID:{
			if ([[result objectForKey:@"message"] isEqualToString:@"OK"]) {
				[[self delegate]didSendVGoodAsGift:YES];
			}
			else {[[self delegate]didSendVGoodAsGift:NO];}
		}
			break;
			
			/* ------ MARKETPLACE ------- */
			
		case MARKET_SELLVGOOD_CALLER_ID:{
			//BLOG(@"sellVGOOD result: %@",result);
			if ([[result objectForKey:@"message"] isEqualToString:@"OK"]) {
				[[self delegate]didSellVGood:YES];
			}
			else {[[self delegate]didSellVGood:NO];}		
		}
			break;
		case MARKET_GOODSTOBUY_CALLER_ID:{
			//BLOG(@"vgood to buy result: %@",result);
			[[self delegate]didGetVGoodsToBuy:(NSArray *)result];
		}
			break;
		case MARKET_BUYVGOOD_CALLER_ID:{
			//BLOG(@"vgood bought result: %@",result);
			[[self delegate]didBuyVgoodWithResult:result];
		}
			break;
			
		default:{
			//statements
		}
			break;
	}	
}

- (void)dealloc {
	[parser release];
	[_player release];
	[rest_resource release];
	[super dealloc];
}


@end
