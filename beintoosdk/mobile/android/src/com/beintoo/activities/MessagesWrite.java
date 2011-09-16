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


import com.beintoo.beintoosdk.BeintooMessages;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.MessageDisplayer;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.wrappers.Player;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessagesWrite extends Dialog{
	private MessagesWrite current = this;
	private Double ratio;
	public String toNickname;
	public String toExtId;
	private final int MESSAGE_SENT = 0;
	private final int MESSAGE_ERROR = 1;

	public MessagesWrite(Context context) {
		super(context, context.getResources().getIdentifier("ThemeBeintoo", "style", context.getPackageName()));
		current = this;

		setContentView(current.getContext().getResources().getIdentifier("messageswrite", "layout", current.getContext().getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		// SET TITLE
		TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		t.setText(current.getContext().getResources().getIdentifier("messagesend", "string", current.getContext().getPackageName()));		
		// GETTING DENSITY PIXELS RATIO
		ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*40),BDrawableGradient.BAR_GRADIENT));

		Button send = (Button) findViewById(current.getContext().getResources().getIdentifier("sendmessage", "id", current.getContext().getPackageName()));
	    BeButton b = new BeButton(context);
	    send.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
	    send.setBackgroundDrawable(
	    		b.setPressedBackg(
			    		new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));			    	 
	    send.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				final ProgressDialog  dialog = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("messagesending", "string", current.getContext().getPackageName())),true);
				new Thread(new Runnable(){      
		    		public void run(){
		    			try{		    				
		    				TextView editto = (TextView)findViewById(current.getContext().getResources().getIdentifier("editto", "id", current.getContext().getPackageName()));
		    				TextView edittext = (TextView)findViewById(current.getContext().getResources().getIdentifier("edittext", "id", current.getContext().getPackageName()));
		    				String to = editto.getText().toString().trim();
		    				String text = edittext.getText().toString().trim();
		    				
		    				if(text.length() > 2 && to.length() > 0){
			    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
			    				BeintooMessages bm = new BeintooMessages();
			    				bm.sendMessage(p.getUser().getId(), toExtId,text);
			    				Message msg = new Message();
			    				Bundle b = new Bundle();
			    				b.putString("to", to);
			    				msg.setData(b);
			    				msg.what = MESSAGE_SENT;			    				
			    				UIhandler.sendMessage(msg);			    				
		    				}else {
		    					UIhandler.sendEmptyMessage(MESSAGE_ERROR);
		    				}
		    				dialog.dismiss();
		    			}catch (Exception e){
		    				dialog.dismiss();
		    				e.printStackTrace();
		    			}
		    		}
				}).start();																
			}
		});
	    

	    EditText to = (EditText) findViewById(current.getContext().getResources().getIdentifier("editto", "id", current.getContext().getPackageName()));
	    to.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Friends mf = new Friends(current.getContext(), current, Friends.SHOW_FRIENDS_FROM_MESSAGES, android.R.style.Theme_Dialog);
				mf.show();
			}
		});
	}
	
	public void setToNickname(String toNickname) {
		TextView editto = (TextView)findViewById(current.getContext().getResources().getIdentifier("editto", "id", current.getContext().getPackageName()));
		editto.setText(toNickname);
		this.toNickname = toNickname;
	}

	public void setToExtId(String toExtId) {
		this.toExtId = toExtId;
	}
	
	private void showAlert(){
		AlertDialog alertDialog = new AlertDialog.Builder(current.getContext()).create();
	    alertDialog.setMessage(current.getContext().getString(current.getContext().getResources().getIdentifier("messageselect", "string", current.getContext().getPackageName())));
	    alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	    } }); 
	    alertDialog.show();
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {	
			  switch(msg.what){
			  	  case MESSAGE_SENT:
			  		  String to = msg.getData().getString("to");
			  		  MessageDisplayer.showMessage(current.getContext(), 
			  				  current.getContext().getString(current.getContext().getResources().getIdentifier("messagesent", "string", current.getContext().getPackageName()))+to, Gravity.BOTTOM);
			  		  current.dismiss();
				  break;
			  	  case MESSAGE_ERROR:
			  		showAlert();
				  break;
			  }
			  
			  super.handleMessage(msg);
		  }
	};
}
