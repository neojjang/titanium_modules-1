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

#import "BImageDownload.h"

@interface BImageDownload()
@property (nonatomic, retain) NSMutableData *receivedData;
@end


@implementation BImageDownload

@synthesize urlString,image,delegate,receivedData;

#pragma mark -
- (UIImage *)image{
    if (image == nil && !downloading){

        if (urlString != nil && [urlString length] > 0){
            NSURLRequest *req = [[NSURLRequest alloc] initWithURL:[NSURL URLWithString:self.urlString]];
            NSURLConnection *con = [[NSURLConnection alloc] initWithRequest:req delegate:self startImmediately:NO];
            [con scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
            [con start];
            
            if (con) {
                NSMutableData *data = [[NSMutableData alloc] init];
                self.receivedData=data;
                [data release];
            }
            else {
                NSError *error = [NSError errorWithDomain:BeintooDownloadErrorDomain 
                                                     code:BeintooDownloadErrorNoConnection 
                                                 userInfo:nil];
                if ([self.delegate respondsToSelector:@selector(download:didFailWithError:)])
                    [delegate download:self didFailWithError:error];
            }   
            [req release];
            downloading = YES;
        }
    }
    return image;
}

- (NSString *)filename{
    return [urlString lastPathComponent];
}

#pragma mark -
#pragma mark NSURLConnection Callbacks
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response{
    [receivedData setLength:0];
}
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data{
    [receivedData appendData:data];
}
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    [connection release];
    if ([delegate respondsToSelector:@selector(download:didFailWithError:)])
        [delegate download:self didFailWithError:error];
}
- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    self.image = [UIImage imageWithData:receivedData];
    if ([delegate respondsToSelector:@selector(downloadDidFinishDownloading:)])
        [delegate downloadDidFinishDownloading:self];
    
    [connection release];
    self.receivedData = nil;
}

#pragma mark -
#pragma mark Comparison
- (NSComparisonResult)compare:(id)theOther{
    BImageDownload *other = (BImageDownload *)theOther;
    return [self.filename compare:other.filename];
}

- (void)dealloc {
    [urlString release];
    [image release];
    delegate = nil;
    [receivedData release];
    [super dealloc];
}

@end