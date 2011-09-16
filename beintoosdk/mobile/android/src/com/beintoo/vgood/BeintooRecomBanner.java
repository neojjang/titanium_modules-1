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

import com.beintoo.beintoosdkutility.BeintooAnimations;
import com.beintoo.main.Beintoo;
import com.beintoo.main.Beintoo.BGetVgoodListener;
import com.beintoo.wrappers.VgoodChooseOne;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class BeintooRecomBanner implements OnClickListener{
	private Context ctx;
	private ViewFlipper vf;
	private LinearLayout container;
	private VgoodChooseOne vgood;
	private ImageView image;
	private BGetVgoodListener gvl;
	
	public BeintooRecomBanner(Context context, LinearLayout c, VgoodChooseOne v, BGetVgoodListener listener) {
		ctx = context;
		vgood = v;
		container = c;
		gvl = listener;
		
		try {
			vf = new ViewFlipper(context);
			LinearLayout empty = new LinearLayout(context);
			vf.addView(empty);
	
			LinearLayout.LayoutParams bgparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			
			LinearLayout top = new LinearLayout(context);
			LinearLayout.LayoutParams topparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);			
			top.setOrientation(LinearLayout.HORIZONTAL);
			top.setLayoutParams(topparams);
			top.setBackgroundColor(Color.TRANSPARENT);
			top.setGravity(Gravity.CENTER);
	
			image = new ImageView(ctx);			
			top.addView(image);
			
			vf.addView(top);
			vf.setForegroundGravity(Gravity.CENTER);
			vf.setLayoutParams(bgparams);
			container.setOnClickListener(this);			
		}catch(Exception e){}	
	}
	
	public void loadBanner(){
		try {
			getDrawableFromUrl(vgood.getVgoods().get(0).getImageUrl());
		}catch (Exception e){e.printStackTrace();}
	}
	
	public void show(){ 
		try {
			Handler UIhandler = new Handler();
			UIhandler.postDelayed(inRunnable, 400);
			UIhandler.postDelayed(outRunnable, 12000);
			UIhandler.postDelayed(goneRunnable, 14000);
			
			if(gvl != null)
				gvl.onComplete(vgood);
		}catch(Exception e){}
	}

	private Runnable inRunnable = new Runnable() {
		public void run() {
			try {
				// SETTING UP CONTAINER			
				container.setBackgroundColor(Color.TRANSPARENT);
				container.setGravity(Gravity.CENTER);
				container.addView(vf);		
				container.setVisibility(LinearLayout.VISIBLE);
				
				vf.setInAnimation(BeintooAnimations.inFromBottomAnimation());
				vf.setOutAnimation(BeintooAnimations.outFromTopAnimation());
				vf.showPrevious();
			}catch(Exception e){}
		}
	};

	private Runnable outRunnable = new Runnable() {
		public void run() {
			try {
				vf.showPrevious();
			}catch(Exception e){}
		}
	};
	
	private Runnable goneRunnable = new Runnable() {
		public void run() {	
			try {
				container.setVisibility(LinearLayout.GONE);
				container.removeAllViews();
			}catch(Exception e){}
		}
	};

	public void onClick(View v) {
		try {
			// OPEN BROWSER		
			//BeintooBrowser bb = new BeintooBrowser(ctx,vgood.getVgoods().get(0).getGetRealURL());
			//bb.show();
			Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(vgood.getVgoods().get(0).getGetRealURL()));
			ctx.startActivity(browserIntent);
		}catch(Exception e){}
	}
	
	private String getWebUserAgent(){
		String userAgent = null;
		
		try {
			userAgent = Beintoo.userAgent;
		}catch(Exception e){ e.printStackTrace(); return userAgent;}
		
		return userAgent;
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  show();
		  }
	};
	
	
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
					}catch (Exception e){} 
				}
			}).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private int toDip (int px) {
		double ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);
		
		return (int)(ratio*px);
	}
}
