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

#import "BeintooVC.h"
#import <QuartzCore/QuartzCore.h>

@implementation BeintooVC

@synthesize beintooPlayer,loginNavController,
					retrievedPlayersArray,popOverController,loginPopoverController,featuresArray;

-(id)init {
	if (self != nil) {		
	}
	return self;
}

#pragma mark -
#pragma mark BeintooInitialization

+ (UIButton *)closeButton{
	UIImage *closeImg = [UIImage imageNamed:@"bar_close.png"];
	UIButton *closeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
	[closeBtn setImage:closeImg forState:UIControlStateNormal];
	closeBtn.frame = CGRectMake(0,0, closeImg.size.width+7, closeImg.size.height);
	[closeBtn addTarget:self action:@selector(closeBeintoo) forControlEvents:UIControlEventTouchUpInside];
	return closeBtn;
}

+ (void)closeBeintoo{
	[Beintoo dismissBeintoo];
}

#pragma mark -
#pragma mark UIViewController

- (void)viewDidLoad {
	[super viewDidLoad];
	
	// ----------- User service initialization ---------------
	_user = [[BeintooUser alloc]init];
	_user.delegate = self;	
	beintooPlayer = [[BeintooPlayer alloc]init];
	beintooPlayer.delegate = self;	
	
	homeNavController = [Beintoo getMainNavigationController];
	
	
	
	pressTheButtonLabel.text = NSLocalizedStringFromTable(@"pressTheButton",@"BeintooLocalizable",@"Press the button below to try Beintoo");
	titleLabel1.text = NSLocalizedStringFromTable(@"beintootransforms",@"BeintooLocalizable",@"Beintoo transforms");
	titleLabel2.text = NSLocalizedStringFromTable(@"yourpassion",@"BeintooLocalizable",@"your Passion for Apps");
	titleLabel3.text = NSLocalizedStringFromTable(@"realreward",@"BeintooLocalizable",@"in Real Rewards.");
	
	[homeView setTopHeight:33.0f];
	[homeView setBodyHeight:444.0f];	
	homeTable.rowHeight = 80;
	homeTable.delegate = self;
	homeTable.dataSource = self;
	
	[tryBeintooView setTopHeight:90.0f];
	[tryBeintooView setBodyHeight:330.0f];
	[self.view addSubview:tryBeintooView];
	[tryBeintooView setHidden:YES];
	tryBeintooView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
	
	UIBarButtonItem *barCloseBtn = [[UIBarButtonItem alloc] initWithCustomView:[BeintooVC closeButton]];
	[self.navigationItem setRightBarButtonItem:barCloseBtn animated:YES];
	[barCloseBtn release];
		
	[button1 setHighColor:[UIColor colorWithRed:156.0/255 green:168.0/255 blue:184.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(156, 2)/pow(255,2) green:pow(168, 2)/pow(255,2) blue:pow(184, 2)/pow(255,2) alpha:1]];
	[button1 setMediumHighColor:[UIColor colorWithRed:116.0/255 green:135.0/255 blue:159.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(116, 2)/pow(255,2) green:pow(135, 2)/pow(255,2) blue:pow(159, 2)/pow(255,2) alpha:1]];
	[button1 setMediumLowColor:[UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(108, 2)/pow(255,2) green:pow(128, 2)/pow(255,2) blue:pow(154, 2)/pow(255,2) alpha:1]];
    [button1 setLowColor:[UIColor colorWithRed:89.0/255 green:112.0/255 blue:142.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(89, 2)/pow(255,2) green:pow(112, 2)/pow(255,2) blue:pow(142, 2)/pow(255,2) alpha:1]];
	[button1 setTitle:NSLocalizedStringFromTable(@"tryBeintooBtn",@"BeintooLocalizable",@"Try beintoo") forState:UIControlStateNormal];
	[button1 setButtonTextSize:20];
	
	[button2 setHighColor:[UIColor colorWithRed:156.0/255 green:168.0/255 blue:184.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(156, 2)/pow(255,2) green:pow(168, 2)/pow(255,2) blue:pow(184, 2)/pow(255,2) alpha:1]];
	[button2 setMediumHighColor:[UIColor colorWithRed:116.0/255 green:135.0/255 blue:159.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(116, 2)/pow(255,2) green:pow(135, 2)/pow(255,2) blue:pow(159, 2)/pow(255,2) alpha:1]];
	[button2 setMediumLowColor:[UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(108, 2)/pow(255,2) green:pow(128, 2)/pow(255,2) blue:pow(154, 2)/pow(255,2) alpha:1]];
    [button2 setLowColor:[UIColor colorWithRed:89.0/255 green:112.0/255 blue:142.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(89, 2)/pow(255,2) green:pow(112, 2)/pow(255,2) blue:pow(142, 2)/pow(255,2) alpha:1]];
	[button2 setTitle:NSLocalizedStringFromTable(@"noThanks",@"BeintooLocalizable",@"no Thanks") forState:UIControlStateNormal];
	[button2 setButtonTextSize:20];
	
	unreadMessagesView = [[BView alloc] initWithFrame:CGRectMake(0, 33, self.view.bounds.size.width, 30)];
	unreadMessagesView.autoresizingMask = UIViewAutoresizingFlexibleWidth;
	[unreadMessagesView setIsScrollView:YES];
	[unreadMessagesView setTopHeight:30];
	[unreadMessagesView setBodyHeight:0];
	[self.view addSubview:unreadMessagesView];
	[self.view sendSubviewToBack:unreadMessagesView];
	
	unreadMessagesLabel						= [[UILabel alloc] initWithFrame:CGRectMake(11, 4, self.view.bounds.size.width, 20)];
	unreadMessagesLabel.autoresizingMask	= UIViewAutoresizingFlexibleWidth;
	unreadMessagesLabel.backgroundColor		= [UIColor clearColor];
	unreadMessagesLabel.font				= [UIFont systemFontOfSize:14];
	unreadMessagesLabel.textColor			= [UIColor colorWithWhite:0 alpha:0.7];
	[unreadMessagesView addSubview:unreadMessagesLabel];
	
	unreadMessagesButton = [UIButton buttonWithType:UIButtonTypeCustom];
	unreadMessagesButton.frame = CGRectMake(11, 4, self.view.bounds.size.width, 20);
	unreadMessagesButton.autoresizingMask = UIViewAutoresizingFlexibleWidth;
	unreadMessagesButton.backgroundColor   	  = [UIColor clearColor];
	[unreadMessagesButton addTarget:self action:@selector(openMessages) forControlEvents:UIControlEventTouchUpInside];
	
	[unreadMessagesView addSubview:unreadMessagesButton];
	homeTableAnimationPerformed = NO;
}

