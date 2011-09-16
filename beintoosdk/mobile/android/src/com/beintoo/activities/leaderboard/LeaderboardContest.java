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
package com.beintoo.activities.leaderboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.beintoo.beintoosdk.BeintooAlliances;
import com.beintoo.beintoosdk.BeintooApp;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.LeaderboardContainer;
import com.beintoo.wrappers.Player;

public class LeaderboardContest extends Dialog implements OnClickListener{
	private Dialog current;
	private Context currentContext;
	private final double ratio;
	private Map<String, LeaderboardContainer> leader;
	private Player player = null;
	private String kind = null;
	public LeaderboardContest(Context ctx) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));
		setContentView(ctx.getResources().getIdentifier("contestselection", "layout", ctx.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		current = this;
		currentContext = ctx;
		
		// SET TITLE
		TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		t.setText(current.getContext().getResources().getIdentifier("leaderboard", "string", current.getContext().getPackageName()));

		// GETTING DENSITY PIXELS RATIO
		ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 40;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));

		try {
			player = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", currentContext));
		}catch (Exception e){ e.printStackTrace(); }
		
		try {			
			showLoading();
			startLoading();
		}catch(Exception e){e.printStackTrace(); ErrorDisplayer.showConnectionError(ErrorDisplayer.CONN_ERROR , ctx,e);}	
	 
		final BeButton b = new BeButton(ctx);
		Button general = (Button) findViewById(current.getContext().getResources().getIdentifier("generaleader", "id", current.getContext().getPackageName()));
		
		general.setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));				
		general.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				if(Beintoo.isLogged(currentContext)){
					// FRIEND FILTER
					final ImageButton friends = (ImageButton) findViewById(current.getContext().getResources().getIdentifier("onlyfriends", "id", current.getContext().getPackageName()));
					findViewById(current.getContext().getResources().getIdentifier("onlyf", "id", current.getContext().getPackageName())).setVisibility(LinearLayout.VISIBLE);
					friends.setImageResource(current.getContext().getResources().getIdentifier("noselected", "drawable", current.getContext().getPackageName()));
					friends.setTag("notselected");
				}
				resetButtons();
				v.setBackgroundDrawable(b.setPressedBackg(
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));				
				showLoading();
				new Thread(new Runnable(){      
            		public void run(){
            			try{ 
							BeintooApp app = new BeintooApp();
							String userExt = null;
							kind = null;
							if(player != null && player.getUser() != null)
								userExt = player.getUser().getId();
							leader = app.getLeaderboard(userExt, null, null, 0, 20);
							UIhandler.sendEmptyMessage(0);
            			}catch (Exception e){
            				e.printStackTrace();
            				manageConnectionException();
            			}
            		}
				}).start();
			}
        });
		
		Button alliances = (Button) findViewById(current.getContext().getResources().getIdentifier("friendsleader", "id", current.getContext().getPackageName()));
		alliances.setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
		alliances.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {					
				// FRIEND FILTER
				findViewById(current.getContext().getResources().getIdentifier("onlyf", "id", current.getContext().getPackageName())).setVisibility(LinearLayout.GONE);
				resetButtons();
				v.setBackgroundDrawable(b.setPressedBackg(
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));				
				
				showLoading();
				new Thread(new Runnable(){      
            		public void run(){
            			try{ 
            				BeintooAlliances ba = new BeintooAlliances();
            				String allianceExtId = null;
            				if(player != null && player.getAlliance() != null)
            					allianceExtId = player.getAlliance().getId();
            				leader = ba.getLeaderboard(allianceExtId, null, null, 0, 20, null);
            				kind = "ALLIANCES";
            				UIhandler.sendEmptyMessage(0);
							/*BeintooApp app = new BeintooApp();												
							String userExt = null;
							kind = "FRIENDS";
							if(player != null && player.getUser() != null)
								userExt = player.getUser().getId();
							leader = app.getLeaderboard(userExt, kind, null, 0, 20);							
							UIhandler.sendEmptyMessage(0); */ 
            			}catch (Exception e){
            				e.printStackTrace();
            				manageConnectionException();
            			}
            		}
				}).start();
			}
        });
		
		if(Beintoo.isLogged(ctx)){				
			findViewById(current.getContext().getResources().getIdentifier("onlyf", "id", current.getContext().getPackageName())).setBackgroundDrawable(new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT));
			final ImageButton friends = (ImageButton) findViewById(current.getContext().getResources().getIdentifier("onlyfriends", "id", current.getContext().getPackageName()));
			friends.setBackgroundDrawable(b.setPressedBackg(
					new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
					new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
					new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			friends.setOnClickListener(new ImageButton.OnClickListener(){
				public void onClick(View v) {
					
					if(v.getTag().equals("notselected")){
						friends.setImageResource(current.getContext().getResources().getIdentifier("selected", "drawable", current.getContext().getPackageName()));
						v.setTag("selected");
						kind = "FRIENDS";
					}else if(v.getTag().equals("selected")){
						friends.setImageResource(current.getContext().getResources().getIdentifier("noselected", "drawable", current.getContext().getPackageName()));
						v.setTag("notselected");
						kind = null;
					}
					  
					showLoading();
					new Thread(new Runnable(){      
	            		public void run(){
	            			try{ 
								BeintooApp app = new BeintooApp();												
								String userExt = null;
								
								if(player != null && player.getUser() != null)
									userExt = player.getUser().getId();
								leader = app.getLeaderboard(userExt, kind, null, 0, 20);							
								UIhandler.sendEmptyMessage(0); 
	            			}catch (Exception e){
	            				e.printStackTrace();
	            				manageConnectionException();
	            			}
	            		}
					}).start();
				}
	        });
		}else { // IF THE USER IS NOT LOGGED HIDE ALL AND FRIENDS BUTTONS
			//findViewById(current.getContext().getResources().getIdentifier("buttonsll", "id", current.getContext().getPackageName())).setVisibility(View.GONE);
			findViewById(current.getContext().getResources().getIdentifier("onlyf", "id", current.getContext().getPackageName())).setVisibility(View.GONE);
			//findViewById(current.getContext().getResources().getIdentifier("spacer", "id", current.getContext().getPackageName())).setVisibility(View.GONE);
		}
	}
	
	public void startLoading (){
		new Thread(new Runnable(){      
    		public void run(){
    			try{ 
    				BeintooApp app = new BeintooApp();							
    				String userExt = null;
					if(player != null && player.getUser() != null)
						userExt = player.getUser().getId();
					leader = app.getLeaderboard(userExt, null, null, 0, 20);					
					UIhandler.sendEmptyMessage(0);
    			}catch (Exception e){
    				manageConnectionException();
    				e.printStackTrace();
    			}
    		}
		}).start();
	}
	
	public void loadContestTable(){
		try {
			TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("tablecontest", "id", current.getContext().getPackageName()));
			table.setColumnStretchable(1, true);
			table.removeAllViews();
			
			BeButton b = new BeButton(getContext());
			
			final ArrayList<View> rowList = new ArrayList<View>();
			Iterator<?> it = leader.entrySet().iterator();
			int count = 0;
		    while (it.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String, LeaderboardContainer> pairs = (Map.Entry<String, LeaderboardContainer>) it.next();		    	
		    	LeaderboardContainer arr = pairs.getValue();			    	
		    	TableRow row = createRow(arr.getContest().getName(),getContext());
		    	if(count % 2 == 0)
		    		row.setBackgroundDrawable(b.setPressedBackg(
				    		new BDrawableGradient(0,(int)(ratio * 35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
							new BDrawableGradient(0,(int)(ratio * 35),BDrawableGradient.HIGH_GRAY_GRADIENT),
							new BDrawableGradient(0,(int)(ratio * 35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
				else
					row.setBackgroundDrawable(b.setPressedBackg(
				    		new BDrawableGradient(0,(int)(ratio * 35),BDrawableGradient.GRAY_GRADIENT),
							new BDrawableGradient(0,(int)(ratio * 35),BDrawableGradient.HIGH_GRAY_GRADIENT),
							new BDrawableGradient(0,(int)(ratio * 35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
				
		    	TableLayout.LayoutParams tableRowParams=
	    		  new TableLayout.LayoutParams
	    		  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
	    		row.setLayoutParams(tableRowParams);
		    	row.setId(count);
				rowList.add(row);
				View spacer = createSpacer(getContext(),1,1);
				spacer.setId(-100);
				rowList.add(spacer);
				View spacer2 = createSpacer(getContext(),2,1);
				spacer2.setId(-100);
				rowList.add(spacer2);
		    	count++;	
	    	
		    }
		    
		    for (View row : rowList) {
			      row.setPadding(0, 0, 0, 0);	      
			      //row.setBackgroundColor(Color.argb(200, 51, 51, 51));
			      if(row.getId() != -100) // IF IS NOT A SPACER IT'S CLICKABLE
			    	  row.setOnClickListener(this);
			      table.addView(row);
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	public TableRow createRow(String txt, Context activity) {
		  TableRow row = new TableRow(activity);
		  row.setGravity(Gravity.CENTER_VERTICAL);
		  
		  TextView text = new TextView(activity);
		  text.setText(txt);
		  text.setPadding((int)(ratio*10), 0, 0, 0);
		  text.setTextColor(Color.parseColor("#545859"));
		  text.setTextSize(14);
		  text.setGravity(Gravity.CENTER_VERTICAL);
		  text.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				  RelativeLayout.LayoutParams.FILL_PARENT));
		  
		  RelativeLayout main = new RelativeLayout(row.getContext());
		  main.setGravity(Gravity.CENTER_VERTICAL);
		  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				  RelativeLayout.LayoutParams.FILL_PARENT);
		  params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		  main.setLayoutParams(params);
		  
		  ImageView arrow = new ImageView(activity);
		  arrow.setImageResource(current.getContext().getResources().getIdentifier("barrow", "drawable", current.getContext().getPackageName()));
		  arrow.setLayoutParams(params);
		  arrow.setPadding(0,0,(int)(ratio * 5),0);
		  
		  main.addView(arrow, params);
		  main.addView(text);
		  
		  
		  row.addView(main,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int)(ratio * 35)));
		  
		   
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
		
	public void resetButtons(){
		Button general = (Button) findViewById(current.getContext().getResources().getIdentifier("generaleader", "id", current.getContext().getPackageName()));		
		general.setBackgroundDrawable(new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT));	
		Button friends = (Button) findViewById(current.getContext().getResources().getIdentifier("friendsleader", "id", current.getContext().getPackageName()));
		friends.setBackgroundDrawable(new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT));
	}
	
	// CALLED WHEN THE USER SELECT A CONTEST
	public void onClick(View v) {
		final int selectedRow = v.getId();
		PreferencesHandler.saveString("selectedContest", ""+selectedRow, currentContext);
    	UIhandler.sendEmptyMessage(1);    				
	}
	
	private void showLoading (){
		ProgressBar pb = new ProgressBar(getContext());
		pb.setIndeterminateDrawable(getContext().getResources().getDrawable(current.getContext().getResources().getIdentifier("progress", "drawable", current.getContext().getPackageName())));
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int)(100*ratio), 0, 0);		
		TableRow row = new TableRow(getContext());
		row.setLayoutParams(params);
		row.setGravity(Gravity.CENTER);
		row.addView(pb);
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("tablecontest", "id", current.getContext().getPackageName()));
		table.setColumnStretchable(0, false);
		table.removeAllViews();
		table.addView(row);
	}
	
	private void manageConnectionException (){
		UIhandler.sendEmptyMessage(3);		
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {			  
			  if(msg.what == 0)
				  loadContestTable();
			  else if(msg.what == 1){
				  Leaderboard leaderboard = new Leaderboard(currentContext,leader, kind);
				  leaderboard.show();
			  }else if(msg.what == 3){
				  TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("tablecontest", "id", current.getContext().getPackageName()));
				  table.removeAllViews();
				  ErrorDisplayer.showConnectionError(ErrorDisplayer.CONN_ERROR , current.getContext(), null);
			  }
			  super.handleMessage(msg);
		  }
	};
	
}
