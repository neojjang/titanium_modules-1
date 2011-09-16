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

import java.util.regex.Pattern;


import com.beintoo.beintoosdk.BeintooPlayer;
import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdk.DeveloperConfiguration;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkui.BeintooFacebookLogin;
import com.beintoo.beintoosdkui.BeintooSignupBrowser;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.BeintooAnimations;
import com.beintoo.beintoosdkutility.BeintooSdkParams;
import com.beintoo.beintoosdkutility.DebugUtility;
import com.beintoo.beintoosdkutility.DeviceId;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.Player;

import com.google.beintoogson.Gson;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class UserRegistration extends Dialog{

	final Dialog current;
	private static final int GO_HOME = 1;
	private static final int SIGNUP_BROWSER = 2;
	private static final int NEXT_STEP = 3;
	private static final int MAIL_ERROR = 4;
	private static final int NICKNAME_ERROR = 5;
	private ViewSwitcher vs;
	private String nickname;
	private String email;
	private String currentUserUserExt;
	
	public UserRegistration(Context ctx) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));		
		setContentView(ctx.getResources().getIdentifier("registration", "layout", ctx.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		current = this;
		
		// GETTING DENSITY PIXELS RATIO
		double ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 47;
		LinearLayout beintooBar = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobar", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		// USED TO DISMISS KEYBOARD
		final InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		vs = (ViewSwitcher) findViewById(current.getContext().getResources().getIdentifier("viewFlipper", "id", current.getContext().getPackageName()));
		vs.setInAnimation(BeintooAnimations.inFromRightAnimation(500));
		vs.setOutAnimation(BeintooAnimations.outToLeftAnimation(500));
		
		// EMAIL
		final EditText emailText = (EditText) findViewById(current.getContext().getResources().getIdentifier("emailtext", "id", current.getContext().getPackageName()));
		
		// NICKNAME
		final EditText nicknameText = (EditText) findViewById(current.getContext().getResources().getIdentifier("nicktext", "id", current.getContext().getPackageName()));
		
		
		BeButton b = new BeButton(ctx);
		Button newuser = (Button) findViewById(current.getContext().getResources().getIdentifier("newuser", "id", current.getContext().getPackageName())); 
		newuser.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
		pixels = ratio * 50;
		newuser.setBackgroundDrawable(
				b.setPressedBackg(
			    		new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));
		newuser.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				final ProgressDialog  dialog = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("loading", "string", current.getContext().getPackageName())),true);
				new Thread(new Runnable(){      
            		public void run(){
            			try{ 
            				
            				email = emailText.getText().toString();
            				if(!checkEmail(email)) {            					
            					UIhandler.sendEmptyMessage(MAIL_ERROR);
            					dialog.dismiss();
            					
            					return;
            				}
            				
            				nickname = email.substring(0, email.indexOf("@"));
            				nicknameText.setText(nickname);
            				
            				Gson gson = new Gson();
							BeintooPlayer player = new BeintooPlayer();							 
							// WE NEED A LOGIN FIRST WITH THE NEW GUID BEFORE CONNECT.HTML
							String currentPlayer = PreferencesHandler.getString("currentPlayer", getContext());
		    				Player loggedUser = null;
		    				
		    				if(currentPlayer != null)
		    					loggedUser = gson.fromJson(currentPlayer, Player.class);
		    				
		    				Player newPlayer;
		    				
		    				DebugUtility.showLog("Logged player before new "+currentPlayer);
		    				
		    				if(loggedUser == null || loggedUser.getUser() != null) // GET A NEW RANDOM PLAYER
		    					newPlayer = player.playerLogin(null,null,null,DeviceId.getUniqueDeviceId(getContext()),null, null);
		    				else // USE THE CURRENT SAVED PLAYER 
		    					newPlayer = loggedUser;
		    				
							if(newPlayer.getGuid() != null){
								
								BeintooUser bu = new BeintooUser();
								bu.setOrUpdateUser("set", newPlayer.getGuid(), null, email, nickname, null, null, null, null, null, true, null, null);
								
								Player userLogin = player.playerLogin(null, newPlayer.getGuid(), null, null, null, null);
								PreferencesHandler.saveString("currentPlayer", new Gson().toJson(userLogin), getContext());
								currentUserUserExt = userLogin.getUser().getId();
								PreferencesHandler.saveBool("isLogged", true, getContext());
							}else{ // SHOW NETWORK ERROR
								ErrorDisplayer.showConnectionError("Connection error.\nPlease check your Internet connection.", getContext(),null);
							} 
							imm.hideSoftInputFromWindow(emailText.getApplicationWindowToken(), 0);
            				dialog.dismiss();
                			UIhandler.sendEmptyMessage(NEXT_STEP);
            			}catch(Exception e){dialog.dismiss(); e.printStackTrace();}	            			
            			
            		} 
				}).start();		
			}
        });
		
		Button confirmNick = (Button) findViewById(current.getContext().getResources().getIdentifier("confirmNick", "id", current.getContext().getPackageName())); 
		confirmNick.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
		confirmNick.setBackgroundDrawable(
				b.setPressedBackg(
			    		new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));
		confirmNick.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				final ProgressDialog  dialog = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("loading", "string", current.getContext().getPackageName())),true);
				new Thread(new Runnable(){      
            		public void run(){
            			try{ 				    
            				Boolean isValidNickname = true;
            				if(nicknameText.getText().length() < 2){
            					isValidNickname = false;
            					UIhandler.sendEmptyMessage(NICKNAME_ERROR);
            					dialog.dismiss();
            				}
            				
            				if(!isValidNickname) return;
            				
            				BeintooPlayer bp = new BeintooPlayer();
            				if(!nicknameText.getText().toString().equals(nickname)){
	            				BeintooUser bu = new BeintooUser();
								bu.setOrUpdateUser("update", null, currentUserUserExt, email, nicknameText.getText().toString(), null, null, null, null, null, true, null, null);
								
								Player userLogin = bp.playerLogin(currentUserUserExt, null, null, null, null, null);
								PreferencesHandler.saveString("currentPlayer", new Gson().toJson(userLogin), getContext());
            				}
            				
            				imm.hideSoftInputFromWindow(nicknameText.getApplicationWindowToken(), 0);
            				UIhandler.sendEmptyMessage(GO_HOME);
            				
            				current.dismiss();
            				dialog.dismiss();
            			}catch(Exception e){dialog.dismiss(); e.printStackTrace();}	            			
            			
            		} 
				}).start();		
			}
        });
		
		Button fblogin = (Button) findViewById(current.getContext().getResources().getIdentifier("fblogin", "id", current.getContext().getPackageName()));
		fblogin.setBackgroundDrawable(b.setPressedBg(current.getContext().getResources().getIdentifier("facebook", "drawable", current.getContext().getPackageName()), current.getContext().getResources().getIdentifier("facebook_h", "drawable", current.getContext().getPackageName()), current.getContext().getResources().getIdentifier("facebook_h", "drawable", current.getContext().getPackageName())));
		fblogin.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				
				String web = BeintooSdkParams.webUrl;
				if(BeintooSdkParams.useSandbox) 
					web = BeintooSdkParams.sandboxWebUrl;
				
				
				String redirect_uri = web + "m/landing_register_ok_no_script.html";
				String logged_uri = web + "m/landing_welcome_no_script.html";  
				
				StringBuilder sb = new StringBuilder();
				sb.append(web);
				sb.append("connect.html?signup=facebook&apikey=");
				sb.append(DeveloperConfiguration.apiKey);
				sb.append("&display=touch&redirect_uri=");
				sb.append(redirect_uri);
				sb.append("&logged_uri=");
				sb.append(logged_uri);
							
				String loginUrl = sb.toString();		
				
				BeintooFacebookLogin fbLoginBrowser = new BeintooFacebookLogin(getContext(),loginUrl);
				fbLoginBrowser.show();
				current.dismiss();
			}	
        });	
		
		TextView loginExisting = (TextView)findViewById(current.getContext().getResources().getIdentifier("loginexisting", "id", current.getContext().getPackageName()));
		loginExisting.setOnClickListener(new TextView.OnClickListener(){
			public void onClick(View v) {
				UserLogin ul = new UserLogin(current.getContext());
				ul.show();
				current.dismiss();
			}
		});
	}
	
	private boolean checkEmail(String email) {
		final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
		          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
		          "\\@" +
		          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
		          "(" +
		          "\\." +
		          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
		          ")+"
		);
		
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	private void showEmailError(){
		AlertDialog.Builder builder = new AlertDialog.Builder(current.getContext());
		builder.setMessage(current.getContext().getString(current.getContext().getResources().getIdentifier("invalidemail", "string", current.getContext().getPackageName())))
	       .setCancelable(false)
	       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                
	           }
	       }).create().show();
	}
	
	private void showNicknameError(){
		AlertDialog.Builder builder = new AlertDialog.Builder(current.getContext());
		builder.setMessage(current.getContext().getString(current.getContext().getResources().getIdentifier("invalidnickname", "string", current.getContext().getPackageName())))
	       .setCancelable(false)
	       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	    
	           }
	       }).create().show();
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  try{
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
				  }else  if(msg.what == NEXT_STEP){
					  vs.showNext();
					  LinearLayout fbandlogin = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("fbandlogin", "id", current.getContext().getPackageName()));
					  fbandlogin.setVisibility(View.GONE);
				  }else if(msg.what == MAIL_ERROR){
					  showEmailError();
				  }else if(msg.what == NICKNAME_ERROR){
					  showNicknameError();
				  }
			  }catch(Exception e){
				  e.printStackTrace();
			  }
			  super.handleMessage(msg);
		  }
	};
}
