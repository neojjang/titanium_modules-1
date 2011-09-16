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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


import com.beintoo.beintoosdk.BeintooMessages;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.UsersMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessagesRead extends Dialog{
	private Dialog current;
	private MessagesList previous;
	private double ratio;		
	private UsersMessage message;
	private boolean hasModified = false;
	
	public MessagesRead(Context context, UsersMessage m, MessagesList p) {
		super(context, context.getResources().getIdentifier("ThemeBeintoo", "style", context.getPackageName()));
		current = this;
		message = m; 
		previous = p;
		
		setContentView(current.getContext().getResources().getIdentifier("messagesread", "layout", current.getContext().getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		// GETTING DENSITY PIXELS RATIO
		ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*40),BDrawableGradient.BAR_GRADIENT));

		// SET UP VIEWS BACKGROUND
		LinearLayout top = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("top", "id", current.getContext().getPackageName()));
		top.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*85),BDrawableGradient.GRAY_GRADIENT));
		 
		try {
			// SET TITLE BAR
			TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
			LoaderImageView profilepict = (LoaderImageView) findViewById(current.getContext().getResources().getIdentifier("profilepict", "id", current.getContext().getPackageName()));
			String nick;
			
			if(message.getUserFrom() != null){
				t.setText(message.getUserFrom().getNickname());		
				profilepict.setImageDrawable(message.getUserFrom().getUserimg());
				nick = "<b>"+getContext().getString(current.getContext().getResources().getIdentifier("messagefrom", "string", current.getContext().getPackageName()))+"</b> "+message.getUserFrom().getNickname();
			}else{
				t.setText(message.getApp().getName());		
				profilepict.setImageDrawable(message.getApp().getImageUrl());
				nick = "<b>"+getContext().getString(current.getContext().getResources().getIdentifier("messagefrom", "string", current.getContext().getPackageName()))+"</b> "+message.getApp().getName();
			}
			
			SimpleDateFormat curFormater = new SimpleDateFormat("d-MMM-y HH:mm:ss", Locale.ENGLISH); 
			curFormater.setTimeZone(TimeZone.getTimeZone("GMT"));			
			Date msgDate = curFormater.parse(message.getCreationdate());			
			curFormater.setTimeZone(TimeZone.getDefault());			
			String date = "<b>"+getContext().getString(current.getContext().getResources().getIdentifier("messagedate", "string", current.getContext().getPackageName()))+"</b> "+(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT,Locale.getDefault()).format(msgDate));
			
			TextView fromview = (TextView) findViewById(current.getContext().getResources().getIdentifier("from", "id", current.getContext().getPackageName()));
			fromview.setText(Html.fromHtml(nick));
			TextView dateview = (TextView) findViewById(current.getContext().getResources().getIdentifier("date", "id", current.getContext().getPackageName()));
			dateview.setText(Html.fromHtml(date));						
			TextView messageview = (TextView) findViewById(current.getContext().getResources().getIdentifier("msgtext", "id", current.getContext().getPackageName()));
			
			// IF IT'S AN APP NOTIFICATION CHECK IF IS HTML 
			if(message.getUserFrom() != null) // message from user, avoid html
				messageview.setText(message.getText());
			else if(message.getType() != null && message.getType().equals("text/html")){ // message from app with html
				messageview.setMovementMethod(LinkMovementMethod.getInstance());
				messageview.setText(Html.fromHtml(message.getText()));
			}else // message from app without html
				messageview.setText(message.getText());
			
			
		}catch (Exception e){e.printStackTrace();}
		
		Button reply = (Button) findViewById(current.getContext().getResources().getIdentifier("reply", "id", current.getContext().getPackageName()));
		
		if(message.getUserFrom() == null)
			reply.setVisibility(LinearLayout.GONE);
		
	    BeButton b = new BeButton(context);
	    reply.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
	    reply.setBackgroundDrawable(
	    		b.setPressedBackg(
			    		new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));			    	 
	    reply.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				MessagesWrite mw = new MessagesWrite(current.getContext());
				mw.setToExtId(message.getUserFrom().getId());
				mw.setToNickname(message.getUserFrom().getNickname());
				mw.show();
			}
		});
	    
	    Button delete = (Button) findViewById(current.getContext().getResources().getIdentifier("delete", "id", current.getContext().getPackageName()));
	    delete.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
	    delete.setBackgroundDrawable(
	    		b.setPressedBackg(
			    		new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));			    	 
	    delete.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				showDeleteAlert();											
			}
		});		
	    
	    // check if the message was unread
	    checkIsUnread();
	}
	
	/**
	 * If the message is UNREAD it marks as READ
	 */
	private void checkIsUnread(){
		String status = message.getStatus();		
		if(status.equals("UNREAD")){
			hasModified = true;	
			new Thread(new Runnable(){   // UPDATE THE MESSAGE STATUS  					
	    		public void run(){
	    			try{
	    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));
	    				BeintooMessages bm = new BeintooMessages();
	    				bm.markAsRead(message.getId(), p.getUser().getId());
	    			}catch(Exception e){e.printStackTrace();}
	    		}
			}).start();	
			
			new Thread(new Runnable(){ // UPDATE USER MESSAGES COUNT    					
	    		public void run(){
	    			try{
	    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));
	    				BeintooHome b = (BeintooHome) Beintoo.homeDialog;
	    				b.updateMessage(p.getUser().getUnreadMessages()-1);
	    			}catch(Exception e){e.printStackTrace();}
	    		}
			}).start();	
		}
	}
	
	private void deleteMessage (){
		final ProgressDialog  dialog = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("messagedeleting", "string", current.getContext().getPackageName())),true);
		new Thread(new Runnable(){     					
    		public void run(){
    			try{
    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));
    				BeintooMessages bm = new BeintooMessages();
    				bm.markAsArchived(message.getId(), p.getUser().getId());
    				UIhandler.sendEmptyMessage(0);
    			}catch(Exception e){e.printStackTrace();dialog.dismiss();}
    			dialog.dismiss();
    		}
		}).start();
	} 
	
	private void showDeleteAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(current.getContext());
		builder.setMessage(current.getContext().getString(current.getContext().getResources().getIdentifier("messageaskdelete", "string", current.getContext().getPackageName())))
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   deleteMessage ();
	           }
	       })
	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                
	           }
	       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/*
	 * If the message was unread or the user deleted the message 
	 * we must realod the table 
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(hasModified){
			previous.startFirstLoading();
		}
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  current.dismiss();
			  previous.startFirstLoading();
		  }
	};

}
