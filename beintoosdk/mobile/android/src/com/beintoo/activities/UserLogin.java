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
package com.beintoo.activities;


import com.beintoo.beintoosdk.BeintooPlayer;
import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkui.BeintooSignupBrowser;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.DebugUtility;
import com.beintoo.beintoosdkutility.DeviceId;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.User;
import com.google.beintoogson.Gson;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class UserLogin extends Dialog{

	final Dialog current;
	private static final int GO_HOME = 1;
	private static final int SIGNUP_BROWSER = 2;
	
	public UserLogin(Context ctx) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));		
		setContentView(ctx.getResources().getIdentifier("login", "layout", ctx.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		current = this;
		
		// GETTING DENSITY PIXELS RATIO
		double ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 47;
		LinearLayout beintooBar = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobar", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		// SETTING UP TEXTBOX GRADIENT
		pixels = (ratio * 45);
		LinearLayout textlayout = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("textlayout", "id", current.getContext().getPackageName()));
		textlayout.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.GRAY_GRADIENT));
		
		// USED TO DISMISS KEYBOARD
		final InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		
		/*
		 *  BUTTON CLICKED ON NEW USER
		 *  PLAYERLOGIN WITH NO GUID THE GUID WILL BE ASSIGNED FROM THE API
		 *  THEN CONNECT.HTML WITH THE ASSIGNE GUID
		 */
		pixels = ratio * 50;
		BeButton b = new BeButton(ctx);
		
		Button go = (Button) findViewById(current.getContext().getResources().getIdentifier("go", "id", current.getContext().getPackageName()));
		go.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
		go.setBackgroundDrawable(b.setPressedBackg(
	    		new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_BUTTON_GRADIENT),
				new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
				new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));
		go.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {				
				final ProgressDialog  dialog = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("login", "string", current.getContext().getPackageName())),true);
				new Thread(new Runnable(){      
            		public void run(){
            			try{    
				
							EditText email = (EditText) findViewById(current.getContext().getResources().getIdentifier("mail", "id", current.getContext().getPackageName()));
							EditText psw = (EditText) findViewById(current.getContext().getResources().getIdentifier("password", "id", current.getContext().getPackageName()));
							
							BeintooUser user = new BeintooUser();
							User loggedUser = user.getUsersByEmail(email.getText().toString(), psw.getText().toString());
							
							// DEBUG
							DebugUtility.showLog("logged: "+loggedUser.getId());
							imm.hideSoftInputFromWindow(email.getApplicationWindowToken(), 0);
				            imm.hideSoftInputFromWindow(psw.getApplicationWindowToken(), 0);
							// IF THE USER O PASSWORD ARE WRONG SHOW AN ERROR
							if(loggedUser.getId() == null) {
								// DISMISS THE KEYBOARD								
								dialog.dismiss(); // DISMISS LOGIN DIALOG
								ErrorDisplayer.showConnectionErrorOnThread(getContext().getString(current.getContext().getResources().getIdentifier("wronglogin", "string", current.getContext().getPackageName())), getContext(),null);								
							}else {// PLAYERLOGIN AND THEN CLOSE THE LOGIN FORM AND GO HOME
								BeintooPlayer player = new BeintooPlayer();
								String guid = null;
								try {
									Player p = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", current.getContext()), Player.class);
									if(p!= null)
										guid = p.getGuid(); 
								}catch(Exception e){e.printStackTrace();}
								
								Player newPlayer = player.playerLogin(loggedUser.getId(),guid,null,DeviceId.getUniqueDeviceId(getContext()),null, null);
								Gson gson = new Gson();					
								String jsonPlayer = gson.toJson(newPlayer);
								 
								// SAVE THE CURRENT PLAYER
								PreferencesHandler.saveString("currentPlayer", jsonPlayer, getContext());
								// SET THAT THE PLAYER IS LOGGED IN BEINTOO
								PreferencesHandler.saveBool("isLogged", true, getContext());
								
								// FINALLY GO USER HOME
								UIhandler.sendEmptyMessage(GO_HOME);	
								dialog.dismiss();
							}							
            			}catch(Exception e){
            				dialog.dismiss();
            				ErrorDisplayer.showConnectionErrorOnThread(getContext().getString(current.getContext().getResources().getIdentifier("wronglogin", "string", current.getContext().getPackageName())), getContext(),null);
            			}	            			
            		} 
				}).start();
				
			}	
        });		
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  if(msg.what == GO_HOME){
				if(Beintoo.OPEN_DASHBOARD_AFTER_LOGIN){
				  BeintooHome beintooHome = new BeintooHome(getContext());
				  beintooHome.show();
				}
			  	Beintoo.currentDialog.dismiss();
			  	current.dismiss();
			  }else  if(msg.what == SIGNUP_BROWSER){
				BeintooSignupBrowser signupBrowser = new BeintooSignupBrowser(getContext());
				signupBrowser.show();
				current.dismiss();
			  }
			  super.handleMessage(msg);
		  }
	};
}
