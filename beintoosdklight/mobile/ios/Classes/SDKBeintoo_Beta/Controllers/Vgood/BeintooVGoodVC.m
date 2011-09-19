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


#import "BeintooVGoodVC.h"
#import "Beintoo.h"

@implementation BeintooVGoodVC

@synthesize homeSender,generatedVGood, theVirtualGood,startingOptions;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
		theVirtualGood		= [[BVirtualGood alloc] init];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.title = @"Beintoo";
	
	registrationVC  = [BeintooVGoodShowVC alloc];
	[registrationVC setIsFromWallet:NO];
	beintooPlayer   = [[BeintooPlayer alloc] init];
	
	scrollView.contentSize	   = CGSizeMake(self.view.bounds.size.width, 400);
	scrollView.backgroundColor = [UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:1];
	
	endLabelTitle.text			= NSLocalizedStringFromTable(@"endDate",@"BeintooLocalizable",@"EndDate");
	whoAlsoConvertedTitle.text	= NSLocalizedStringFromTable(@"whoAlsoConverted",@"BeintooLocalizable",@"WhoAlso");
	titleLabel1.text			= NSLocalizedStringFromTable(@"congratulations",@"BeintooLocalizable",@"Congratulations!");
	
	[vgoodView setTopHeight:40];
	[vgoodView setBodyHeight:440];
	[vgoodView setIsScrollView:YES];
	[descView setGradientType:GRADIENT_HEADER];
	
	[whoAlsoConvertedTitle setHidden:YES];

	UIBarButtonItem *barCloseBtn = [[UIBarButtonItem alloc] initWithCustomView:[self closeButton]];
	[self.navigationItem setRightBarButtonItem:barCloseBtn animated:YES];
	[barCloseBtn release];
	
	vgoodTable.delegate		  = self;
	vgoodTable.rowHeight	  = 45;
	
	[getCouponButton setHighColor:[UIColor colorWithRed:156.0/255 green:168.0/255 blue:184.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(156, 2)/pow(255,2) green:pow(168, 2)/pow(255,2) blue:pow(184, 2)/pow(255,2) alpha:1]];
	[getCouponButton setMediumHighColor:[UIColor colorWithRed:116.0/255 green:135.0/255 blue:159.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(116, 2)/pow(255,2) green:pow(135, 2)/pow(255,2) blue:pow(159, 2)/pow(255,2) alpha:1]];
	[getCouponButton setMediumLowColor:[UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(108, 2)/pow(255,2) green:pow(128, 2)/pow(255,2) blue:pow(154, 2)/pow(255,2) alpha:1]];
    [getCouponButton setLowColor:[UIColor colorWithRed:89.0/255 green:112.0/255 blue:142.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(89, 2)/pow(255,2) green:pow(112, 2)/pow(255,2) blue:pow(142, 2)/pow(255,2) alpha:1]];
	[getCouponButton setTitle:NSLocalizedStringFromTable(@"getCoupon",@"BeintooLocalizable",@"Accept Coupon") forState:UIControlStateNormal];
	[getCouponButton setButtonTextSize:17];

	[sendAsAGiftButton setHighColor:[UIColor colorWithRed:156.0/255 green:168.0/255 blue:184.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(156, 2)/pow(255,2) green:pow(168, 2)/pow(255,2) blue:pow(184, 2)/pow(255,2) alpha:1]];
	[sendAsAGiftButton setMediumHighColor:[UIColor colorWithRed:116.0/255 green:135.0/255 blue:159.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(116, 2)/pow(255,2) green:pow(135, 2)/pow(255,2) blue:pow(159, 2)/pow(255,2) alpha:1]];
	[sendAsAGiftButton setMediumLowColor:[UIColor colorWithRed:108.0/255 green:128.0/255 blue:154.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(108, 2)/pow(255,2) green:pow(128, 2)/pow(255,2) blue:pow(154, 2)/pow(255,2) alpha:1]];
    [sendAsAGiftButton setLowColor:[UIColor colorWithRed:89.0/255 green:112.0/255 blue:142.0/255 alpha:1.0] andRollover:[UIColor colorWithRed:pow(89, 2)/pow(255,2) green:pow(112, 2)/pow(255,2) blue:pow(142, 2)/pow(255,2) alpha:1]];
	[sendAsAGiftButton setTitle:NSLocalizedStringFromTable(@"sendAsGift",@"BeintooLocalizable",@"Send as a gift") forState:UIControlStateNormal];
	[sendAsAGiftButton setButtonTextSize:17];
}

- (void)viewWillAppear:(BOOL)animated{
	[self setContentSizeForViewInPopover:CGSizeMake(320, 415)];
			
	vgoodNameText.text		= self.theVirtualGood.vGoodName;
	vgoodEndDateLbl.text	= self.theVirtualGood.vGoodEndDate;
	vgoodDescrTextView.text	= self.theVirtualGood.vGoodDescription;
	[vgoodImageView setImage:[UIImage imageWithData:self.theVirtualGood.vGoodImageData]];
	
	if ([self.theVirtualGood.whoAlsoConverted count]>0) {
		[whoAlsoConvertedTitle setHidden:NO];
	}
	
	/*
	 *  Check if the vgood is pushed from a multipleVgoodVC, if yes hides back button.
	 *
	NSArray *VCArray = self.navigationController.viewControllers;
	for (int i=0; i<[VCArray count]; i++) {
		if ([[VCArray objectAtIndex:i] isKindOfClass:[BeintooMultipleVgoodVC class]]) {
			[self.navigationItem setHidesBackButton:YES];
		}
	} */ // not needed anymore (at least now....)
	
	if (isThisVgoodConverted) {
		[self.navigationItem setHidesBackButton:YES];
	}
	
}

- (IBAction)getItReal{
	isThisVgoodConverted = YES;
	[registrationVC initWithNibName:@"BeintooVGoodShowVC" bundle:[NSBundle mainBundle] urlToOpen:self.theVirtualGood.getItRealURL];

	UIBarButtonItem *backButton = [[UIBarButtonItem alloc] initWithTitle: @"Back" style: UIBarButtonItemStyleBordered target:nil action:nil];
	[[self navigationItem] setBackBarButtonItem: backButton];
	[backButton release];
	
	[self.navigationController pushViewController:registrationVC animated:YES];	
}

#pragma mark -
#pragma mark Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [self.theVirtualGood.whoAlsoConverted count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
   	int _gradientType = (indexPath.row % 2) ? GRADIENT_CELL_HEAD : GRADIENT_CELL_BODY;
	
	BTableViewCell *cell = (BTableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil || TRUE) {
        cell = [[[BTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier andGradientType:_gradientType] autorelease];
    }
	
	cell.textLabel.text =  [[self.theVirtualGood.whoAlsoConverted objectAtIndex:indexPath.row] objectForKey:@"nickname"];
	cell.textLabel.font = [UIFont systemFontOfSize:13];
	NSString *imgString = [[self.theVirtualGood.whoAlsoConverted objectAtIndex:indexPath.row] objectForKey:@"usersmallimg"];
	cell.imageView.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:imgString]]];
    return cell;
}

- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath{
	return nil;
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
	[Beintoo dismissPrize];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
    [super viewDidUnload];
}

- (void)viewDidDisappear:(BOOL)animated{
}

- (void)dealloc {
    [super dealloc];
	[theVirtualGood release];
	[registrationVC release];
	[beintooPlayer release];
	[prizeBanner release];
	[generatedVGood release];
}


@end
