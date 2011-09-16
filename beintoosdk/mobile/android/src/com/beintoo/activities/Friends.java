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



import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdk.DeveloperConfiguration;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.MessageDisplayer;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Friends extends Dialog implements OnClickListener{
	private Dialog current;
	private Dialog previous;
	private double ratio;
	
	public static final int MAIN_FRIENDS_MENU = 0;
	public static final int SHOW_FRIENDS_FROM_MESSAGES = 1;
	public static final int SHOW_FRIENDS_FROM_PROFILE = 2;
	public static final int FIND_FRIENDS = 3;
	public static final int FRIENDSHIP_REQUESTS = 4;
	
	public static final int SHOW_EMPTY = 5;
	public static final int LOAD_TABLE = 6;
	public static final int SHOW_MESSAGE = 7;
	public static final int OPEN_RTAF = 8;
	
	private int whichSection;
	
	User [] friends;
	
	public Friends(Context context, Dialog p, int ws, int theme) {
		super(context,theme);
		current = this; 
		previous = p;
		whichSection = ws;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		setContentView(current.getContext().getResources().getIdentifier("friendlist", "layout", current.getContext().getPackageName()));
				
		ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
				
		if(ws == SHOW_FRIENDS_FROM_MESSAGES){
			initFromMessage();
		}else if(ws == SHOW_FRIENDS_FROM_PROFILE){
			initFromProfile();
		}else if(ws == MAIN_FRIENDS_MENU){
			initMainMenu();			
		}else if(ws == FRIENDSHIP_REQUESTS){
			initFriendshipReq();
		}else if(ws == FIND_FRIENDS){
			initFindFriends();
		}
	}

	private void initFromMessage(){
		setContentView(current.getContext().getResources().getIdentifier("friendlist", "layout", current.getContext().getPackageName()));
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.LIGHT_GRAY_GRADIENT));
		TextView title = (TextView) findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		title.setTextColor(Color.BLACK);
		title.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("friendSel", "string", current.getContext().getPackageName())));
		startLoading();
	}
	
	private void initFromProfile(){
		setContentView(current.getContext().getResources().getIdentifier("friendlist", "layout", current.getContext().getPackageName()));
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.BAR_GRADIENT));
		TextView title = (TextView) findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		title.setTextColor(Color.WHITE);
		title.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("friendsTitle", "string", current.getContext().getPackageName())));
		startLoading();
	}
	
	private void initMainMenu(){
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.BAR_GRADIENT));
		TextView title = (TextView) findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		title.setTextColor(Color.WHITE);
		title.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("friends", "string", current.getContext().getPackageName())));
		loadSelectionTable();
	}
	
	private void initFindFriends(){
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.BAR_GRADIENT));
		TextView title = (TextView) findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		title.setTextColor(Color.WHITE);
		title.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("friendFind", "string", current.getContext().getPackageName())));
	
		ImageView searchicon = new ImageView(current.getContext());
		searchicon.setImageResource(current.getContext().getResources().getIdentifier("findfriend", "drawable", current.getContext().getPackageName()));
		EditText querytext = new EditText(current.getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
		lp.setMargins(10, 0, 0, 0);
		querytext.setLayoutParams(lp);
		querytext.setHint(current.getContext().getString(current.getContext().getResources().getIdentifier("friendFindeditsearch", "string", current.getContext().getPackageName())));
		querytext.setPadding((int)(ratio * 10), 0, 0, 0);
		querytext.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		querytext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            	if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {            		
            		
            		InputMethodManager imm = (InputMethodManager)current.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            		
                	loadSearchResults(v.getText().toString());                	
                	return true;
                }
                return false;
            }
        });
		LinearLayout searchBar = new LinearLayout(current.getContext());
		searchBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		searchBar.setOrientation(LinearLayout.HORIZONTAL);
		searchBar.setPadding(10, 10, 10, 10);
		searchBar.addView(searchicon);
		searchBar.addView(querytext);
		searchBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 90),BDrawableGradient.GRAY_GRADIENT));		
		LinearLayout searchlayout = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("searchspace", "id", current.getContext().getPackageName()));
		searchlayout.addView(searchBar);
		searchlayout.addView(createSpacer(current.getContext(),1,1));
		searchlayout.addView(createSpacer(current.getContext(),2,1));
		searchlayout.setVisibility(LinearLayout.VISIBLE);
	}
	
	private void initFriendshipReq (){
		setContentView(current.getContext().getResources().getIdentifier("friendlist", "layout", current.getContext().getPackageName()));
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.BAR_GRADIENT));
		TextView title = (TextView) findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		title.setTextColor(Color.WHITE);
		title.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("friendReqTitle", "string", current.getContext().getPackageName())));		
		TextView tiptext = (TextView) findViewById(current.getContext().getResources().getIdentifier("subtitleinfo", "id", current.getContext().getPackageName()));
		tiptext.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("friendReqSubtitle", "string", current.getContext().getPackageName())));		
		LinearLayout tip = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("tip", "id", current.getContext().getPackageName()));
		tip.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*27),BDrawableGradient.LIGHT_GRAY_GRADIENT));
		tip.setVisibility(LinearLayout.VISIBLE);		
		startLoading();
	}
	
	private void startLoading(){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();
		showLoading();
		if(whichSection == SHOW_FRIENDS_FROM_MESSAGES || whichSection == SHOW_FRIENDS_FROM_PROFILE){
			loadFriendsData();
		}else if(whichSection == FRIENDSHIP_REQUESTS){
			loadPendingFriendsReq();
		}	
	}
	
	private void loadFriendsData () {
		new Thread(new Runnable(){      
    		public void run(){
    			try{ 
    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
    				BeintooUser u = new BeintooUser();
    				friends = u.getUserFriends(p.getUser().getId(), null);
    				if(friends.length > 0)
    					UIhandler.sendEmptyMessage(LOAD_TABLE);
    				else
    					setEmptyTable(SHOW_FRIENDS_FROM_MESSAGES);
    			}catch (Exception e){
    				e.printStackTrace();
    			}
    		}
		}).start();
	}
	 
	private void loadPendingFriendsReq () {
		new Thread(new Runnable(){      
    		public void run(){
    			try{ 
    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
    				BeintooUser u = new BeintooUser();
    				friends = u.getUserFriendshipRequests(p.getUser().getId(), null);		
    				if(friends.length > 0)
    					UIhandler.sendEmptyMessage(LOAD_TABLE);
    				else
    					setEmptyTable(FRIENDSHIP_REQUESTS);
    			}catch (Exception e){
    				e.printStackTrace();
    			}
    		} 
		}).start();
	}
	
	private void loadSearchResults (final String query) {		
		showLoading();
		new Thread(new Runnable(){      
    		public void run(){
    			try{  
    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
    				BeintooUser u = new BeintooUser();
    				friends = u.findFriendsByQuery(query, p.getUser().getId(), true,  null);
    				
    				if(friends.length > 0)
    					UIhandler.sendEmptyMessage(LOAD_TABLE);
    				else
    					setEmptyTable(FIND_FRIENDS);
    			}catch (Exception e){
    				setEmptyTable(FIND_FRIENDS);
    				e.printStackTrace();
    			}
    		}
		}).start();
	}   
	
	private void showLoading (){
		ProgressBar pb = new ProgressBar(getContext());
		pb.setIndeterminateDrawable(getContext().getResources().getDrawable(current.getContext().getResources().getIdentifier("progress", "drawable", current.getContext().getPackageName())));
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		pb.setPadding(0, (int)(50*ratio), 0, (int)(50*ratio));		
		pb.setLayoutParams(params);
		pb.setId(5000);
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();
		table.setGravity(Gravity.CENTER);
		table.addView(pb);
	} 
	
	public void loadFriendsTable(){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();
		
		final ArrayList<View> rowList = new ArrayList<View>();
		
    	for(int i = 0; i<friends.length; i++){
    		final LoaderImageView image = new LoaderImageView(getContext(),friends[i].getUserimg(),(int)(ratio * 70),(int)(ratio *70));
       		
    		TableRow row = createRowFriendList(image, friends[i].getNickname(),friends[i].getName(), getContext());
			row.setId(i);
			rowList.add(row);			 
			row.setOnClickListener(this);
			
			View spacer = createSpacer(getContext(),1,1);
			spacer.setId(-100);
			rowList.add(spacer);
			View spacer2 = createSpacer(getContext(),2,1);
			spacer2.setId(-100);
			rowList.add(spacer2);
			
			BeButton b = new BeButton(current.getContext());
			if(i % 2 == 0)
	    		row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.LIGHT_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			else
				row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT)));
	
    	}	 
	    
	    for (View row : rowList) {	      
		      table.addView(row);
		}
	} 
	
	public void loadSelectionTable(){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();		
				
		final ArrayList<View> rowList = new ArrayList<View>();
		
		
		for(int i = 0; i < 4;i++){
	    	ImageView icon = new ImageView(getContext());
	    	String section = "";
	    	String description = "";
	    	if(i == 0){
	    		icon.setImageResource(current.getContext().getResources().getIdentifier("findfriend", "drawable", current.getContext().getPackageName()));
	    		section = current.getContext().getString(current.getContext().getResources().getIdentifier("friendFind", "string", current.getContext().getPackageName()));
	    		description = current.getContext().getString(current.getContext().getResources().getIdentifier("friendFindT", "string", current.getContext().getPackageName()));
	    	}else if(i == 1){
	    		icon.setImageResource(current.getContext().getResources().getIdentifier("yourfriend", "drawable", current.getContext().getPackageName()));
	    		section = current.getContext().getString(current.getContext().getResources().getIdentifier("friendYours", "string", current.getContext().getPackageName()));
	    		description = current.getContext().getString(current.getContext().getResources().getIdentifier("friendYoursT", "string", current.getContext().getPackageName()));
	    	}else if(i == 2){
	    		icon.setImageResource(current.getContext().getResources().getIdentifier("friendreq", "drawable", current.getContext().getPackageName()));
	    		section = current.getContext().getString(current.getContext().getResources().getIdentifier("friendReqTitle", "string", current.getContext().getPackageName()));
	    		description = current.getContext().getString(current.getContext().getResources().getIdentifier("friendFriendshipReqT", "string", current.getContext().getPackageName()));
	    	}else if(i == 3){
	    		icon.setImageResource(current.getContext().getResources().getIdentifier("rtaf", "drawable", current.getContext().getPackageName()));
	    		section = current.getContext().getString(current.getContext().getResources().getIdentifier("friendRecommendT", "string", current.getContext().getPackageName()));
	    		description = current.getContext().getString(current.getContext().getResources().getIdentifier("friendRecommend", "string", current.getContext().getPackageName()));
	    	}
	    	
			TableRow row = createRowFriendList(icon, section,description, getContext());
			row.setId(i);
			rowList.add(row);			
			row.setOnClickListener(this);
			
			View spacer = createSpacer(getContext(),1,1);
			spacer.setId(-100);
			rowList.add(spacer);
			View spacer2 = createSpacer(getContext(),2,1);
			spacer2.setId(-100);
			rowList.add(spacer2);
			
			BeButton b = new BeButton(current.getContext());
			if(i % 2 == 0)
	    		row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.LIGHT_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			else
				row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 80),BDrawableGradient.HIGH_GRAY_GRADIENT)));
	
		}
	    
	    for (View row : rowList) {	      
		      table.addView(row);
		}
	}
	
	public TableRow createRowFriendList(View image, String nick,String name, Context activity) {
		  TableRow row = new TableRow(activity);
		  row.setGravity(Gravity.CENTER);
		  
		  image.setPadding(15, 4, 10, 4);
		  
		  LinearLayout foto = new LinearLayout(row.getContext());
		  foto.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  foto.setOrientation(LinearLayout.VERTICAL);
		  
		  foto.addView(image);
		  row.addView(foto,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT)); //
		 
		  LinearLayout main = new LinearLayout(row.getContext());
		  main.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  main.setOrientation(LinearLayout.VERTICAL);
		  main.setGravity(Gravity.CENTER_VERTICAL);
		  
		  TextView nickname = new TextView(activity);		  
		  nickname.setText(nick);
		  nickname.setPadding(5, 0, 0, 0);
		  nickname.setTextColor(Color.parseColor("#545859")); 
		  nickname.setMaxLines(1);
		  nickname.setTextSize(16);
		  nickname.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));		  
		  
		  TextView scoreView = new TextView(activity);		
		  scoreView.setText(name);	
		  scoreView.setPadding(5, 0, 0, 0);
		  scoreView.setTextColor(Color.parseColor("#787A77"));		  
		  scoreView.setTextSize(12);
		  scoreView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  
		  main.addView(nickname);
		  main.addView(scoreView);		  
		  row.addView(main,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int)(ratio * 80))); 
		  
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
	
	private void setEmptyTable(int section){
		Message msg = new Message();
		Bundle b = new Bundle();
		if(section == SHOW_FRIENDS_FROM_MESSAGES || section == SHOW_FRIENDS_FROM_PROFILE){
			msg.what = SHOW_EMPTY;
			b.putString("message", current.getContext().getString(current.getContext().getResources().getIdentifier("friendsNo", "string", current.getContext().getPackageName())));
			msg.setData(b);
			UIhandler.sendMessage(msg);
		}else if(section == FIND_FRIENDS){
			msg.what = SHOW_EMPTY;
			b.putString("message", current.getContext().getString(current.getContext().getResources().getIdentifier("friendNotfound", "string", current.getContext().getPackageName())));
			msg.setData(b);
			UIhandler.sendMessage(msg);
		}else if(section == FRIENDSHIP_REQUESTS){
			msg.what = SHOW_EMPTY;
			b.putString("message", current.getContext().getString(current.getContext().getResources().getIdentifier("friendReqEmpty", "string", current.getContext().getPackageName())));
			msg.setData(b);
			UIhandler.sendMessage(msg);
		} 
	}
	
	private void showFriendShipDialog(final String userExtTo, final View row){
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage(current.getContext().getString(current.getContext().getResources().getIdentifier("friendReqAccept", "string", current.getContext().getPackageName())))
		       .setCancelable(false)
		       .setPositiveButton(getContext().getString(current.getContext().getResources().getIdentifier("yes", "string", current.getContext().getPackageName())), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   friendshipThread(userExtTo, "accept");
		        	   TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		        	   table.removeView(row);		        	   
		           }
		       })
		       .setNegativeButton(current.getContext().getString(current.getContext().getResources().getIdentifier("no", "string", current.getContext().getPackageName())), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   friendshipThread(userExtTo, "ignore");
		        	   TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		        	   table.removeView(row);		        	   
		           }
		       }).setNeutralButton(current.getContext().getString(current.getContext().getResources().getIdentifier("cancel", "string", current.getContext().getPackageName())),  new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show(); 
	}
	
	private void addFriendDialog(final String userExtTo, final String nickname){
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		
		builder.setMessage(String.format(current.getContext().getString(current.getContext().getResources().getIdentifier("friendSendReq", "string", current.getContext().getPackageName())), nickname))
		       .setCancelable(false)
		       .setPositiveButton(getContext().getString(current.getContext().getResources().getIdentifier("yes", "string", current.getContext().getPackageName())), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   friendshipThread(userExtTo, "invite");
		           }
		       })
		       .setNegativeButton(getContext().getString(current.getContext().getResources().getIdentifier("no", "string", current.getContext().getPackageName())), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show(); 
	}
	 
	public void friendshipThread(final String userExtTo, final String action){		
		new Thread(new Runnable(){      
    		public void run(){
    			try{  
    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
    				BeintooUser bu = new BeintooUser();
    				Message msg = new Message();
    				Bundle b = new Bundle();
    				if(action.equalsIgnoreCase("accept")){
    					com.beintoo.wrappers.Message m = bu.respondToFriendshipRequests(p.getUser().getId(), userExtTo, action, null);
    					if(m.getMessage().equalsIgnoreCase("OK")){
    						b.putString("message", getContext().getString(current.getContext().getResources().getIdentifier("friendReqAcc", "string", current.getContext().getPackageName())));
    						msg.what = SHOW_MESSAGE;
    						msg.setData(b);
    						UIhandler.sendMessage(msg);
    					}
    				}else if(action.equalsIgnoreCase("ignore")){
    					com.beintoo.wrappers.Message m = bu.respondToFriendshipRequests(p.getUser().getId(), userExtTo, action, null);
    					if(m.getMessage().equalsIgnoreCase("OK")){
    						b.putString("message", getContext().getString(current.getContext().getResources().getIdentifier("friendReqIgn", "string", current.getContext().getPackageName())));
    						msg.what = SHOW_MESSAGE;
    						msg.setData(b);
    						UIhandler.sendMessage(msg);
    					}
    				}else if(action.equalsIgnoreCase("invite")){
    					com.beintoo.wrappers.Message m = bu.respondToFriendshipRequests(p.getUser().getId(), userExtTo, action, null);
    					if(m.getMessage().equalsIgnoreCase("OK")){
    						b.putString("message", getContext().getString(current.getContext().getResources().getIdentifier("friendReqSent", "string", current.getContext().getPackageName())));
    						msg.what = SHOW_MESSAGE;
    						msg.setData(b);
    						UIhandler.sendMessage(msg);
    					}
    				}
    				
    			}catch (Exception e){
    				e.printStackTrace();
    			} 
    		}
		}).start();
	}  
	
	public void onClick(View v) {
		if(whichSection == SHOW_FRIENDS_FROM_MESSAGES){
			((MessagesWrite) previous).setToNickname(friends[v.getId()].getNickname());
			((MessagesWrite)previous).setToExtId(friends[v.getId()].getId());
			current.dismiss();
		}else if(whichSection == SHOW_FRIENDS_FROM_PROFILE){
			UserProfile userProfile = new UserProfile(getContext(),friends[v.getId()].getId());
			userProfile.show();
		}else if(whichSection == MAIN_FRIENDS_MENU){
			 if(v.getId() == 0){
				Friends friend = new Friends(getContext(), null, Friends.FIND_FRIENDS, current.getContext().getResources().getIdentifier("ThemeBeintoo", "style", current.getContext().getPackageName()));
				friend.show();
			}else if(v.getId() == 1){
				Friends friend = new Friends(getContext(), null, Friends.SHOW_FRIENDS_FROM_PROFILE, current.getContext().getResources().getIdentifier("ThemeBeintoo", "style", current.getContext().getPackageName()));
				friend.show();
			}else if(v.getId() == 2){
				Friends friend = new Friends(getContext(), null, Friends.FRIENDSHIP_REQUESTS, current.getContext().getResources().getIdentifier("ThemeBeintoo", "style", current.getContext().getPackageName()));
				friend.show();
			}else if(v.getId() == 3){
				UIhandler.sendEmptyMessage(OPEN_RTAF);
			}
		}else if(whichSection == FRIENDSHIP_REQUESTS){
			showFriendShipDialog(friends[v.getId()].getId(),v);
		}else if(whichSection == FIND_FRIENDS){
			addFriendDialog(friends[v.getId()].getId(), friends[v.getId()].getNickname());
		}
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  if(msg.what == LOAD_TABLE)
				  loadFriendsTable();
			  else if (msg.what == SHOW_EMPTY){
				  TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
				  table.removeAllViews();
				  TextView t = new TextView(current.getContext());
				  t.setText(msg.getData().getString("message"));
				  t.setTextColor(Color.GRAY);
				  t.setPadding(15,15,0,0);
				  table.addView(t);
			  }else if(msg.what == SHOW_MESSAGE){
				  MessageDisplayer.showMessage(getContext(), msg.getData().getString("message"), Gravity.BOTTOM);
			  }else if(msg.what == OPEN_RTAF){
				  Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
				  StringBuilder url = new StringBuilder("http://sdk-mobile.beintoo.com/rtaf/?apikey=");
				  url.append(DeveloperConfiguration.apiKey);
				  url.append("&user_ext=");
				  url.append(p.getUser().getId());
				  url.append("#main");
				  SmartWebUi swu = new SmartWebUi(current.getContext(), url.toString());
				  swu.show();				    
			  }		  
				  
			  super.handleMessage(msg);
		  }
	};
}
