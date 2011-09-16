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
package com.beintoo.beintoosdkui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.Button;

public class BeButton extends Button{
	Context btContext;
	public BeButton(Context context) {		
		super(context);
		btContext = context;
	}
	public StateListDrawable setPressedBg(Integer p1,Integer p2,Integer p3) {
	    StateListDrawable bg = new StateListDrawable();
	    Drawable normal = btContext.getResources().getDrawable(p1);
	    Drawable selected = btContext.getResources().getDrawable(p2);
	    Drawable pressed = btContext.getResources().getDrawable(p3);

	    bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
	    bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
	    bg.addState(View.ENABLED_STATE_SET, normal);
	    bg.addState(View.FOCUSED_STATE_SET, selected);
	    bg.addState(View.EMPTY_STATE_SET, normal);
	    return bg;
	}
	
	public StateListDrawable setPressedBackg(Drawable p1,Drawable p2,Drawable p3) {
	    StateListDrawable bg = new StateListDrawable();
	    Drawable normal = p1;
	    Drawable selected = p2;
	    Drawable pressed = p3;

	    bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
	    bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
	    bg.addState(View.ENABLED_STATE_SET, normal);
	    bg.addState(View.FOCUSED_STATE_SET, selected);
	    bg.addState(View.EMPTY_STATE_SET, normal);
	    return bg;
	}
}
