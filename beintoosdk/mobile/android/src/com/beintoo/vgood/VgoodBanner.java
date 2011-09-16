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
package com.beintoo.vgood;


import com.beintoo.beintoosdk.BeintooVgood;
import com.beintoo.beintoosdkutility.BeintooAnimations;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.VgoodChooseOne;
import com.google.beintoogson.Gson;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class VgoodBanner implements OnClickListener {
	
	/*
	 * list of vgoods. It could be only a single vgood or maximum of 3 vgoods
	 */	
	private VgoodChooseOne vgood;
	
	/*
	 * current context
	 */
	private Context ctx;
	
	/*
	 * ViewFlipper used for the animation 
	 */
	private ViewFlipper vf;
	
	/*
	 * The developers LinearLayout where the vgood banner will be displayed 
	 */
	private LinearLayout container;
	
	/*
	 * True if the user has clicked on the banner.
	 * Used to autoassign a vgood if the user doesn't click on the banner. 
	 */
	private boolean hasClicked = false;
	
	public VgoodBanner(Context context, LinearLayout c, VgoodChooseOne v) {
		ctx = context;
		vgood = v;
		container = c;
		try {
			vf = new ViewFlipper(context);
			LinearLayout empty = new LinearLayout(context);
			vf.addView(empty);
	
			LinearLayout bg = new LinearLayout(context);
			LinearLayout.LayoutParams bgparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			bg.setPadding(toDip(1), toDip(1), toDip(1), toDip(1));
			bg.setBackgroundColor(Color.argb(180, 150, 150, 150));
			bg.setOrientation(LinearLayout.VERTICAL);
			bg.setLayoutParams(bgparams);
	
			LinearLayout top = new LinearLayout(context);
			LinearLayout.LayoutParams topparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			top.setPadding(toDip(5), toDip(5), toDip(5), toDip(5));
			top.setOrientation(LinearLayout.HORIZONTAL);
			top.setLayoutParams(topparams);
			top.setBackgroundColor(Color.argb(190, 30, 25, 25));
			top.setGravity(Gravity.CENTER);
	
			bg.addView(top);
	
			final LoaderImageView image = new LoaderImageView(context,
					v.getVgoods().get(0).getImageUrl(), toDip(70), toDip(70));
			image.setPadding(0, 0, toDip(10), 0);
			TextView text = new TextView(context);
			String message = vgood.getVgoods().get(0).getRewardText();
			if(message != null)
				text.setText(message);
			else
				text.setText(context.getString(context.getResources().getIdentifier("vgoodmessagebanner", "string", context.getPackageName())));
			text.setTextColor(Color.WHITE);
			text.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
			top.addView(image);
			top.addView(text);
			
			vf.addView(bg);
			vf.setForegroundGravity(Gravity.CENTER);
			vf.setLayoutParams(bgparams);
			container.setOnClickListener(this);
			
		}catch(Exception e){}	
	}
	
	public void show(){
		Handler UIhandler = new Handler();
		UIhandler.postDelayed(inRunnable, 200);
		UIhandler.postDelayed(outRunnable, 12000);
		UIhandler.postDelayed(goneRunnable, 14000);
	}

	private Runnable inRunnable = new Runnable() {
		public void run() {
			// SETTING UP CONTAINER			
			container.setBackgroundColor(Color.TRANSPARENT);
			container.setGravity(Gravity.CENTER);
			container.addView(vf);		
			container.setVisibility(LinearLayout.VISIBLE);
			
			vf.setInAnimation(BeintooAnimations.inFromBottomAnimation());
			vf.setOutAnimation(BeintooAnimations.outFromTopAnimation());
			vf.showPrevious();
		}
	};

	private Runnable outRunnable = new Runnable() {
		public void run() {
			vf.showPrevious();
		}
	};
	
	private Runnable goneRunnable = new Runnable() {
		public void run() {	
			try {
			container.setVisibility(LinearLayout.GONE);
			container.removeAllViews();
			if((hasClicked == false) && (vgood.getVgoods().size() > 1)){ // AUTO ASSIGN A VGOOD
				new Thread(new Runnable(){     					
	        		public void run(){	
	    				try {
	    					Player loggedUser = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", ctx), Player.class);
	    					BeintooVgood bv = new BeintooVgood();	    					
	    					bv.acceptVgood(vgood.getVgoods().get(0).getId(), loggedUser.getUser().getId(), null);
	    				}catch(Exception e){e.printStackTrace();}
	        		}	
	        	}).start();	
			}
			}catch(Exception e){}
		}
	};

	private int toDip(int px) {
		double ratio = (ctx.getApplicationContext().getResources()
				.getDisplayMetrics().densityDpi / 160d);
		return (int) (ratio * px);
	}

	public void onClick(View v) {	
		hasClicked = true;
		if(vgood.getVgoods().size() > 1){
			VGoodGetList getVgoodList = new VGoodGetList(ctx, vgood);
    		getVgoodList.show();
		}else {
			VGoodGetDialog m = new VGoodGetDialog(ctx, vgood.getVgoods().get(0));
			m.show();
		}
	}
}
