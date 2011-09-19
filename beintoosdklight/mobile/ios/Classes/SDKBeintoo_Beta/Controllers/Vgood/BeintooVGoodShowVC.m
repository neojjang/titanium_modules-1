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

#import "BeintooVGoodShowVC.h"
#import "Beintoo.h"


@implementation BeintooVGoodShowVC

@synthesize urlToOpen;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil urlToOpen:(NSString *)URL {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
		self.urlToOpen = URL;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.title = @"Beintoo";
	
	if (self.navigationItem != nil) {
		UIBarButtonItem *barCloseBtn = [[UIBarButtonItem alloc] initWithCustomView:[self closeButton]];
		[self.navigationItem setRightBarButtonItem:barCloseBtn animated:YES];
		[barCloseBtn release];
	}
	
	loadingIndicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
	loadingIndicator.hidesWhenStopped = YES;
	[self.view addSubview:loadingIndicator];
		
	vGoodWebView.delegate = self;
	vGoodWebView.scalesPageToFit = YES;
}

- (void)viewWillAppear:(BOOL)animated{
	[loadingIndicator stopAnimating];
	
	[self setContentSizeForViewInPopover:CGSizeMake(320, 415)];
	
	loadingIndicator.center = CGPointMake((self.view.bounds.size.width/2)-5, (self.view.bounds.size.height/2)-30);
	
	NSHTTPCookie *cookie;
	NSHTTPCookieStorage *storage = [NSHTTPCookieStorage sharedHTTPCookieStorage];
	for (cookie in [storage cookies]) {
		[storage deleteCookie:cookie];
	}
	
	/*
	 *  Check if the vgood is pushed from a multipleVgoodVC, if yes hides back button.
	 */
	 NSArray *VCArray = self.navigationController.viewControllers;
	 for (int i=0; i<[VCArray count]; i++) {
	 if ([[VCArray objectAtIndex:i] isKindOfClass:[BeintooMultipleVgoodVC class]]) {
	 [self.navigationItem setHidesBackButton:YES];
		}
	 }
	
	[vGoodWebView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:urlToOpen]]];
}

#pragma mark -
#pragma mark webViewDelegates

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)req navigationType:(UIWebViewNavigationType)navigationType {
    NSMutableURLRequest *request = (NSMutableURLRequest *)req;
	
	didOpenTheRecommendation = NO;
	
    NSURL *url = request.URL;
	//NSLog(@"URL %@",url);
	if (![url.scheme isEqual:@"http"] && ![url.scheme isEqual:@"https"]) {
		// ******** Remember that this will NOT work on the simulator ******* //
		if ([[UIApplication sharedApplication]canOpenURL:url]) {
			[[UIApplication sharedApplication]openURL:url];
			[loadingIndicator stopAnimating];
			didOpenTheRecommendation = YES;
			if (didOpenTheRecommendation) {
				[Beintoo dismissRecommendation];
			}
			
			return NO;
		}
	}
    return YES; 
}

- (void)webViewDidStartLoad:(UIWebView *)theWebView{
	[loadingIndicator startAnimating];
}
- (void)webViewDidFinishLoad:(UIWebView *)theWebView{
	[loadingIndicator stopAnimating];
}

- (void)webView:(UIWebView *)wv didFailLoadWithError:(NSError *)error {
    // Ignore NSURLErrorDomain error -999.
    if (error.code == NSURLErrorCancelled) return;
	
    // Ignore "Fame Load Interrupted" errors. Seen after app store links.
    if (error.code == 102 && [error.domain isEqual:@"WebKitErrorDomain"]) return;
	
	[loadingIndicator stopAnimating];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}

- (void)setIsFromWallet:(BOOL)value{
	isFromWallet = value;
}

- (UIButton *)closeButton{
	UIImage *closeImg = [UIImage imageNamed:@"bar_close.png"];
	UIButton *closeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
	[closeBtn setImage:closeImg forState:UIControlStateNormal];
	closeBtn.frame = CGRectMake(0,0, closeImg.size.width+7, closeImg.size.height);
	[closeBtn addTarget:self action:@selector(closeBeintoo) forControlEvents:UIControlEventTouchUpInside];
	return closeBtn;
}

-(void)closeBeintoo{
	if (isFromWallet) { 
		[Beintoo dismissBeintoo];
	}else {
		[Beintoo dismissPrize];
	}
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return NO;
}

- (void)viewWillDisappear:(BOOL)animated {
	[vGoodWebView loadHTMLString:@"<html><head></head><body></body></html>" baseURL:nil];
}

- (void)setRecommendationPopoverController:(UIPopoverController *)_recommPopover{
	recommendPopoverController = _recommPopover;
}

- (void)viewDidDisappear:(BOOL)animated{
}

- (void)viewDidUnload {
    [super viewDidUnload];
}

- (void)dealloc {
    [super dealloc];
}


@end
