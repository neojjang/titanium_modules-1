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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import android.widget.LinearLayout;



import com.beintoo.main.Beintoo.BGetVgoodListener;
import com.beintoo.wrappers.VgoodChooseOne;

public class BeintooRecomDialogHTML extends Dialog implements OnClickListener{
	Context ctx;
	private Dialog current;
	VgoodChooseOne vgood;
	private WebView webview;
	private final int SHOW_DIALOG = 1;
	private final int HIDE_DIALOG = 2;
	private BGetVgoodListener gvl;
	
	public BeintooRecomDialogHTML(Context context, VgoodChooseOne v, BGetVgoodListener listener) {
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
			 
			webview = new WebView(ctx);
			webview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			webview.setBackgroundColor(0);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			
			top.addView(webview);	
			main.addView(bg,mainparams);
			
			this.setContentView(main);			
			
		}catch (Exception e) {e.printStackTrace();}	
	}
	
	public void loadAlert(){
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
							UIhandler.sendEmptyMessage(SHOW_DIALOG);
						}
						
						public void onPageStarted(WebView view, String url, Bitmap favicon) {	
							
						}
						
					    public boolean shouldOverrideUrlLoading(WebView view, String url) {
					    	Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
							ctx.startActivity(browserIntent);
							UIhandler.sendEmptyMessage(HIDE_DIALOG);
					        return true;
					    }
					});
					
					webview.setOnTouchListener(new View.OnTouchListener() {						
					    public boolean onTouch(View v, MotionEvent event) {
					      return (event.getAction() == MotionEvent.ACTION_MOVE);
					    }
					});
					
					webview.loadData("<div align=\"center\">"+vgood.getVgoods().get(0).getContent()+"</div>", vgood.getVgoods().get(0).getContentType(), "UTF-8");
				}catch (Exception e){e.printStackTrace();}
			} 
		}).start();
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
			  if(msg.what == SHOW_DIALOG){
				  current.show();
				  
				  if(gvl != null)
					  gvl.onComplete(vgood);
			  }else if(msg.what == HIDE_DIALOG)
				  current.dismiss();
		  }
	};
}
