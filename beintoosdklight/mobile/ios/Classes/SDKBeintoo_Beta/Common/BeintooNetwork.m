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

#import "BeintooNetwork.h"


@implementation BeintooNetwork

#pragma mark ConnectionAvailability check

+ (void)showNoConnectionAlert{
	
	UIAlertView *noConnectionAlert = [[UIAlertView alloc] initWithTitle:@"Beintoo" message:NSLocalizedStringFromTable(@"connectionError",@"BeintooLocalizable",@"no Thanks") delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
	[noConnectionAlert show];
	[noConnectionAlert release];	
}

+ (BOOL)connectedToNetwork{
	
	struct sockaddr_in zeroAddress;
	bzero(&zeroAddress, sizeof(zeroAddress));
	zeroAddress.sin_len = sizeof(zeroAddress);
	zeroAddress.sin_family = AF_INET;
	
	SCNetworkReachabilityRef defaultRouteReachability = SCNetworkReachabilityCreateWithAddress(NULL, (struct sockaddr *)&zeroAddress);
	SCNetworkReachabilityFlags flags;
	
	BOOL didRetrieveFlags = SCNetworkReachabilityGetFlags(defaultRouteReachability, &flags);
	CFRelease(defaultRouteReachability);
	if(!didRetrieveFlags){
		return NO;
	}
	
	BOOL isReachable = flags & kSCNetworkFlagsReachable;
	BOOL needsConnection = flags & kSCNetworkFlagsConnectionRequired;
	return (isReachable && !needsConnection) ? YES : NO;
}

+ (NSString *)convertToCurrentDate:(NSString *)date{
	
	NSDateFormatter *df = [[NSDateFormatter alloc] init];
	[df setDateFormat:@"d-MMM-y HH:mm:ss"];
	NSLocale *gbLocale = [[NSLocale alloc] initWithLocaleIdentifier:@"en_GB"];
	[df setLocale:gbLocale];
	[df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
	
	NSDate *theDate = [df dateFromString:date];  

	NSDateFormatter *df2 = [[NSDateFormatter alloc] init];
	[df2 setDateFormat:@"d-MMM-y HH:mm:ss"];
	[df2 setLocale:[NSLocale currentLocale]];
	[df2 setTimeZone:[NSTimeZone localTimeZone]];

	NSString *timeStamp = [df2 stringFromDate:theDate];
	[df release];
	[df2 release];
	[gbLocale release];
	return timeStamp;
}	

+ (NSString *)getUserAgent{
	NSString *userAgent = [[NSUserDefaults standardUserDefaults]objectForKey:@"userAgent"];
	if (userAgent!=nil) {
		return userAgent;
	}
	else{
        UIWebView* webView = [[UIWebView alloc] initWithFrame:CGRectZero];
        userAgent = [webView stringByEvaluatingJavaScriptFromString:@"navigator.userAgent"];
        [[NSUserDefaults standardUserDefaults] setObject:userAgent forKey:@"userAgent"];
        [webView release];		
        return userAgent;
	}
}

+ (NSData *)getSynchImageWithUA:(NSString *)url{
	NSMutableURLRequest *theRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
	[theRequest setValue:[BeintooNetwork getUserAgent] forHTTPHeaderField:@"User-Agent"];
	[theRequest setHTTPMethod:@"GET"];

	NSData *imgData = [NSURLConnection sendSynchronousRequest:theRequest returningResponse: nil error:nil];	
	
	return imgData;
}

- (void)dealloc {
    [super dealloc];
}

@end
