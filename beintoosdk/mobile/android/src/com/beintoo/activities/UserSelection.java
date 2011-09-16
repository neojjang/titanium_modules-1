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



import java.util.ArrayList;

import com.beintoo.beintoosdk.BeintooPlayer;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.DeviceId;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.LoaderImageView;
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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UserSelection extends Dialog implements OnClickListener{
	User[] users;
	final Dialog current;
	private static double ratio;
	public UserSelection(Context ctx) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));		
		setContentView(ctx.getResources().getIdentifier("userselection", "layout", ctx.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		current = this;
		
		// GETTING DENSITY PIXELS RATIO
		ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 47;
		LinearLayout beintooBar = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobar", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		// SETTING UP TEXTBOX GRADIENT
		pixels = ratio * 60;
		LinearLayout textlayout = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("textlayout", "id", current.getContext().getPackageName()));
		textlayout.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.GRAY_GRADIENT));
		
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("usersTableLayout", "id", current.getContext().getPackageName()));
		table.setColumnStretchable(1, true);
	
		try {
			final ArrayList<View> rowList = new ArrayList<View>();
			String currentUsersList = PreferencesHandler.getString("deviceUsersList", getContext());
			Gson gson = new Gson();
			users = gson.fromJson(currentUsersList, User[].class);
			
			double rowHeightpx = ratio * 70;
			
			for(int i = 0; i< users.length; i++){
				if(users[i] != null){
					final LoaderImageView image = new LoaderImageView(getContext(), users[i].getUserimg(),(int)(ratio*70),(int)(ratio*70));
					TableRow row = createRow(image, users[i].getNickname(), getContext());
					
					if(i % 2 == 0)
						row.setBackgroundDrawable(new BDrawableGradient(0,(int)rowHeightpx,BDrawableGradient.LIGHT_GRAY_GRADIENT));
					else
						row.setBackgroundDrawable(new BDrawableGradient(0,(int)rowHeightpx,BDrawableGradient.GRAY_GRADIENT));
					
					row.setId(i);
					rowList.add(row);
					View spacer = createSpacer(getContext(),1,1);
					spacer.setId(-100);
					rowList.add(spacer);
					View spacer2 = createSpacer(getContext(),2,1);
					spacer2.setId(-100);
					rowList.add(spacer2);
				}
			}
			
			for (View row : rowList) {
		      row.setPadding(0, 0, 0, 0);	      
		      if(row.getId() != -100) // IF IS NOT A SPACER IT'S CLICKABLE
		    	  row.setOnClickListener(this);
		      table.addView(row);
		    }
		}catch (Exception e) {ErrorDisplayer.externalReport(e);}
		
		// ADD THE NEW PLAYER BUTTON
	    Button newplayer = (Button) findViewById(current.getContext().getResources().getIdentifier("anotheracc", "id", current.getContext().getPackageName()));
	    BeButton b = new BeButton(ctx);
	    pixels = ratio * 50;
	    newplayer.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
	    newplayer.setBackgroundDrawable(b.setPressedBackg(
	    		new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_BUTTON_GRADIENT),
				new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
				new BDrawableGradient(0,(int) pixels,BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));		
	        
	    newplayer.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				UserRegistration userLogin = new UserRegistration(getContext());
				userLogin.show();	
				Beintoo.currentDialog = current;
			}
        });
		    
	}
    
	public static TableRow createRow(View image, String txt, Context activity) {
		  TableRow row = new TableRow(activity);
		  row.setGravity(Gravity.CENTER_VERTICAL);
		  
		  LinearLayout main = new LinearLayout(row.getContext());
		  main.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  main.setOrientation(LinearLayout.HORIZONTAL);
		  main.setGravity(Gravity.CENTER_VERTICAL);
		  
		  TextView text = new TextView(activity);
		  text.setText(txt);
		  text.setPadding(0, 0, 0, 0);
		  text.setTextColor(Color.parseColor("#000000"));
		  
		  image.setPadding((int)(ratio * 15), 0, (int)(ratio * 15), 0);
		  		  
		  ((LinearLayout) image).setGravity(Gravity.LEFT);
		  
		  main.addView(image);
		  main.addView(text);
		  
		  row.addView(main, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int)(ratio * 75)));
		  
		  return row;
	}
	
	private static View createSpacer(Context activity, int color, int height) {
		  View spacer = new View(activity);
		  spacer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,height));
		  if(color == 1)
			  spacer.setBackgroundColor(Color.parseColor("#8F9193"));
		  else if(color == 2)
			  spacer.setBackgroundColor(Color.WHITE);
		  
		  return spacer;
	}
	 
	 
	// CALLED WHEN THE USER SELECT A USER IN THE TABLE
	public void onClick(View v) {
		final int selectedRow = v.getId();
		final ProgressDialog  dialog = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("login", "string", current.getContext().getPackageName())),true);
		new Thread(new Runnable(){      
    		public void run(){
    			try{   
					Gson gson = new Gson();
					
					String userExt = users[selectedRow].getId();
					String guid = null;
					try {
						Player p = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", current.getContext()), Player.class);
						if(p!= null)
							guid = p.getGuid(); 
					}catch(Exception e){e.printStackTrace();}
					
					BeintooPlayer player = new BeintooPlayer();
					Player newPlayer = player.playerLogin(userExt,guid,null,DeviceId.getUniqueDeviceId(getContext()),null, null);
					String jsonPlayer = gson.toJson(newPlayer);
					
					// SAVE THE CURRENT PLAYER
					PreferencesHandler.saveString("currentPlayer", jsonPlayer, getContext());
					// SET THAT THE PLAYER IS LOGGED IN BEINTOO
					PreferencesHandler.saveBool("isLogged", true, getContext());
					if(newPlayer.getUser() != null)
						UIhandler.sendEmptyMessage(1);
					else 
						UIhandler.sendEmptyMessage(0);
    			}catch(Exception e){}	
        			dialog.dismiss();
        		}
			}).start();		
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  if(msg.what == 1){
				  if(Beintoo.OPEN_DASHBOARD_AFTER_LOGIN){
					  BeintooHome beintooHome = new BeintooHome(getContext());
					  beintooHome.show();
				  } 
			  }
			  current.dismiss();
	        super.handleMessage(msg);
		  }
	};
}
