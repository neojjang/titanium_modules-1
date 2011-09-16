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


import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.wrappers.Challenge;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChallengeOverview extends Dialog{
	Challenge reqChallenge = null;
	final int ACCEPTED = 1;
	final int ENDED = 2;
	
	public ChallengeOverview(Context context, Challenge requested, int typeOfChallenge) {
		super(context);
		reqChallenge = requested;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(context.getResources().getIdentifier("challengeoverview", "layout", context.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// GETTING DENSITY PIXELS RATIO
		double ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 40;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(context.getResources().getIdentifier("beintoobarsmall", "id", context.getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.LIGHT_GRAY_GRADIENT));
		
		if(reqChallenge != null){
			try{
				TextView contest = (TextView) findViewById(context.getResources().getIdentifier("contname", "id", context.getPackageName()));
				contest.setText(reqChallenge.getContest().getName());
				
				TextView status = (TextView) findViewById(context.getResources().getIdentifier("status", "id", context.getPackageName()));
				status.setText(reqChallenge.getStatus());
				
				LinearLayout frompict = (LinearLayout) findViewById(context.getResources().getIdentifier("frompict", "id", context.getPackageName()));
				LoaderImageView from = new LoaderImageView(getContext(),reqChallenge.getPlayerFrom().getUser().getUserimg(),70,70);
				
				frompict.addView(from);
				
				LinearLayout topict = (LinearLayout) findViewById(context.getResources().getIdentifier("topict", "id", context.getPackageName()));
				LoaderImageView to = new LoaderImageView(getContext(),reqChallenge.getPlayerTo().getUser().getUserimg(),70,70);
				topict.addView(to);
				 
				TextView fromNick = (TextView) findViewById(context.getResources().getIdentifier("fromnick", "id", context.getPackageName()));
				fromNick.setText(reqChallenge.getPlayerFrom().getUser().getNickname());
				TextView toNick = (TextView) findViewById(context.getResources().getIdentifier("tonick", "id", context.getPackageName()));
				toNick.setText(reqChallenge.getPlayerTo().getUser().getNickname());
				 
				TextView fromScore = (TextView) findViewById(context.getResources().getIdentifier("scorefrom", "id", context.getPackageName()));
				fromScore.setText(getContext().getString(context.getResources().getIdentifier("leadScore", "string", context.getPackageName()))+reqChallenge.getPlayerFromScore().toString());				
				TextView toScore = (TextView) findViewById(context.getResources().getIdentifier("scoreto", "id", context.getPackageName()));
				toScore.setText(getContext().getString(context.getResources().getIdentifier("leadScore", "string", context.getPackageName()))+reqChallenge.getPlayerToScore().toString());
				
				SimpleDateFormat curFormater = new SimpleDateFormat("d-MMM-y HH:mm:ss", Locale.ENGLISH); 
				curFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
				
				Date startDate = curFormater.parse(reqChallenge.getStartdate());
				Date endDate = curFormater.parse(reqChallenge.getEnddate());
				curFormater.setTimeZone(TimeZone.getDefault());
				TextView sDate = (TextView) findViewById(context.getResources().getIdentifier("startdate", "id", context.getPackageName()));
				sDate.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT,Locale.getDefault()).format(startDate));
				TextView eDate = (TextView) findViewById(context.getResources().getIdentifier("enddate", "id", context.getPackageName()));
				eDate.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT,Locale.getDefault()).format(endDate));
				
				if(typeOfChallenge == ENDED){	
					TextView winner = (TextView) findViewById(context.getResources().getIdentifier("winner", "id", context.getPackageName()));
					winner.setText(reqChallenge.getWinner().getUser().getNickname());
					TextView prize = (TextView) findViewById(context.getResources().getIdentifier("prize", "id", context.getPackageName()));
					prize.setText(reqChallenge.getPrize().toString());
				}else {
					LinearLayout win = (LinearLayout) findViewById(context.getResources().getIdentifier("winll", "id", context.getPackageName()));
					win.setVisibility(LinearLayout.INVISIBLE);
				}
			}catch(Exception e){e.printStackTrace();}
		}else{ // dismiss if error
			this.dismiss();
		}
	}
}
