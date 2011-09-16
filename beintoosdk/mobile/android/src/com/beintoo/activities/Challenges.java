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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.MessageDisplayer;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.wrappers.Challenge;
import com.beintoo.wrappers.Player;
import com.google.beintoogson.Gson;

public class Challenges extends Dialog implements OnClickListener{
	static Dialog current;
	Challenge [] challenge;
	private final int PENDING = 0;
	private final int ACCEPTED = 1;
	private final int ENDED = 2;
	private final int CONNECTION_ERROR = 3;
	
	int CURRENT_SECTION = 0;
	final double ratio;
	public Challenges(Context ctx) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));		
		setContentView(ctx.getResources().getIdentifier("challenges", "layout", ctx.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		current = this;
		// SET TITLE
		TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		t.setText(current.getContext().getResources().getIdentifier("challenges", "string", current.getContext().getPackageName()));

		// GETTING DENSITY PIXELS RATIO
		ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 40;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		try{
			showLoading();
			startLoading();
		}catch (Exception e){e.printStackTrace();}
		
		
		final BeButton b = new BeButton(ctx);
		
		// PENDING CHALLENGES BUTTON
		final Button pending = (Button) findViewById(current.getContext().getResources().getIdentifier("pendingchall", "id", current.getContext().getPackageName()));		
		pending.setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));	
		pending.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				resetButtons();
				pending.setBackgroundDrawable(b.setPressedBackg(
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
				showLoading();
				new Thread(new Runnable(){      
            		public void run(){
            			try{ 
							BeintooUser newuser = new BeintooUser();            				
							// GET THE CURRENT LOGGED PLAYER
							Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));
							challenge = newuser.challengeShow(p.getUser().getId(), "TO_BE_ACCEPTED");				
							UIhandler.sendEmptyMessage(PENDING);
						}catch (Exception e){
							e.printStackTrace();
							manageConnectionException();
						}
					}
				}).start();
			}
		});
		
		// ACCEPTED CHALLENGES BUTTON
		final Button accepted = (Button) findViewById(current.getContext().getResources().getIdentifier("acceptedchall", "id", current.getContext().getPackageName()));		
		accepted.setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
		accepted.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				resetButtons();
				accepted.setBackgroundDrawable(b.setPressedBackg(
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
				showLoading();
				new Thread(new Runnable(){      
            		public void run(){
            			try{ 
							BeintooUser newuser = new BeintooUser();            				
							// GET THE CURRENT LOGGED IN PLAYER
							Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));
							challenge = newuser.challengeShow(p.getUser().getId(), "STARTED");				
							UIhandler.sendEmptyMessage(ACCEPTED);
            			}catch (Exception e){
            				e.printStackTrace();
            				manageConnectionException();
            			}
            		}
				}).start();	
			}
		});
		
		
		// ENDED CHALLENGES BUTTON
		final Button ended = (Button) findViewById(current.getContext().getResources().getIdentifier("endedchall", "id", current.getContext().getPackageName()));		
		ended.setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
		ended.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				resetButtons();
				ended.setBackgroundDrawable(b.setPressedBackg(
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
				
				//final ProgressDialog  dialog = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("loading", "string", current.getContext().getPackageName())),true);
				showLoading();
				new Thread(new Runnable(){      
            		public void run(){
            			try{ 
							BeintooUser newuser = new BeintooUser();            				
							// GET THE CURRENT LOGGED IN PLAYER
							Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));
							challenge = newuser.challengeShow(p.getUser().getId(), "ENDED");				
							UIhandler.sendEmptyMessage(ENDED);
            			}catch (Exception e){
            				e.printStackTrace();
            				manageConnectionException();
            			}
            			//dialog.dismiss();
            		}
				}).start();
			}
		});
	}
	
	public void startLoading (){
		new Thread(new Runnable(){      
    		public void run(){
    			try{ 
					BeintooUser newuser = new BeintooUser();            				
					// GET THE CURRENT LOGGED PLAYER
					Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));
					challenge = newuser.challengeShow(p.getUser().getId(), "TO_BE_ACCEPTED");
					UIhandler.sendEmptyMessage(PENDING);
    			}catch (Exception e){
    				e.printStackTrace();
    				manageConnectionException();
    			}
    		}
		}).start();
	}
	
	private void loadChallenges(int section){
		CURRENT_SECTION = section;
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.setColumnStretchable(1, true);
		table.removeAllViews();
		Player p = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", getContext()), Player.class);
			
		int count = 0;
		final ArrayList<View> rowList = new ArrayList<View>();
	
	    for (int i = 0; i < challenge.length; i++){
	    	final LoaderImageView image;
	    	String nick;
	    	String contest;
	    	
    		if(p.getUser().getId().equals(challenge[i].getPlayerFrom().getUser().getId())){
    			//image = new LoaderImageView(getContext(), challenge[i].getPlayerTo().getUser().getUsersmallimg());
    			image = new LoaderImageView(getContext(),challenge[i].getPlayerTo().getUser().getUserimg(),(int)(ratio * 70),(int)(ratio * 70));
    			nick = getContext().getString(current.getContext().getResources().getIdentifier("challFromTo", "string", current.getContext().getPackageName()))+challenge[i].getPlayerTo().getUser().getNickname();
    			contest = challenge[i].getContest().getName();
    		}else{
    			//image = new LoaderImageView(getContext(), challenge[i].getPlayerFrom().getUser().getUsersmallimg());
    			image = new LoaderImageView(getContext(),challenge[i].getPlayerFrom().getUser().getUserimg(),(int)(ratio * 70),(int)(ratio * 70));
    			nick = getContext().getString(current.getContext().getResources().getIdentifier("challFrom", "string", current.getContext().getPackageName()))+challenge[i].getPlayerFrom().getUser().getNickname()+
    			getContext().getString(current.getContext().getResources().getIdentifier("challYou", "string", current.getContext().getPackageName()));
    			contest = challenge[i].getContest().getName();    			
    		}
    		
    		TableRow row = createRow(image, nick,contest, table.getContext());
    		    		
    		if(section == PENDING){
	    		if(!p.getUser().getId().equals(challenge[i].getPlayerFrom().getUser().getId())){
	    			row.setOnClickListener(this);
	    		}
    		}else{ row.setOnClickListener(this); }
    		
			row.setId(count);
			rowList.add(row);
			
			BeButton b = new BeButton(getContext());
			if(i % 2 == 0)
	    		row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 90),BDrawableGradient.LIGHT_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 90),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 90),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			else
				row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 90),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 90),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 90),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			
			View spacer = createSpacer(getContext(),1,1);
			spacer.setId(-100);
			rowList.add(spacer);
			View spacer2 = createSpacer(getContext(),2,1);
			spacer2.setId(-100);
			rowList.add(spacer2);
			count++;
	    }
	
	    for (View row : rowList) {
	
		      table.addView(row);
		}
	}
	
	
	private TableRow createRow(View image, String name, String contest, Context activity) {
		  TableRow row = new TableRow(activity);
		  row.setGravity(Gravity.CENTER);
		  
		  image.setPadding(15, 4, 10, 4);		  
		  ((LinearLayout) image).setGravity(Gravity.LEFT);
		  row.addView(image);
		  
		  LinearLayout main = new LinearLayout(row.getContext());
		  main.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  main.setOrientation(LinearLayout.VERTICAL);
		  main.setGravity(Gravity.CENTER_VERTICAL);
		  
		  // NICKNAME TEXTVIEW
		  TextView nameView = new TextView(activity);		  
		  if(contest.length() > 26)
			  nameView.setText(contest.substring(0, 23)+"...");
		  else
			  nameView.setText(contest);
		  
		  nameView.setPadding(0, 0, 0, 0);
		  nameView.setTextColor(Color.parseColor("#545859"));
		  nameView.setTextSize(16);
		  nameView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  
		  // CONTEST NAME TEXTVIEW
		  TextView contestView = new TextView(activity);		
		  contestView.setText(name);		  
		  contestView.setPadding(0, 0, 0, 0);
		  contestView.setTextColor(Color.parseColor("#787A77"));
		  contestView.setTextSize(14);
		  contestView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  	
		  main.addView(nameView);
		  main.addView(contestView);		  
		  row.addView(main,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int)(ratio * 90)));

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

	public void onClick(View v) {
		if(v.getId()!=-100){ // REMOVE THE SPACER
			if(CURRENT_SECTION == PENDING)
				this.respondDialog(v);
			else if(CURRENT_SECTION == ACCEPTED){
				ChallengeOverview challengeShow = new ChallengeOverview(getContext(),challenge[v.getId()], ACCEPTED);
				challengeShow.show();
			}else if(CURRENT_SECTION == ENDED){
				ChallengeOverview challengeShow = new ChallengeOverview(getContext(),challenge[v.getId()], ENDED);
				challengeShow.show();
			}
		}	
	}
	
	private void respondDialog (final View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage(getContext().getString(current.getContext().getResources().getIdentifier("challAccept", "string", current.getContext().getPackageName()))+challenge[v.getId()].getPrice().intValue()+" BeDollars?")
		       .setCancelable(false)
		       .setPositiveButton(getContext().getString(current.getContext().getResources().getIdentifier("yes", "string", current.getContext().getPackageName())), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {		 
		        	   respondChallenge(challenge[v.getId()].getPlayerTo().getUser().getId(),
		        			   challenge[v.getId()].getPlayerFrom().getUser().getId(),"ACCEPT",
		        			   challenge[v.getId()].getContest().getCodeID());      	   
		        	   dialog.dismiss();
		        	   MessageDisplayer.showMessage(getContext(), getContext().getString(current.getContext().getResources().getIdentifier("challAccepted", "string", current.getContext().getPackageName())),Gravity.BOTTOM);
		        	   
		        	   TableLayout table = (TableLayout) v.getParent();
		        	   table.removeView(v);
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   respondChallenge(challenge[v.getId()].getPlayerTo().getUser().getId(),
		        			   challenge[v.getId()].getPlayerFrom().getUser().getId(),"REFUSE",
		        			   challenge[v.getId()].getContest().getCodeID());
		        	   dialog.dismiss();
		        	   MessageDisplayer.showMessage(getContext(), getContext().getString(current.getContext().getResources().getIdentifier("challRefused", "string", current.getContext().getPackageName())),Gravity.BOTTOM);

		        	   TableLayout table = (TableLayout) v.getParent();
		        	   table.removeView(v);
		           }
		       }).setNeutralButton("Cancel",  new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void respondChallenge (final String userExtFrom, final String userExtTo, final String action, final String codeID){
		new Thread(new Runnable(){      
    		public void run(){
    			try{             				
    				BeintooUser user = new BeintooUser();
    				user.challenge(userExtFrom, userExtTo, action, codeID); 
    			}catch (Exception e){
    				e.printStackTrace();
    			}
    			
    		}
		}).start();
	}
	
	@SuppressWarnings("unused")
	private String getStatus (String status){
		if(status.equals("TO_BE_ACCEPTED"))
			return "Pending";
		else if(status.equals("STARTED"))
			return "Ongoing";
		else if(status.equals("ENDED"))
			return "Over";
		else
			return "";
	}
	
	private void loadEmptySection (int section){
		TextView noChallenge = new TextView(getContext());
		if(section == PENDING)
			noChallenge.setText(getContext().getString(current.getContext().getResources().getIdentifier("challNoPendingC", "string", current.getContext().getPackageName())));
		else if(section == ACCEPTED)
			noChallenge.setText(getContext().getString(current.getContext().getResources().getIdentifier("challNoAccepted", "string", current.getContext().getPackageName())));
		else if(section == ENDED)
			noChallenge.setText(getContext().getString(current.getContext().getResources().getIdentifier("challNoEnded", "string", current.getContext().getPackageName())));
		
		noChallenge.setTextColor(Color.GRAY);
		noChallenge.setPadding(15,15,0,0);
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();
		table.addView(noChallenge);
	}
	
	private void resetButtons(){
		BeButton b = new BeButton(getContext());
		findViewById(current.getContext().getResources().getIdentifier("pendingchall", "id", current.getContext().getPackageName())).setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
		findViewById(current.getContext().getResources().getIdentifier("acceptedchall", "id", current.getContext().getPackageName())).setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));
		findViewById(current.getContext().getResources().getIdentifier("endedchall", "id", current.getContext().getPackageName())).setBackgroundDrawable(b.setPressedBackg(
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.LIGHT_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT),
				new BDrawableGradient(0,(int) (ratio*35),BDrawableGradient.HIGH_GRAY_GRADIENT)));	
		
		
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
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();
		table.addView(row);
	}
	
	private void manageConnectionException (){
		UIhandler.sendEmptyMessage(3);		
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {			  
			  if(msg.what == PENDING){
				  if(challenge.length > 0)
						loadChallenges(PENDING);
				  else { // NO CHALLENGE REQUEST
						loadEmptySection(PENDING);
				  }
			  }else if(msg.what == ACCEPTED){
				  if(challenge.length > 0)
						loadChallenges(ACCEPTED);
				  else { // NO CHALLENGE REQUEST
						loadEmptySection(ACCEPTED);
				  }
			  }else if (msg.what == ENDED){
				  if(challenge.length > 0)
						loadChallenges(ENDED);
				  else { // NO CHALLENGE REQUEST
						loadEmptySection(ENDED);
				  }
			  }else if(msg.what == CONNECTION_ERROR){
				  TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
				  table.removeAllViews();
				  ErrorDisplayer.showConnectionError(ErrorDisplayer.CONN_ERROR , current.getContext(), null);
			  }
			  super.handleMessage(msg);
		  }
	};
}