- (void)viewWillAppear:(BOOL)animated{
	
	[self setContentSizeForViewInPopover:CGSizeMake(320, 415)];
	
	/*
	 * BeintooLogo
	 */
	
	int appOrientation = [Beintoo appOrientation];
	
	UIImageView *logo;
	if(appOrientation == UIInterfaceOrientationLandscapeLeft || appOrientation == UIInterfaceOrientationLandscapeRight
	   || appOrientation == 0)
		logo = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"bar_logo_34.png"]];
	else 
		logo = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"bar_logo.png"]];

	
	self.navigationItem.titleView = logo;
	[logo release];
	
	[tryBeintooView setNeedsDisplay];
	tryBeintooView.userInteractionEnabled = YES;
	if (![Beintoo isUserLogged]) {
		tryBeintooView.userInteractionEnabled = YES;
		[tryBeintooView setHidden:NO];
	}
	else {
		[tryBeintooView setHidden:YES];		
		@try {
			NSDictionary *currentUser = [Beintoo getUserIfLogged];

			userNick.text		= [currentUser objectForKey:@"nickname"];
			bedollars.text		= [NSString stringWithFormat:@"%@ Bedollars",[currentUser objectForKey:@"bedollars"]];
			unreadMessagesLabel.text	= [NSString stringWithFormat:@"%@ %@",[currentUser objectForKey:@"unreadMessages"],NSLocalizedStringFromTable(@"newMessages",@"BeintooLocalizable",@"Try beintoo")];								   
		}
		@catch (NSException * e) {
		}
		
		NSString *guid = [Beintoo getPlayerID];
		if (guid!=nil) {
			[beintooPlayer getPlayerByGUID:guid];
		}
		[homeTable deselectRowAtIndexPath:[homeTable indexPathForSelectedRow] animated:NO];
	}
}

#pragma mark -
#pragma mark Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.featuresArray count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	NSString *CellIdentifier = [NSString stringWithFormat:@"Cell%d",indexPath.row];

	int _gradientType = (indexPath.row % 2) ? GRADIENT_CELL_HEAD : GRADIENT_CELL_BODY;
	
	BTableViewCell *cell = (BTableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[BTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier andGradientType:_gradientType] autorelease];
    }
	@try {
		cell.textLabel.text				= [[self.featuresArray objectAtIndex:indexPath.row] objectForKey:@"featureName"];
		cell.textLabel.font				= [UIFont systemFontOfSize:18];
		cell.detailTextLabel.text		= [[self.featuresArray objectAtIndex:indexPath.row] objectForKey:@"featureDesc"];
		cell.imageView.image			= [UIImage imageNamed:[[self.featuresArray objectAtIndex:indexPath.row] objectForKey:@"featureImg"]];
	}
	@catch (NSException * e){
		[beintooPlayer logException:[NSString stringWithFormat:@"STACK: %@\n\nException: %@",[NSThread callStackSymbols],e]];
	}
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	[[Beintoo getMainNavigationController] pushViewController:[[self.featuresArray objectAtIndex:indexPath.row] objectForKey:@"featureVC"] animated:YES];
}

#pragma mark -
#pragma mark IBActions

- (IBAction)tryBeintoo{	
	@try {
		if ([BeintooNetwork connectedToNetwork]) {
			tryBeintooView.userInteractionEnabled = NO;
			[BLoadingView startActivity:tryBeintooView];
			[self.loginNavController popToRootViewControllerAnimated:NO];
		}
		[_user getUserByUDID];
	}
	@catch (NSException * e) {
	}
}

-(IBAction)close{
	[Beintoo dismissBeintoo];
}

#pragma mark -
#pragma mark player delegate

- (void)playerDidLogin:(BeintooPlayer *)player{
}

- (void)didGetUserByUDID:(NSMutableArray *)result{
}

- (void)player:(BeintooPlayer *)player getPlayerByGUID:(NSDictionary *)result{
	
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
    [super viewDidUnload];
}

- (void)viewWillDisappear:(BOOL)animated {
    @try {
		[BLoadingView stopActivity];
	}
	@catch (NSException * e) {
	}
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return NO;
}

- (void)dealloc {
	[beintooPlayer release];
	[_user release];
	[featuresArray release];
	[unreadMessagesLabel release];
	[super dealloc];
}

@end
