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




import com.beintoo.beintoosdkutility.BeintooAnimations;
import com.beintoo.main.Beintoo.BGetVgoodListener;
import com.beintoo.wrappers.VgoodChooseOne;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class BeintooRecomBannerHTML{
	private Context ctx;
	private ViewFlipper vf;
	private LinearLayout container;
	private VgoodChooseOne vgood;
	private WebView webview;
	private BGetVgoodListener gvl;
	
	public BeintooRecomBannerHTML(Context context, LinearLayout c, VgoodChooseOne v, BGetVgoodListener listener) {
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
	
			
			webview = new WebView(ctx);
			webview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			
			top.addView(webview);
			
			vf.addView(top);
			vf.setForegroundGravity(Gravity.CENTER);
			vf.setLayoutParams(bgparams);		
		}catch(Exception e){}	
	}
	
	public void loadRecomm (){
		new Thread(new Runnable(){     					
			public void run(){ 
				try{
					webview.setVerticalScrollBarEnabled(false);
					webview.setHorizontalScrollBarEnabled(false);
					webview.setFocusableInTouchMode(false);
					webview.setFocusable(false);					
					webview.setWebViewClient(new WebViewClient() {
						public void onReceivedError(WebView view, int errorCode,
								String description, String failingUrl) {
						}
						
						public void onPageFinished(WebView view, String url){
							UIhandler.sendEmptyMessage(0);
						}
						
						public void onPageStarted(WebView view, String url, Bitmap favicon) {				
						}
						
					    public boolean shouldOverrideUrlLoading(WebView view, String url) {
					    	Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
							ctx.startActivity(browserIntent);
					        return true;
					    }
					});
					
					webview.loadData(vgood.getVgoods().get(0).getContent(), vgood.getVgoods().get(0).getContentType(), "utf-8");					
				}catch (Exception e){e.printStackTrace();}
			} 
		}).start();
	}
	
	public void show(){ 
		try {
			Handler UIhandler = new Handler();
			UIhandler.postDelayed(inRunnable, 400);
			UIhandler.postDelayed(outRunnable, 12000);
			UIhandler.postDelayed(goneRunnable, 14000);
			
			if(gvl != null)
				gvl.onComplete(vgood);
		}catch(Exception e){e.printStackTrace();}
	}

	private Runnable inRunnable = new Runnable() {
		public void run() {
			try {	
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

	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  show();
		  }
	};
}
