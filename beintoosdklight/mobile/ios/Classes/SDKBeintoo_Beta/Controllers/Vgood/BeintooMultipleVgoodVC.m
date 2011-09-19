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


#import "BeintooMultipleVgoodVC.h"
#import "Beintoo.h"

@implementation BeintooMultipleVgoodVC

@synthesize multipleVgoodTable, vgoodArrayList, vgoodImages, selectedVgood, vGood, startingOptions;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil andOptions:(NSDictionary *)options{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
		self.startingOptions	= options;
		callerPopoverController = [options objectForKey:@"popoverController"];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.title			 	= @"Beintoo";
	selectVgoodLabel.text	= NSLocalizedStringFromTable(@"multipleVgoodsLabel",@"BeintooLocalizable",@"Select A Friend");
	
	singleVgoodVC	= [BeintooVGoodVC alloc];
	vgoodShowVC		= [BeintooVGoodShowVC alloc];


	[multipleVgoodsView setTopHeight:50];
	[multipleVgoodsView setBodyHeight:377];
	
	self.multipleVgoodTable.delegate	= self;
	self.multipleVgoodTable.rowHeight	= 90.0;	
	
	self.vgoodArrayList = [[NSMutableArray alloc] init];
	self.vgoodImages    = [[NSMutableArray alloc] init];
	self.selectedVgood  = [[NSDictionary alloc] init];
	
	//self.vGood.popoverVgoodController = callerPopoverController;
	_player	   = [[BeintooPlayer alloc] init];
	
	UIBarButtonItem *barCloseBtn = [[UIBarButtonItem alloc] initWithCustomView:[self closeButton]];
	[self.navigationItem setRightBarButtonItem:barCloseBtn animated:YES];
	[barCloseBtn release];
}

- (void)viewWillAppear:(BOOL)animated{
		
	[self setContentSizeForViewInPopover:CGSizeMake(320, 415)];
	[self.multipleVgoodTable deselectRowAtIndexPath:[self.multipleVgoodTable indexPathForSelectedRow] animated:YES];

	self.vgoodArrayList = [self.startingOptions objectForKey:@"vgoodArray"];
	
	[BLoadingView startActivity:self.view];
	[NSThread detachNewThreadSelector:@selector(loadImages) toTarget:self withObject:nil];	
}

-(void)loadImages{
	
	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
	@try {
		int i;
		for (i=0; i<[self.vgoodArrayList count]; i++) {
			NSURL *imageURL = [NSURL URLWithString:[[self.vgoodArrayList objectAtIndex:i] objectForKey:@"imageSmallUrl"]];
			[self.vgoodImages insertObject:[NSData dataWithContentsOfURL:imageURL] atIndex:i];
		}
	}
	@catch (NSException * e) {
		BLOG(@"EXCEPTION :%@",e);
	}
	[BLoadingView stopActivity];	
	[self.multipleVgoodTable reloadData];
	[pool release];
}

#pragma mark -
#pragma mark Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [self.vgoodArrayList count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
   	int _gradientType = (indexPath.row % 2) ? GRADIENT_CELL_HEAD : GRADIENT_CELL_BODY;
	
	BTableViewCell *cell = (BTableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil || TRUE) {
        cell = [[[BTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier andGradientType:_gradientType] autorelease];
    }
	@try {
		UILabel *textLabel = [[UILabel alloc] initWithFrame:CGRectMake(85, 6, 230, 77)];
		textLabel.numberOfLines = 4;
		textLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;
		textLabel.text = [[self.vgoodArrayList objectAtIndex:indexPath.row] objectForKey:@"name"];
		textLabel.font = [UIFont systemFontOfSize:12];
		textLabel.backgroundColor = [UIColor clearColor];
		
		UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(8, 8, 70, 70)];
		imageView.contentMode = UIViewContentModeScaleAspectFit;
		imageView.backgroundColor = [UIColor clearColor];
		[imageView setImage:[UIImage imageWithData:[self.vgoodImages objectAtIndex:indexPath.row]]];
		
		[cell addSubview:textLabel];
		[cell addSubview:imageView];
		
		[textLabel release];
		[imageView release];
	}
	@catch (NSException * e) {
	}
	
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	@try {
		
		self.selectedVgood = [self.vgoodArrayList objectAtIndex:indexPath.row];
		[singleVgoodVC initWithNibName:@"BeintooVGoodVC" bundle:[NSBundle mainBundle]];
		[singleVgoodVC.theVirtualGood setVgoodContent:self.selectedVgood];
		
		//[BLoadingView startActivity:self.view];
		
		UIActionSheet *as =  [[UIActionSheet alloc] initWithTitle:nil 
														 delegate:self 
												cancelButtonTitle:NSLocalizedStringFromTable(@"cancel",@"BeintooLocalizable",@"") 
										   destructiveButtonTitle:nil
												otherButtonTitles:NSLocalizedStringFromTable(@"getCoupon",@"BeintooLocalizable",@""), NSLocalizedStringFromTable(@"details",@"BeintooLocalizable",@""), nil ];		
		
		as.actionSheetStyle = UIActionSheetStyleDefault;
		as.tag = indexPath.row;
		[as showInView:self.view];
		[as release];
		
	}
	@catch (NSException * e) {
		[_player logException:[NSString stringWithFormat:@"STACK: %@\n\nException: %@",[NSThread callStackSymbols],e]];
	}
}

#pragma mark -
#pragma mark UIActionSheetDelegate

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
	[multipleVgoodTable deselectRowAtIndexPath:[multipleVgoodTable indexPathForSelectedRow] animated:YES];
	if (buttonIndex == 0) {
		NSLog(@"getCoupon");
		[self getItReal];
	}
	else if (buttonIndex == 1){
		[self.navigationController pushViewController:singleVgoodVC animated:YES];	
		NSLog(@"details");
	}
}

- (void)getItReal{
	[vgoodShowVC initWithNibName:@"BeintooVGoodShowVC" bundle:[NSBundle mainBundle] urlToOpen:[self.selectedVgood objectForKey:@"getRealURL"]];
		
	[self.navigationController pushViewController:vgoodShowVC animated:YES];		
}

- (void)didAcceptVgood{
}

-(void)closeBeintoo{
	@synchronized(self){
		BeintooVgood *vgoodService = [Beintoo beintooVgoodService];
		[vgoodService acceptGoodWithId:[self.selectedVgood objectForKey:@"id"]];
	}
	[Beintoo dismissPrize];
}

- (UIButton *)closeButton{
	UIImage *closeImg = [UIImage imageNamed:@"bar_close.png"];
	UIButton *closeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
	[closeBtn setImage:closeImg forState:UIControlStateNormal];
	closeBtn.frame = CGRectMake(0,0, closeImg.size.width+7, closeImg.size.height);
	[closeBtn addTarget:self action:@selector(closeBeintoo) forControlEvents:UIControlEventTouchUpInside];
	return closeBtn;
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

- (void)dealloc {
    [super dealloc];
	[self.vGood release];
	[self.vgoodArrayList release];
	[self.vgoodImages release];	
	[_player release];
	[singleVgoodVC release];	
	[self.selectedVgood release];
	[vgoodShowVC release];
}

@end
