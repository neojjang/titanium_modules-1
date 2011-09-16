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

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.GeolocationPermissions.Callback;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.DebugUtility;
import com.beintoo.beintoosdkutility.PreferencesHandler;


public class BeintooBrowser extends Dialog implements android.webkit.GeolocationPermissions.Callback{
	WebView webview;
	final Dialog current;
	public String openUrl = null;
	public BeintooBrowser(Context ctx, String url) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));		
		current = this;		
		setContentView(current.getContext().getResources().getIdentifier("browser", "layout", current.getContext().getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// GETTING DENSITY PIXELS RATIO
		double ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 47;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobar", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		webview = (WebView) findViewById(current.getContext().getResources().getIdentifier("webview", "id", current.getContext().getPackageName()));
		webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			
		webview.getSettings().setJavaScriptEnabled(true);
		
		 // ADD ZOOM CONTROLS
		 final FrameLayout.LayoutParams ZOOM_PARAMS =new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				 																	ViewGroup.LayoutParams.WRAP_CONTENT,
				 																	Gravity.BOTTOM);
		 final View zoom = webview.getZoomControls();
		 webview.addView(zoom, ZOOM_PARAMS);
		
		// CLEAR ALL PREVIOUS COOKIES BEFORE LOGIN
		clearAllCookies();
		
		WebSettings ws = webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setPluginsEnabled(true);

		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				ProgressBar p = (ProgressBar) findViewById(current.getContext().getResources().getIdentifier("progress", "id", current.getContext().getPackageName()));				
				p.setProgress(progress);
			}
			
			public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {				
				super.onGeolocationPermissionsShowPrompt(origin, callback);
				callback.invoke(origin, true, false);
			}
		});
		
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
					Toast.makeText(getContext(), "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
			
			public void onPageFinished(WebView view, String url){
				ProgressBar p = (ProgressBar) findViewById(current.getContext().getResources().getIdentifier("progress", "id", current.getContext().getPackageName()));
				p.setVisibility(LinearLayout.GONE);
			}
			
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				ProgressBar p = (ProgressBar) findViewById(current.getContext().getResources().getIdentifier("progress", "id", current.getContext().getPackageName()));
				p.setVisibility(LinearLayout.VISIBLE);
			}
		});
		webview.setInitialScale(1);
		String locationParams = getSavedPlayerLocationParams();
		
		if(url.contains("beintoo.com")) // ADD LOCATION COORDINATES IF ON BEINTOO 
			url = url + locationParams;
		
		webview.loadUrl(url);		
		// DEBUG CALLED URL
		DebugUtility.showLog(url);
		
		Button close = (Button) findViewById(current.getContext().getResources().getIdentifier("close", "id", current.getContext().getPackageName()));
		BeButton b = new BeButton(ctx);
		close.setBackgroundDrawable(b.setPressedBg(current.getContext().getResources().getIdentifier("close", "drawable", current.getContext().getPackageName()), current.getContext().getResources().getIdentifier("close_h", "drawable", current.getContext().getPackageName()), current.getContext().getResources().getIdentifier("close_h", "drawable", current.getContext().getPackageName())));	    
		close.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				current.dismiss();
			}
		});
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(webview.canGoBack())
	    		webview.goBack();
	    	else
	    		current.dismiss();
	        return false; 
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private String getSavedPlayerLocationParams(){
		try {
			Double latitude = Double.parseDouble(PreferencesHandler.getString("playerLatitude", current.getContext()));
			Double longitude = Double.parseDouble(PreferencesHandler.getString("playerLongitude", current.getContext()));
			Float accuracy = Float.parseFloat(PreferencesHandler.getString("playerAccuracy", current.getContext()));

			String params = "&lat="+latitude+"&lng="+longitude+"&acc="+accuracy;
			
			return params;
		}catch (Exception e){
			return "";
		}
		
	}

	/*public void clearCache() {
		File dir = getCacheDir();
		
		if (dir != null && dir.isDirectory()) {
			try {
				File[] children = dir.listFiles();
				if (children.length > 0) {
					for (int i = 0; i < children.length; i++) {
						File[] temp = children[i].listFiles();
						for (int x = 0; x < temp.length; x++) {
							temp[x].delete();
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}*/

	private void clearAllCookies (){ // NOW NOT USED
	    webview.clearCache(true);
	    webview.clearHistory();
	    CookieSyncManager cookieSyncMngr =
            CookieSyncManager.createInstance(getContext());
	    cookieSyncMngr.startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();        
	    cookieManager.removeSessionCookie();
	    cookieSyncMngr.stopSync();	    
	}

	public void invoke(String origin, boolean allow, boolean remember) {
		// TODO Auto-generated method stub
		
	}
}
