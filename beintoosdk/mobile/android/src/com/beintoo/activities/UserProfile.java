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

import java.util.Iterator;
import java.util.Map;


import com.beintoo.activities.alliances.UserAlliance;
import com.beintoo.beintoosdk.BeintooPlayer;
import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.DeviceId;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.PlayerScore;
import com.google.beintoogson.Gson;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserProfile extends Dialog {
	Dialog current;
	double ratio;
	private Player currentPlayer;
	
	private int currentSection;
	
	// HANDLERS
	private final int LOAD_PROFILE = 1;
	private final int SET_NEWMSG_BUTTON = 2;
	private static final int CONNECTION_ERROR = 3;
	
	// SECTIONS
	public static final int CURRENT_USER_PROFILE = 1; 
	public static final int FRIEND_USER_PROFILE = 2;
		
	public UserProfile(final Context ctx) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));				
		current = this;
		currentSection = CURRENT_USER_PROFILE;
		setupMainLayoutGradients();
		showLoading();
		setupCurrentUserProfileLayout();		
		startLoading(null, CURRENT_USER_PROFILE);			
	}
	
	public UserProfile(final Context ctx, final String userExt) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));				
		current = this;
		currentSection = FRIEND_USER_PROFILE;
		setupMainLayoutGradients();		
		showLoading();
		setupFriendProfileLayout();
		startLoading(userExt, FRIEND_USER_PROFILE);
	}
	
	private void setupMainLayoutGradients (){
		setContentView(current.getContext().getResources().getIdentifier("profileb", "layout", current.getContext().getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ratio = (current.getContext().getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		double pixels = ratio * 40;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		// SETTING UP PICTURE AND LEVEL CONTENT GRADIENT
		pixels = ratio * 104;
		LinearLayout textlayout = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("textlayout", "id", current.getContext().getPackageName()));
		textlayout.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.GRAY_GRADIENT));
	}
	
	private void setupCurrentUserProfileLayout (){
		// SET TITLE
		TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		t.setText(current.getContext().getResources().getIdentifier("profile", "string", current.getContext().getPackageName()));
		LinearLayout gc = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("goodcontent", "id", current.getContext().getPackageName()));
		gc.setVisibility(LinearLayout.INVISIBLE);
		
		currentPlayer = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", getContext()), Player.class);	
		
		Button logout = (Button) findViewById(current.getContext().getResources().getIdentifier("logout", "id", current.getContext().getPackageName()));
	    BeButton b = new BeButton(current.getContext());
	    logout.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
	    logout.setBackgroundDrawable(
	    		b.setPressedBackg(
			    		new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));			    	 
		logout.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Beintoo.logout(getContext());
				Beintoo.homeDialog.dismiss();
				current.dismiss();												
			}
		});
		
		Button detach = (Button) findViewById(current.getContext().getResources().getIdentifier("detach", "id", current.getContext().getPackageName()));
		detach.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
		detach.setBackgroundDrawable(
	    		b.setPressedBackg(
			    		new BDrawableGradient(0,(int) (ratio*30),BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*30),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*30),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));			    	 
		detach.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				BeintooUser usr = new BeintooUser();
				try {													
					usr.detachUserFromDevice(DeviceId.getUniqueDeviceId(current.getContext()), currentPlayer.getUser().getId());
					Beintoo.logout(getContext());	
					Beintoo.homeDialog.dismiss();
					current.dismiss();
				}catch (Exception e){e.printStackTrace();}	
			}
		});
		
		
		/*
		 * SETUP TOOLBAR BUTTONS 
		 */
		
		ImageButton friends = (ImageButton) findViewById(current.getContext().getResources().getIdentifier("friendsbt", "id", current.getContext().getPackageName()));
		friends.setOnClickListener(new ImageButton.OnClickListener(){
			public void onClick(View v) {
				Friends mf = new Friends(getContext(), null, Friends.MAIN_FRIENDS_MENU, current.getContext().getResources().getIdentifier("ThemeBeintoo", "style", current.getContext().getPackageName()));
		        mf.show();										
			}
		});
		
		ImageButton balancebt = (ImageButton) findViewById(current.getContext().getResources().getIdentifier("balancebt", "id", current.getContext().getPackageName()));
		balancebt.setOnClickListener(new ImageButton.OnClickListener(){
			public void onClick(View v) {
				UserBalance ub = new UserBalance(getContext());
		        ub.show();											
			} 
		});
		
		ImageButton alliancebt = (ImageButton) findViewById(current.getContext().getResources().getIdentifier("alliancebt", "id", current.getContext().getPackageName()));
		alliancebt.setOnClickListener(new ImageButton.OnClickListener(){
			public void onClick(View v) {
				UserAlliance ua = new UserAlliance(getContext(),UserAlliance.ENTRY_VIEW);
		        ua.show();											
			} 
		});
				
		ImageButton messages = (ImageButton) findViewById(current.getContext().getResources().getIdentifier("messagesbt", "id", current.getContext().getPackageName()));
		messages.setOnClickListener(new ImageButton.OnClickListener(){
			public void onClick(View v) {
				MessagesList ml = new MessagesList(getContext());
		        ml.show();											
			}
		});
		
		try {
			TextView unread = (TextView) findViewById(current.getContext().getResources().getIdentifier("msgunread", "id", current.getContext().getPackageName()));
			unread.setText(Integer.toString(currentPlayer.getUser().getUnreadMessages()));			
		}catch(Exception e){e.printStackTrace();}
		
		LinearLayout toolbar = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("toolbar", "id", current.getContext().getPackageName()));
		toolbar.setBackgroundDrawable(new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_BUTTON_GRADIENT));
		
	}
	
	private void setupFriendProfileLayout (){
		TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		Button logout = (Button) findViewById(current.getContext().getResources().getIdentifier("logout", "id", current.getContext().getPackageName()));
		Button detach = (Button) findViewById(current.getContext().getResources().getIdentifier("detach", "id", current.getContext().getPackageName()));
		LinearLayout gc = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("goodcontent", "id", current.getContext().getPackageName()));
		ImageView mail = (ImageView) findViewById(current.getContext().getResources().getIdentifier("messagesbt", "id", current.getContext().getPackageName()));
		
		t.setText(current.getContext().getResources().getIdentifier("profile", "string", current.getContext().getPackageName()));				
		logout.setVisibility(LinearLayout.GONE);
		detach.setVisibility(LinearLayout.GONE);
		gc.setVisibility(LinearLayout.INVISIBLE);
		mail.setVisibility(LinearLayout.GONE); 	
		
		LinearLayout toolbar = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("toolbar", "id", current.getContext().getPackageName()));
		toolbar.setVisibility(LinearLayout.GONE);
	}
	
	private void startLoading (final String userExt, final int ws) {
		new Thread(new Runnable(){      
    		public void run(){
    			try{ 
					BeintooPlayer bPlayer = new BeintooPlayer();
					if(ws == CURRENT_USER_PROFILE){
						Player currentSaved = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));				
						currentPlayer = bPlayer.getPlayer(currentSaved.getGuid());
					}else if(ws == FRIEND_USER_PROFILE){
						currentPlayer = bPlayer.getPlayerByUser(userExt, null);
						Bundle b = new Bundle();
						b.putString("nickname", currentPlayer.getUser().getNickname());
						b.putString("userExt", currentPlayer.getUser().getId());
						Message msg = new Message();
						msg.setData(b);
						msg.what = SET_NEWMSG_BUTTON;
						UIhandler.sendMessage(msg);
					}
					
					UIhandler.sendEmptyMessage(LOAD_PROFILE);
					
    			}catch (Exception e){manageConnectionException();}			
    		}
		}).start();
	}
	
	private void loadData (){
		// SETTING UP TEXTVIEWS AND PROFILE IMAGE
		LoaderImageView profilepict = (LoaderImageView) findViewById(current.getContext().getResources().getIdentifier("profilepict", "id", current.getContext().getPackageName()));		
		TextView nickname = (TextView) findViewById(current.getContext().getResources().getIdentifier("nickname", "id", current.getContext().getPackageName()));
		TextView level = (TextView) findViewById(current.getContext().getResources().getIdentifier("level", "id", current.getContext().getPackageName()));
		TextView dollars = (TextView) findViewById(current.getContext().getResources().getIdentifier("bedollars", "id", current.getContext().getPackageName()));
		TextView bescore = (TextView) findViewById(current.getContext().getResources().getIdentifier("salary", "id", current.getContext().getPackageName()));
		LinearLayout contestsContainer = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("contests", "id", current.getContext().getPackageName()));
		profilepict.setImageDrawable(currentPlayer.getUser().getUserimg());
		nickname.setText(currentPlayer.getUser().getNickname());
		level.setText(getContext().getString(current.getContext().getResources().getIdentifier("profileLevel", "string", current.getContext().getPackageName()))+fromIntToLevel(currentPlayer.getUser().getLevel()));
		dollars.setText("Bedollars: "+currentPlayer.getUser().getBedollars());
		bescore.setText("Bescore: "+currentPlayer.getUser().getBescore());
		
		Map<String, PlayerScore> playerScore = currentPlayer.getPlayerScore();
		
		if(playerScore != null) { // THE USER HAS SCORES FOR THE APP
			Iterator<?> it = playerScore.entrySet().iterator();
		    while (it.hasNext()) {			    	
		        @SuppressWarnings("unchecked")
				Map.Entry<String, PlayerScore> pairs = (Map.Entry<String, PlayerScore>) it.next();
		        if(pairs.getValue().getContest().isPublic()){
			        PlayerScore pscore = pairs.getValue();
			        
			        LinearLayout contestsRow = new LinearLayout(getContext());
			        LinearLayout contestsNameFeed = new LinearLayout(getContext());
			        LinearLayout contests = new LinearLayout(getContext());
			        LinearLayout grayLine = new LinearLayout(getContext());
			        LinearLayout grayLine2 = new LinearLayout(getContext());
			        
			        contestsRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			        contestsRow.setOrientation(LinearLayout.VERTICAL);
			        contestsNameFeed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			        contestsNameFeed.setOrientation(LinearLayout.VERTICAL);		
			        contestsNameFeed.setPadding(12,5,0,5);
			        contestsNameFeed.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*27),BDrawableGradient.GRAY_GRADIENT));			        
			        contests.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			        contests.setOrientation(LinearLayout.HORIZONTAL);		
			        contests.setPadding(12,5,0,5);
			        grayLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,1));
			        grayLine.setBackgroundColor(Color.parseColor("#8F9193"));			        
			        grayLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,1));
			        grayLine2.setBackgroundColor(Color.parseColor("#8F9193"));
			        
			        
			        TextView contestName = new TextView(getContext());				        
			        TextView feedData = null;
			        TextView totalScore = new TextView(getContext());
			        TextView lastScore = new TextView(getContext());
			        TextView bestScore = new TextView(getContext());
			        
			        contestName.setText(pscore.getContest().getName());
			        contestName.setTextColor(Color.BLACK);	 			        
			        contestsNameFeed.addView(contestName);
			        
			        String feed = pscore.getContest().getFeed();			        
			        if(feed != null){
			        	feedData = new TextView(getContext());
			        	feedData.setTextColor(Color.parseColor("#6E6E6E"));	 			        					        
			        	feedData.setText(feed);
			        	contestsNameFeed.addView(feedData);
			        	contestsNameFeed.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*60),BDrawableGradient.GRAY_GRADIENT));
			        }
			        
			        //totalScore.setPadding(15,5,0,5);
			        totalScore.setText(getContext().getString(current.getContext().getResources().getIdentifier("profileTotalScore", "string", current.getContext().getPackageName()))+pscore.getBalance());
			        totalScore.setTextSize(12);
			        totalScore.setTextColor(Color.parseColor("#545859"));
			        
			        lastScore.setPadding(10,0,0,0);
			        lastScore.setText(getContext().getString(current.getContext().getResources().getIdentifier("profileLastScore", "string", current.getContext().getPackageName()))+pscore.getLastscore());
			        lastScore.setTextSize(12);
			        lastScore.setTextColor(Color.parseColor("#545859"));
			        
			        bestScore.setPadding(10,0,0,0);
			        bestScore.setText(getContext().getString(current.getContext().getResources().getIdentifier("profileBestScore", "string", current.getContext().getPackageName()))+pscore.getBestscore());
			        bestScore.setTextSize(12);
			        bestScore.setTextColor(Color.parseColor("#545859"));
			        				        
			        
			        contestsRow.addView(contestsNameFeed);
			        contestsRow.addView(grayLine);
			        contests.addView(totalScore);			        
			        contests.addView(lastScore);
			        contests.addView(bestScore);
			        contestsRow.addView(contests);
			        contestsRow.addView(grayLine2);
			        
			        contestsContainer.addView(contestsRow);			        
		        }
		    }
		}else { // NO SCORES FOR THE APP
			if(currentSection == CURRENT_USER_PROFILE){
				TextView noScores = new TextView(getContext());
				noScores.setText(getContext().getString(current.getContext().getResources().getIdentifier("profileNoScores", "string", current.getContext().getPackageName())));
				noScores.setPadding(5,10,0,2);
				noScores.setTextColor(Color.BLACK);
				contestsContainer.addView(noScores);	
			}
		} 
		
		LinearLayout mc = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("maincontainer", "id", current.getContext().getPackageName()));
		LinearLayout l = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("loading", "id", current.getContext().getPackageName()));
		mc.removeView(l);		
		LinearLayout gc = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("goodcontent", "id", current.getContext().getPackageName()));
		gc.setVisibility(LinearLayout.VISIBLE);
	}
	
	private void showLoading (){
		ProgressBar pb = new ProgressBar(getContext());
		pb.setIndeterminateDrawable(getContext().getResources().getDrawable(current.getContext().getResources().getIdentifier("progress", "drawable", current.getContext().getPackageName())));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 50, 0, 0);				
		LinearLayout mc = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("loading", "id", current.getContext().getPackageName()));
		mc.setLayoutParams(params);
		mc.addView(pb);
	}
	
	// USED FOR FRIEND PROFILE NEW MESSAGE BUTTON
	private void setMsgButton(final String extId, final String nickname){
		Button newMessage = (Button) findViewById(current.getContext().getResources().getIdentifier("logout", "id", current.getContext().getPackageName()));
	    BeButton b = new BeButton(current.getContext());
	    newMessage.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
	    newMessage.setVisibility(LinearLayout.VISIBLE);
	    newMessage.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("messagesend", "string", current.getContext().getPackageName())));
	    newMessage.setBackgroundDrawable(
	    		b.setPressedBackg(
			    		new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));			    	 
	    newMessage.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				MessagesWrite mw = new MessagesWrite(current.getContext());
				mw.setToExtId(extId);
				mw.setToNickname(nickname);
				mw.show();												
			}
		});
	}
	
	private void manageConnectionException (){
		UIhandler.sendEmptyMessage(3);		
	}
	
	public String fromIntToLevel (int level) {
		if(level == 1) return "Novice";
		if(level == 2) return "Learner";
		if(level == 3) return "Passionate";
		if(level == 4) return "Winner";
		
		return "Novice";
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {			  
			  if(msg.what == LOAD_PROFILE){
				try {
					loadData();
				}catch(Exception e){e.printStackTrace();}
			  }else if(msg.what == SET_NEWMSG_BUTTON){
				Bundle b = msg.getData();
				try{
					setMsgButton(b.getString("userExt"),b.getString("nickname"));
					TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
					t.setText(String.format(current.getContext().getString(current.getContext().getResources().getIdentifier("profileOf", "string", current.getContext().getPackageName())), b.getString("nickname")));
			  	}catch(Exception e){e.printStackTrace();}
			  }else if(msg.what == CONNECTION_ERROR){
				  LinearLayout mc = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("goodcontent", "id", current.getContext().getPackageName()));
				  LinearLayout loading = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("loading", "id", current.getContext().getPackageName()));
				  mc.removeAllViews();
				  loading.removeAllViews();
				  ErrorDisplayer.showConnectionError(ErrorDisplayer.CONN_ERROR , current.getContext(), null);
			  }
			  
			  super.handleMessage(msg);
		  }
	};
}
