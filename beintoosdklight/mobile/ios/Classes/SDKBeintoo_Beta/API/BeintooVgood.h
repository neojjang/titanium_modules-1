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

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "Parser.h"
#import "BeintooPlayer.h"

#define TO_BE_CONVERTED 40
#define CONVERTED		41

@protocol BeintooVgoodDelegate;

@interface BeintooVgood : NSObject <BeintooParserDelegate>{
	
	Parser	*parser;
	BeintooPlayer *_player;
	id <BeintooVgoodDelegate> delegate;
	BOOL    isMultipleVgood;
	BOOL	isRecommendation;
	NSDictionary		 *generatedVGood;
	
	NSString *rest_resource;
	
	id callingDelegate;
}

+ (void)getSingleVirtualGood;
+ (void)getSingleVirtualGoodWithDelegate:(id)_delegate;
+ (void)getMultipleVirtualGood;
+ (void)getMultipleVirtualGoodWithDelegate:(id)_delegate;

+ (void)notifyVGoodGenerationOnUserDelegate;
+ (void)notifyVGoodGenerationErrorOnUserDelegate:(NSString *)_error;

- (NSString *)restResource;

/* --- VGOODS ---- */
- (void)showGoodsByUserForState:(int)state;
- (void)sendGoodWithID:(NSString *)good_id asGiftToUser:(NSString *)ext_id_to;
- (void)acceptGoodWithId:(NSString *)good_id;

/* --- MARKETPLACE --- */
- (void)sellVGood:(NSString *)vGood_Id;
- (void)showGoodsToBuy;
- (void)showGoodsToBuyFeatured;
- (void)buyGoodFromUser:(NSString *)vGood_Id;
- (void)buyGoodFeatured:(NSString *)vGood_Id;

@property(nonatomic, assign) id <BeintooVgoodDelegate> delegate;
@property(nonatomic, assign) id  callingDelegate;
@property(nonatomic,retain) Parser *parser;
@property(nonatomic,retain) NSDictionary *generatedVGood;
@property(nonatomic,retain) BeintooPlayer *_player;

@end


@protocol BeintooVgoodDelegate <NSObject>

@optional
- (void)didGenerateVgood:(BOOL)isVgoodGenerated withResult:(BeintooVgood *)theVgood;
- (void)didGetAllvGoods:(NSArray *)vGoodList;
- (void)didSendVGoodAsGift:(BOOL)result;
- (void)didAcceptVgood;

// MARKETPLACE
- (void)didSellVGood:(BOOL)result;
- (void)didGetVGoodsToBuy:(NSArray *)vGoodList;
- (void)didBuyVgoodWithResult:(NSDictionary *)goodBought;

@end
