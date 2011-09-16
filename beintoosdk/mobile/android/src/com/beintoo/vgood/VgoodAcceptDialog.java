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
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.VgoodChooseOne;
import com.google.beintoogson.Gson;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class VgoodAcceptDialog extends Dialog implements OnClickListener{
	Context ctx;
	VgoodChooseOne vgood;
	public VgoodAcceptDialog(Context context, VgoodChooseOne v) {
		super(context, context.getResources().getIdentifier("ThemeBeintooVgood", "style", context.getPackageName()));
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		ctx = context;
		vgood = v;
		
		try {
			LinearLayout main = new LinearLayout(context);
			LinearLayout.LayoutParams mainparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			main.setPadding(toDip(5), 0, toDip(5), 0);		
			main.setBackgroundColor(Color.TRANSPARENT);
			main.setOrientation(LinearLayout.VERTICAL);
			main.setLayoutParams(mainparams);
			main.setGravity(Gravity.CENTER);
							
			LinearLayout bg = new LinearLayout(context);
			LinearLayout.LayoutParams bgparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			bg.setPadding(toDip(1), toDip(1), toDip(1), toDip(1));
			bg.setBackgroundColor(Color.argb(180, 150, 150, 150));
			bg.setOrientation(LinearLayout.VERTICAL);
			bg.setLayoutParams(bgparams);
			
			LinearLayout top = new LinearLayout(context);
			LinearLayout.LayoutParams topparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);	
			top.setPadding(toDip(15), toDip(15), toDip(15), toDip(15));
			top.setOrientation(LinearLayout.HORIZONTAL);
			top.setLayoutParams(topparams);
			top.setBackgroundColor(Color.argb(190, 30, 25, 25));	
			top.setGravity(Gravity.CENTER);
			
			bg.addView(top);
					
			LinearLayout bottom = new LinearLayout(context);
			LinearLayout.LayoutParams bottomparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
			bottom.setPadding(0, toDip(10), 0, 0);
			bottom.setOrientation(LinearLayout.HORIZONTAL);
			bottom.setLayoutParams(bottomparams);
			bottom.setBackgroundColor(Color.TRANSPARENT);		
			
			final LoaderImageView image = new LoaderImageView(getContext(), vgood.getVgoods().get(0).getImageUrl(),toDip(100),toDip(100));
			image.setPadding(0, 0, toDip(10), 0);
			TextView text = new TextView (context);
			String message = vgood.getVgoods().get(0).getRewardText();
			if(message != null)
				text.setText(message);
			else
				text.setText(context.getString(context.getResources().getIdentifier("vgoodmessagealert", "string", context.getPackageName())));
			text.setTextColor(Color.WHITE);
			top.addView(image);
			top.addView(text);
			
			// BUTTONS
			LinearLayout.LayoutParams buttonsparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,toDip(30),1f);
			buttonsparams.setMargins(toDip(5), 0, toDip(5), 0);
			
			BeButton b = new BeButton(context);		
			Button yes = new Button(context);
			yes.setBackgroundDrawable(
					b.setPressedBackg(
				    		new BDrawableGradient(0,toDip(30),BDrawableGradient.LIGHT_GRAY_GRADIENT),
							new BDrawableGradient(0,toDip(30),BDrawableGradient.GRAY_GRADIENT),
							new BDrawableGradient(0,toDip(30),BDrawableGradient.GRAY_GRADIENT)));
			yes.setText(context.getString(context.getResources().getIdentifier("vgoodmessagepositive", "string", context.getPackageName())));
			yes.setId(1);
			yes.setOnClickListener(this);
			yes.setLayoutParams(buttonsparams);
			yes.setPadding(0,0,0,0);
			yes.setTextColor(Color.argb(255, 41, 34, 34));
			Button no = new Button(context);
			no.setBackgroundDrawable(
					b.setPressedBackg(
				    		new BDrawableGradient(0,toDip(30),BDrawableGradient.LIGHT_GRAY_GRADIENT),
							new BDrawableGradient(0,toDip(30),BDrawableGradient.GRAY_GRADIENT),
							new BDrawableGradient(0,toDip(30),BDrawableGradient.GRAY_GRADIENT)));		
			no.setText(context.getString(context.getResources().getIdentifier("vgoodmessagenegative", "string", context.getPackageName())));	
			no.setId(0);
			no.setOnClickListener(this);
			no.setLayoutParams(buttonsparams);
			no.setPadding(0,0,0,0);
			no.setTextColor(Color.argb(255, 41, 34, 34));
			bottom.addView(yes);
			bottom.addView(no);
			
			
			main.addView(bg,mainparams);
			main.addView(bottom,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT));	
			
			this.setContentView(main);
			
		}catch (Exception e) {}	
	}
	
	private int toDip (int px) {
		double ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);
		
		return (int)(ratio*px);
	}

	public void onClick(View v) {
		if(v.getId() == 0){
			this.cancel();
			if(vgood.getVgoods().size() > 1){ // AUTO ASSIGN A VGOOD
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
		}else if(v.getId() == 1){
			if(vgood.getVgoods().size() > 1){
				VGoodGetList getVgoodList = new VGoodGetList(ctx, vgood);
				this.dismiss();
	    		getVgoodList.show();
			}else {
				VGoodGetDialog m = new VGoodGetDialog(ctx, vgood.getVgoods().get(0));
				this.dismiss();
				m.show();
			}
		}	
	}
}
