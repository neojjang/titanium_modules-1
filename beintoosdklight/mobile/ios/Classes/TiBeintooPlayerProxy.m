/**
 * Beintoo  
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "TiBeintooPlayerProxy.h"
#import "Beintoo.h"

@implementation TiBeintooPlayerProxy

#pragma mark Public APIs

- (void)login:(id)args
{
    [BeintooPlayer setPlayerDelegate:self];
    [BeintooPlayer login];
}

- (void)loginWithUserID:(id)args
{
    ENSURE_SINGLE_ARG(args, NSString);
    [BeintooPlayer setPlayerDelegate:self];
    [BeintooPlayer loginWithUserID:args];
}

- (void)logout:(id)args{
    [Beintoo playerLogout];
    NSLog(@"[INFO][Beintoo] Beintoo player logged out.");
}

- (void)submitScore:(id)args{
    NSString  *scoreValue   = (NSString *)[args objectAtIndex:0];
    NSString  *contestName  = (NSString *)[args objectAtIndex:1];
    
    [BeintooPlayer setPlayerDelegate:self];
    [BeintooPlayer submitScore:[scoreValue intValue] forContest:contestName];
}

- (void)getScore:(id)args{
    [BeintooPlayer setPlayerDelegate:self];
    [BeintooPlayer getScore];
}

- (void)setBalance:(id)args{
    NSString  *scoreValue   = (NSString *)[args objectAtIndex:0];
    NSString  *contestName  = (NSString *)[args objectAtIndex:1];
    
    [BeintooPlayer setPlayerDelegate:self];
	[BeintooPlayer setBalance:[scoreValue intValue] forContest:contestName];
}

// -------------- PLAYER LOGIN CALLBACKS
- (void)playerDidLoginWithResult:(NSDictionary *)result{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:result,@"player",nil];
    if ([self _hasListeners:@"onPlayerLogin"]) {
        [self fireEvent:@"onPlayerLogin" withObject:arg];
    }
    NSLog(@"[INFO][Beintoo] PlayerLogin result %@",result);
}

- (void)playerDidFailLoginWithResult:(NSString *)error{
   
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:error,@"result",nil];
    if ([self _hasListeners:@"onPlayerLoginError"]) {
        [self fireEvent:@"onPlayerLoginError" withObject:arg];
    }
}

// -------------- PLAYER SUBMITSORE CALLBACKS
- (void)playerDidSumbitScoreWithResult:(NSString *)result{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:result,@"result",nil];
    if ([self _hasListeners:@"onPlayerSubmitScore"]) {
        [self fireEvent:@"onPlayerSubmitScore" withObject:arg];
    }
    //NSLog(@"[INFO][Beintoo] Submit score result %@",result);
}
- (void)playerDidFailSubmitScoreWithError:(NSString *)error{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:error,@"result",nil];
    if ([self _hasListeners:@"onPlayerSubmitScoreError"]) {
        [self fireEvent:@"onPlayerSubmitScoreError" withObject:arg];
    }
}

// -------------- PLAYER GETSCORE CALLBACKS
- (void)playerDidGetScoreWithResult:(NSDictionary *)result{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:result,@"result",nil];
    if ([self _hasListeners:@"onPlayerGetscore"]) {
        [self fireEvent:@"onPlayerGetscore" withObject:arg];
    }
    //NSLog(@"[INFO][Beintoo] Get Score result %@",result);
}

- (void)playerDidFailGetScoreWithError:(NSString *)error{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:error,@"result",nil];
    if ([self _hasListeners:@"onPlayerGetscoreError"]) {
        [self fireEvent:@"onPlayerGetscoreError" withObject:arg];
    }
}

// -------------- PLAYER SETBALANCE CALLBACKS
- (void)playerDidSetBalanceWithResult:(NSString *)result{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:result,@"result",nil];
    if ([self _hasListeners:@"onPlayerSetBalance"]) {
        [self fireEvent:@"onPlayerSetBalance" withObject:arg];
    }
}
- (void)playerDidFailSetBalanceWithError:(NSString *)error{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:error,@"result",nil];
    if ([self _hasListeners:@"onPlayerSetBalanceError"]) {
        [self fireEvent:@"onPlayerSetBalanceError" withObject:arg];
    }
}

@end

