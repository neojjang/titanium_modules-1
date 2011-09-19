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

#import "Parser.h"
#import "SBJSON.h"
#import "BeintooNetwork.h"
#import "Beintoo.h"
 

@implementation Parser
@synthesize delegate, request,callerID,webpage;

-(id)init {
	if (self = [super init])
	{
	}
    return self;
}

- (void)parsePageAtUrl:(NSString *)URL withHeaders:(NSDictionary *)headers fromCaller:(int)caller{
	@synchronized(self){		
		self.callerID = caller;
		if (![BeintooNetwork connectedToNetwork]) {
			if (caller == PLAYER_SSCORE_NOCONT_CALLER_ID || caller == PLAYER_SSCORE_CONT_CALLER_ID || 
				caller == PLAYER_GSCOREFORCONT_CALLER_ID || caller == PLAYER_LOGIN_CALLER_ID ||
				caller == ACHIEVEMENTS_GETSUBMITPERCENT_CALLER_ID || caller == ACHIEVEMENTS_GETSUBMITSCORE_CALLER_ID ) {
				NSLog(@"Beintoo - no connection available!");
				return;
			}
			[self performSelectorOnMainThread:@selector(parsingEnd:) withObject:nil waitUntilDone:YES];
			[BeintooNetwork showNoConnectionAlert];
			return;
		}

		NSURL *serviceURL = [NSURL URLWithString:URL];
		//NSURL *serviceURL = [NSURL URLWithString:@"http://www.beachscout.it/vgoodmultiple.html"];
		self.request = [[[NSMutableURLRequest alloc] initWithURL:serviceURL cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:60.0] autorelease];
		[self.request setHTTPMethod:@"GET"];

		for (id theKey in headers) {
			[self.request setValue:[NSString stringWithFormat:@"%@",[headers objectForKey:theKey]] forHTTPHeaderField:theKey];
		}
		[self.request setValue:[Beintoo currentVersion] forHTTPHeaderField:@"X-BEINTOO-SDK-VERSION"];

		
		if (self.callerID == VGOOD_MULTIPLE_CALLER_ID || self.callerID == VGOOD_SINGLE_CALLER_ID ||
			self.callerID == VGOOD_MULTIPLEwDELEG_CALLER_ID || self.callerID == VGOOD_SINGLEwDELEG_CALLER_ID) {			
            
			[self.request setValue:[BeintooNetwork getUserAgent] forHTTPHeaderField:@"User-Agent"];

			//NSLog(@"user agent: %@",[self.request valueForHTTPHeaderField:@"User-Agent"]);
		}
		[NSThread detachNewThreadSelector:@selector(retrievedWebPage) toTarget:self withObject:nil];
	}	
}

- (void)parsePageAtUrlWithPOST:(NSString *)URL withHeaders:(NSDictionary *)headers fromCaller:(int)caller{
	self.callerID = caller;
	if (![BeintooNetwork connectedToNetwork]) {
		[BeintooNetwork showNoConnectionAlert];
		return;
	}
	NSURL *serviceURL = [NSURL URLWithString:URL];
	self.request = [[[NSMutableURLRequest alloc] initWithURL:serviceURL cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:60.0] autorelease];
    [self.request setHTTPMethod:@"POST"];
	
	for (id theKey in headers) {
		[self.request setValue:[NSString stringWithFormat:@"%@",[headers objectForKey:theKey]] forHTTPHeaderField:theKey];
	}
	[self.request setValue:[Beintoo currentVersion] forHTTPHeaderField:@"X-BEINTOO-SDK-VERSION"];

	[NSThread detachNewThreadSelector:@selector(retrievedWebPage) toTarget:self withObject:nil];
}

- (void)parsePageAtUrlWithPOST:(NSString *)URL withHeaders:(NSDictionary *)headers withHTTPBody:(NSString *)httpBody fromCaller:(int)caller{
	self.callerID = caller;
	if (![BeintooNetwork connectedToNetwork]) {
		if (caller == MESSAGE_SET_READ_CALLER_ID || caller == PLAYER_SSCORE_OFFLINE_CALLER_ID ||
			ACHIEVEMENTS_SUBMIT_PERCENT_ID || ACHIEVEMENTS_SUBMIT_SCORE_ID ) {
			[self performSelectorOnMainThread:@selector(parsingEnd:) withObject:nil waitUntilDone:YES];
			return;
		}
		[BeintooNetwork showNoConnectionAlert];
		[self performSelectorOnMainThread:@selector(parsingEnd:) withObject:nil waitUntilDone:YES];
		return;
	}
	NSURL *serviceURL = [NSURL URLWithString:URL];
	self.request = [[[NSMutableURLRequest alloc] initWithURL:serviceURL cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:60.0] autorelease];
    [self.request setHTTPMethod:@"POST"];
	[self.request setHTTPBody:[httpBody dataUsingEncoding:NSUTF8StringEncoding]]; 
	for (id theKey in headers) {
		[self.request setValue:[NSString stringWithFormat:@"%@",[headers objectForKey:theKey]] forHTTPHeaderField:theKey];
	}
	[self.request setValue:[Beintoo currentVersion] forHTTPHeaderField:@"X-BEINTOO-SDK-VERSION"];

	[NSThread detachNewThreadSelector:@selector(retrievedWebPage) toTarget:self withObject:nil];
}

