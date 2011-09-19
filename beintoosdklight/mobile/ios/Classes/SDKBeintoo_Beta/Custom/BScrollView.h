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

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#define GRADIENT_BODY			1
#define GRADIENT_HEADER			2
#define GRADIENT_BUTTONS		3
#define GRADIENT_FOOTER			4
#define GRADIENT_CELL_HEAD		5
#define GRADIENT_CELL_BODY		6
#define GRADIENT_CELL_SELECTED	7
#define GRADIENT_TOOLBAR		8

@interface BScrollView : UIScrollView {

	int gradientType;
}

-(void)setGradientType:(int)_gradientType;

@end
