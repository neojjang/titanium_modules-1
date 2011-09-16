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


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.LinearLayout;




import com.beintoo.main.Beintoo;
import com.beintoo.main.Beintoo.BGetVgoodListener;
import com.beintoo.wrappers.VgoodChooseOne;

public class BeintooRecomDialog extends Dialog implements OnClickListener{
	Context ctx;
	private Dialog current;
	VgoodChooseOne vgood;
	private ImageView image;
	private BGetVgoodListener gvl;
	public BeintooRecomDialog(Context context, VgoodChooseOne v, BGetVgoodListener listener) {
		super(context, context.getResources().getIdentifier("ThemeBeintooVgood", "style", context.getPackageName()));
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		ctx = context;
		vgood = v;
		current = this;
		gvl = listener;
		
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
			top.setPadding(toDip(10), toDip(6), toDip(10), toDip(10));
			top.setOrientation(LinearLayout.VERTICAL);
			top.setLayoutParams(topparams);
			top.setBackgroundColor(Color.argb(190, 30, 25, 25));	
			top.setGravity(Gravity.CENTER);
			
			bg.addView(top);
			
			// TOP ROW LAYOUT
			LinearLayout infoclose = new LinearLayout(context);
			LinearLayout.LayoutParams infocloseparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);	
			infoclose.setOrientation(LinearLayout.HORIZONTAL);
			infoclose.setLayoutParams(infocloseparams);	
					
			// CLOSE BUTTON LAYOUT
			LinearLayout buttonlayout = new LinearLayout(context);	
			buttonlayout.setOrientation(LinearLayout.HORIZONTAL);
			buttonlayout.setLayoutParams(infocloseparams);	
			buttonlayout.setGravity(Gravity.RIGHT);
			
			// CLOSE BUTTON		
			Button no = new Button(context);
			no.setBackgroundDrawable(null);
			no.setText("X");
			no.setTextSize(18);
			no.setTypeface(null, Typeface.BOLD);
			no.setId(0);
			no.setOnClickListener(this);
			no.setPadding(0,toDip(-3),0,0);
			no.setTextColor(Color.WHITE);
			
			// INFO CLICK
			TextView info = new TextView(ctx);
			String message = vgood.getVgoods().get(0).getRewardText();
			if(message != null)
				info.setText(message);
			else
				info.setText(ctx.getString(context.getResources().getIdentifier("vgoodhtmlmessage", "string", context.getPackageName())));

			// ADD ALL TO TOPROW
			infoclose.addView(info);
			buttonlayout.addView(no);
			infoclose.addView(buttonlayout);
			
			// ADD TOPROW TO MAIN LAYOUT
			top.addView(infoclose);
			
			image = new ImageView(ctx);			
			image.setPadding(0, 0, 0, 0);	
			image.setOnClickListener(this);
			image.setId(1);
			top.addView(image);
			
			main.addView(bg,mainparams);
			this.setContentView(main);			
			
		}catch (Exception e) {e.printStackTrace();}	
	}
	
	public void loadAlert(){
		try{
			getDrawableFromUrl(vgood.getVgoods().get(0).getImageUrl());
		}catch(Exception e){e.printStackTrace();}
	}
	
	private int toDip (int px) {
		double ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);
		
		return (int)(ratio*px);
	}

	public void onClick(View v) {
		try {
			if(v.getId() == 0){ // REFUSE
				this.cancel();
			}else if(v.getId() == 1){ // ACCEPT
				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(vgood.getVgoods().get(0).getGetRealURL()));
				ctx.startActivity(browserIntent);
				this.cancel();
			}
		}catch(Exception e){}
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  current.show();
			  
			  if(gvl != null)
				  gvl.onComplete(vgood);
		  }
	};
	
	private String getWebUserAgent(){
		String userAgent = null;
		
		try {
			userAgent = Beintoo.userAgent;
		}catch(Exception e){ e.printStackTrace(); return userAgent;}
		
		return userAgent;
	}
	
	private void getDrawableFromUrl(final String url) throws IOException, MalformedURLException {
		try{ 
			new Thread(new Runnable(){     					
				public void run(){ 
					try{
						HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
						String userAgent = getWebUserAgent();
						if(userAgent != null)
							conn.setRequestProperty("User-Agent", userAgent);
						conn.connect();	            
			            InputStream is = conn.getInputStream();
			            BufferedInputStream bis = new BufferedInputStream(is);
			            Bitmap bmImg = BitmapFactory.decodeStream(is);
			            // SCALE THE IMAGE TO FIT THE DEVICE SIZE
			            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmImg, toDip(bmImg.getWidth()), toDip(bmImg.getHeight()), true);
			            bis.close();
			            is.close();
			            image.setImageBitmap(resizedbitmap);
			            UIhandler.sendEmptyMessage(0);
					}catch (Exception e){e.printStackTrace();} 
				}
			}).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
