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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import com.beintoo.beintoosdk.BeintooMessages;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.UsersMessage;
import android.view.View.OnClickListener;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MessagesList extends Dialog implements OnClickListener{
	MessagesList current;
	private double ratio;	
	private List<UsersMessage> messages;
	private int currentStartPosition = 0;
	private int totalUserMessages = 0;
	private int totalMessageDisplayed = 0;
	
	private final int FIRST_LOADING = 0;
	private final int NEXT_LOADING = 1;
	private final int CONNECTION_ERROR = 3;
	
	private Player p; 
	
	public MessagesList(Context context) {
		super(context, context.getResources().getIdentifier("ThemeBeintoo", "style", context.getPackageName()));
		current = this;
		
		setContentView(current.getContext().getResources().getIdentifier("messageslist", "layout", current.getContext().getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// SET TITLE
		TextView t = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		t.setText(current.getContext().getResources().getIdentifier("messages", "string", current.getContext().getPackageName()));
		
		// GETTING DENSITY PIXELS RATIO
		ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 40;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		Button newmessage = (Button) findViewById(current.getContext().getResources().getIdentifier("newmessage", "id", current.getContext().getPackageName()));
	    BeButton b = new BeButton(context);
	    newmessage.setShadowLayer(0.1f, 0, -2.0f, Color.BLACK);
	    newmessage.setBackgroundDrawable(
	    		b.setPressedBackg(
			    		new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT),
						new BDrawableGradient(0,(int) (ratio*50),BDrawableGradient.BLU_ROLL_BUTTON_GRADIENT)));			    	 
	    newmessage.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				MessagesWrite mw = new MessagesWrite(current.getContext());
				mw.show();
			}
		}); 
	     
	    try {
	    	p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", getContext()));	    
	    	totalUserMessages = p.getUser().getMessages();
	    }catch (Exception e){e.printStackTrace();}
	    startFirstLoading();
	} 
	 
	public void startFirstLoading (){
		try{
			messages = new ArrayList<UsersMessage>();
			currentStartPosition = 0;
			totalMessageDisplayed = 0;
			TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
			table.removeAllViews();
			showLoading(FIRST_LOADING);
			loadData(currentStartPosition, FIRST_LOADING);
		}catch (Exception e){e.printStackTrace();}		
	}
	
	private void loadData (final int start, final int action) {
		new Thread(new Runnable(){      
    		public void run(){
    			try{ 
					BeintooMessages bm = new BeintooMessages();            				
					// GET THE CURRENT LOGGED PLAYER
					List<UsersMessage> tmp = bm.getUserMessages(p.getUser().getId(), null,start, 10);
					messages.addAll(tmp);	
					UIhandler.sendEmptyMessage(action);
    			}catch (Exception e){
    				e.printStackTrace();
    				manageConnectionException ();
    			}
    		}
		}).start();
	}
	
	private void loadTableRows () {
		try{
			TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));	
			final ArrayList<View> rowList = new ArrayList<View>();
			addSpacerLines(rowList);
			if(messages.size() > 0){
			    for (int i = currentStartPosition; i < messages.size(); i++){
			    	final LoaderImageView image;
			    	String nick;
			    	String date;
			    	String msgtext;
			    	
			    	if(messages.get(i).getUserFrom() != null){
			    		image = new LoaderImageView(getContext(),messages.get(i).getUserFrom().getUserimg(),(int)(ratio * 65),(int)(ratio * 65));
						nick = "<b>"+getContext().getString(current.getContext().getResources().getIdentifier("messagefrom", "string", current.getContext().getPackageName()))+"</b> "+messages.get(i).getUserFrom().getNickname();						
			    	}else{
			    		image = new LoaderImageView(getContext(),messages.get(i).getApp().getImageUrl(),(int)(ratio * 65),(int)(ratio * 65));
						nick = "<b>"+getContext().getString(current.getContext().getResources().getIdentifier("messagefrom", "string", current.getContext().getPackageName()))+"</b> "+messages.get(i).getApp().getName();
			    	}
					
					SimpleDateFormat curFormater = new SimpleDateFormat("d-MMM-y HH:mm:ss", Locale.ENGLISH); 
					curFormater.setTimeZone(TimeZone.getTimeZone("GMT"));			
					Date msgDate = curFormater.parse(messages.get(i).getCreationdate());			
					curFormater.setTimeZone(TimeZone.getDefault());			
					date = "<b>"+getContext().getString(current.getContext().getResources().getIdentifier("messagedate", "string", current.getContext().getPackageName()))+"</b> "+(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT,Locale.getDefault()).format(msgDate));
					
					msgtext = messages.get(i).getText();
					msgtext = msgtext.replaceAll("\\<.*?>","");
					msgtext = msgtext.replaceAll("\\&.*?\\;", "");
					
					boolean unread = false;
					if(messages.get(i).getStatus().equals("UNREAD")) unread = true;
					
		    		
		    		TableRow row = createRow(image, nick,date,msgtext, unread, table.getContext());
		    		    		
					row.setId(i);
					rowList.add(row);
					row.setOnClickListener(this);
					
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
					
					addSpacerLines(rowList);
					
				totalMessageDisplayed++;	
			    }		    
			    
			    if(totalMessageDisplayed < totalUserMessages){
			    	loadMore(table.getContext(),rowList);
			    }
			    
			    for (View row : rowList) {		 
				      table.addView(row);
				}

			}else {
				TextView noMsg = new TextView(getContext());
				noMsg.setText(getContext().getString(current.getContext().getResources().getIdentifier("messageempty", "string", current.getContext().getPackageName())));
				noMsg.setTextColor(Color.GRAY);
				noMsg.setGravity(Gravity.CENTER);
				noMsg.setPadding(0,(int)(ratio*50),0,0);						
				table.addView(noMsg);
			}
		}catch (Exception e){e.printStackTrace();}
	}
	
	
	private TableRow createRow(View image, String name, String date, String text, boolean isUnread, Context activity) {
		  TableRow row = new TableRow(activity);
		  row.setGravity(Gravity.CENTER_VERTICAL);
		  
		  image.setPadding(15, 4, 10, 4);		  
		  ((LinearLayout) image).setGravity(Gravity.LEFT);
		  
		  LinearLayout main = new LinearLayout(row.getContext());
		  main.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  main.setOrientation(LinearLayout.HORIZONTAL);
		  main.setGravity(Gravity.CENTER_VERTICAL);
		  
		  LinearLayout fromData = new LinearLayout(row.getContext());
		  fromData.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  fromData.setOrientation(LinearLayout.VERTICAL);
		  fromData.setGravity(Gravity.CENTER_VERTICAL);
		  
		  
		  TextView nameView = new TextView(activity);		  
		  nameView.setText(Html.fromHtml(name));		  
		  nameView.setPadding(0, 0, 0, 0);
		  nameView.setTextColor(Color.parseColor("#545859"));
		  nameView.setTextSize(14);
		  nameView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  if(isUnread){
			  nameView.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
		  }
		  
		  TextView dateView = new TextView(activity);		
		  dateView.setText(Html.fromHtml(date));		  
		  dateView.setPadding(0, 0, 0, 0);
		  dateView.setTextColor(Color.parseColor("#545859"));
		  dateView.setTextSize(14);
		  dateView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  if(isUnread){
			  dateView.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
		  }
		  
		  TextView textView = new TextView(activity);
		  InputFilter[] fArray = new InputFilter[1];
		  fArray[0] = new InputFilter.LengthFilter(32);
		  textView.setFilters(fArray);
		  textView.setText(text);
		  textView.setPadding(0, 10, 0, 0);
		  textView.setTextColor(Color.parseColor("#787A77"));
		  textView.setTextSize(14);
		  textView.setSingleLine(true);
		  textView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  if(isUnread){
			  textView.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
			  textView.setTextColor(Color.BLACK);
		  }
		  
		  fromData.addView(nameView);
		  fromData.addView(dateView);
		  fromData.addView(textView);
		 
		  main.addView(image);
		  main.addView(fromData);		  
		  row.addView(main,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int)(ratio * 90)));

		  return row;
	}
	
	private TableRow loadMore(Context context, ArrayList<View> views) {
		TableRow row = new TableRow(context);
		row.setGravity(Gravity.CENTER);
		
		TextView moreView = new TextView(context);		
		moreView.setText(context.getString(current.getContext().getResources().getIdentifier("messageloadmore", "string", current.getContext().getPackageName())));
		moreView.setTextColor(Color.parseColor("#545859"));
		moreView.setTextSize(14);
		moreView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		moreView.setGravity(Gravity.CENTER);
		
		row.addView(moreView,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int)(ratio * 90)));
		row.setId(-200);
		
		row.setOnClickListener(this);
		views.add(row);
		
		addSpacerLines(views);
		
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
	
	private void addSpacerLines (ArrayList<View> views){
		View spacer = createSpacer(getContext(),1,1);
		spacer.setId(-100);
		views.add(spacer);
		View spacer2 = createSpacer(getContext(),2,1);
		spacer2.setId(-100);
		views.add(spacer2);
	}
	
	private void showLoading (final int which){
		ProgressBar pb = new ProgressBar(getContext());
		pb.setIndeterminateDrawable(getContext().getResources().getDrawable(current.getContext().getResources().getIdentifier("progress", "drawable", current.getContext().getPackageName())));
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		
		if(which == FIRST_LOADING)
			pb.setPadding(0, (int)(100*ratio), 0, 0);
		else
			pb.setPadding(0, (int)(5*ratio), 0, (int)(5*ratio));
		
		pb.setLayoutParams(params);
		pb.setId(5000);
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.setGravity(Gravity.CENTER);
		table.addView(pb);
	} 
	
	private void clearTable(){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();
	}
	
	private void removeView(View v){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));	
		table.removeView(v);
	}
	
	private void removeProgress (){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		View v = table.findViewById(5000);
		table.removeView(v);
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()>=0){ 
			MessagesRead mr = new MessagesRead(v.getContext(), messages.get(v.getId()),current);
			mr.show();
		}else if(v.getId() == -200){ // LOAD MORE
			removeView(v);
			showLoading(NEXT_LOADING);			
			currentStartPosition+=10;
			loadData(currentStartPosition, NEXT_LOADING);
		}
	}
	
	private void manageConnectionException (){
		UIhandler.sendEmptyMessage(CONNECTION_ERROR);		
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {	
			  switch(msg.what){
				  case FIRST_LOADING:					  
					  clearTable();
					  loadTableRows();
				  break;
				  case NEXT_LOADING:
					  loadTableRows();
					  removeProgress();
				  break;
				  case CONNECTION_ERROR:
					  TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
					  table.removeAllViews();
					  ErrorDisplayer.showConnectionError(ErrorDisplayer.CONN_ERROR , current.getContext(), null);
				  break;
			  }
			  super.handleMessage(msg);
		  }
	};
}