- (void)retrievedWebPage{
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	NSError         *requestError	= nil;
	NSURLResponse   *response		= nil;
	NSData		    *urlData;
	
	@try{ 
		urlData = [NSURLConnection sendSynchronousRequest:self.request returningResponse:&response error:&requestError];
		
		if (response == nil) {
			// Errors check
			if (requestError != nil) {
				NSLog(@"[Parser parsePageAtUrl] connection error: %@",requestError);
				self.webpage = @"{\"messageID\":-1,\"message\":\"Connection Timed-out\",\"kind\":\"error\"};";
			}
		}
		else {
			// Data correctly received, then converted from byte to string
			self.webpage = [[NSString alloc] initWithData:urlData encoding:NSUTF8StringEncoding];
            //NSLog(@"Web page %@",self.webpage);
		}
	}@catch (NSException *e) {
		NSLog(@"[Connection getPageAtUrl] getPage exception: %@",e);
	}
	
	//SBJsonParser *parser	= [[SBJsonParser alloc] init];
	SBJSON *parser	= [[SBJSON alloc] init];
	NSError *parseError		= nil;
	
	@try {
		result = [parser objectWithString:self.webpage error:&parseError];
	}
	@catch (NSException * e) {
		NSLog(@"[Connection getPageAtUrl] getPage exception: %@",e);
	}

	[parser release];	
	if (self.webpage!=nil) {
		[self.webpage release];
	}
	
	[self performSelectorOnMainThread:@selector(parsingEnd:) withObject:result waitUntilDone:YES];
	[pool release];	
}

- (void)parsingEnd:(NSDictionary *)theResult{
	[[self delegate]didFinishToParsewithResult:theResult forCaller:self.callerID];
}

- (id)blockerParsePageAtUrl:(NSString *)URL withHeaders:(NSDictionary *)headers{
	
	if (![BeintooNetwork connectedToNetwork]) {
		return nil;
	}
	
	NSURL *serviceURL = [NSURL URLWithString:URL];
	NSMutableURLRequest *request2 = [[[NSMutableURLRequest alloc] initWithURL:serviceURL cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:60.0] autorelease];
    [request2 setHTTPMethod:@"GET"];
	for (id theKey in headers) {
		[request2 setValue:[NSString stringWithFormat:@"%@",[headers objectForKey:theKey]] forHTTPHeaderField:theKey];
	}
	NSError         *requestError	= nil;
	NSURLResponse   *response		= nil;
	NSData		    *urlData;
	@try{ 
		urlData = [NSURLConnection sendSynchronousRequest:request2 returningResponse:&response error:&requestError];
		
		if (response == nil) {
			// Errors check
			if (requestError != nil) {
				NSLog(@"[Parser parsePageAtUrl] connection error: %@",requestError);
			}
		}
		else {
			// Data correctly received, then converted from byte to string
			self.webpage = [[NSString alloc] initWithData:urlData encoding:NSUTF8StringEncoding];
		}
	}@catch (NSException *e) {
		NSLog(@"[Connection getPageAtUrl] getPage exception: %@",e);
	}
	//SBJsonParser *parser	= [[SBJsonParser alloc] init];
	SBJSON *parser	= [[SBJSON alloc] init];
	NSError *parseError		= nil;
	result = [parser objectWithString:self.webpage error:&parseError];
	[parser release];
	return result;
}

- (NSString *)createJSONFromObject:(id)object{
	NSString *json;
	@try {
		SBJsonWriter *parserWriter	= [[SBJsonWriter alloc] init];
		json = [parserWriter stringWithObject:object];
		[parserWriter release];		
	}
	@catch (NSException * e) {
		NSLog(@"[CreateJson getPageAtUrl] exception: %@",e);
	}
	return json;
}

- (void)dealloc {
	[self.request release];
	[super dealloc];
}	

@end
