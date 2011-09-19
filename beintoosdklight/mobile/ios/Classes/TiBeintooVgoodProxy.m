/**
 * Beintoo  
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "TiBeintooVgoodProxy.h"
#import "Beintoo.h"

@implementation TiBeintooVgoodProxy

#pragma mark -
#pragma mark Public APIs

- (void)getVgood:(id)args{
    [BeintooVgood getSingleVirtualGoodWithDelegate:self];
}

#pragma mark -
#pragma mark Callbacks

- (void)didBeintooGenerateAVirtualGood:(BVirtualGood *)theVgood{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:[theVgood theGood],@"result",nil];
    if ([self _hasListeners:@"onVgoodGenerated"]) {
        [self fireEvent:@"onVgoodGenerated" withObject:arg];
    }
	NSLog(@"Vgood: %@",[theVgood theGood]);
}

- (void)didBeintooFailToGenerateAVirtualGoodWithError:(NSDictionary *)error{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:error,@"result",nil];
    if ([self _hasListeners:@"onVgoodGeneratedError"]) {
        [self fireEvent:@"onVgoodGeneratedError" withObject:arg];
    }
}


@end

