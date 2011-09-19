/**
 * Beintoo  
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "TiBeintooUserProxy.h"
#import "Beintoo.h"

@implementation TiBeintooUserProxy

#pragma mark -
#pragma mark Public APIs

- (void)getUser:(id)args{
    BeintooUser *user = [[BeintooUser alloc] init];
    user.delegate = self;
    [user getUser];
}

- (void)getUserByMailAndPass:(id)args{
    NSString  *mail   = (NSString *)[args objectAtIndex:0];
    NSString  *pass   = (NSString *)[args objectAtIndex:1];
    
    BeintooUser *user = [[BeintooUser alloc] init];
    user.delegate = self;
    [user getUserByM:mail andP:pass];
}

- (void)registerUserWithMailAndNickname:(id)args{
    NSString  *mail   = (NSString *)[args objectAtIndex:0];
    NSString  *nick   = (NSString *)[args objectAtIndex:1];
    
    BeintooUser *user = [[BeintooUser alloc] init];
    user.delegate = self;
    [user registerUserToGuid:nil withEmail:mail
                    nickname:nick password:nil name:nil country:nil address:nil gender:nil sendGreetingsEmail:YES];
}

#pragma mark -
#pragma mark Callbacks

- (void)didGetUser:(NSDictionary *)result{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:result,@"result",nil];
    if ([self _hasListeners:@"onUserGetuser"]) {
        [self fireEvent:@"onUserGetuser" withObject:arg];
    }
}

- (void)didGetUserByMail:(NSDictionary *)result{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:result,@"result",nil];
    if ([self _hasListeners:@"onUserGetuserByMandP"]) {
        [self fireEvent:@"onUserGetuserByMandP" withObject:arg];
    }
}

- (void)didCompleteRegistration:(NSDictionary *)result{
    NSDictionary *arg = [NSDictionary dictionaryWithObjectsAndKeys:result,@"result",nil];
    if ([self _hasListeners:@"onUserRegistration"]) {
        [self fireEvent:@"onUserRegistration" withObject:arg];
    }
}


@end

