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

import java.util.List;



import com.beintoo.beintoosdk.BeintooAchievements;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.wrappers.AchievementWrap;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.PlayerAchievement;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UserAchievements extends Dialog implements OnClickListener{
	
	private List<PlayerAchievement> pachivement;
	private Dialog current;
	private double ratio;
	
	private int LOAD_DATA = 1;
	private int EMPTY_TABLE = 2;
	private int CONNECTION_ERROR = 3;
	
	
	public UserAchievements(Context context) {
		super(context,context.getResources().getIdentifier("ThemeBeintoo", "style", context.getPackageName()));
		current = this; 
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(current.getContext().getResources().getIdentifier("userbalance", "layout", current.getContext().getPackageName()));
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		// GETTING DENSITY PIXELS RATIO
		ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 40;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));		
		TextView titleBar = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
		titleBar.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("achievement", "string", current.getContext().getPackageName())));

		startLoading();

	}

	private void startLoading(){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.removeAllViews();
		showLoading();
		loadData();
	}
	
	private void loadData () {
		new Thread(new Runnable(){      
    		public void run(){
    			try{ 
    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
    				BeintooAchievements ba = new BeintooAchievements();    				
    				pachivement = ba.getPlayerAchievements(p.getGuid());	
    				if(pachivement.size() > 0)
    					UIhandler.sendEmptyMessage(LOAD_DATA);
    				else 
    					UIhandler.sendEmptyMessage(EMPTY_TABLE);
    			}catch (Exception e){ 
    				e.printStackTrace();
    				UIhandler.sendEmptyMessage(CONNECTION_ERROR);
    				ErrorDisplayer.showConnectionErrorOnThread(ErrorDisplayer.CONN_ERROR, getContext(), e);    				
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
		table.setGravity(Gravity.CENTER);
		table.addView(pb);
	} 
	
	public void loadTable(){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
		table.setColumnStretchable(1, true);
		table.removeAllViews();		
				
		final ArrayList<View> rowList = new ArrayList<View>();
		
    	for(int i = 0; i<pachivement.size(); i++){
    		//final LoaderImageView image = new LoaderImageView(getContext(), getUsersmallimg());
    		final LoaderImageView image = new LoaderImageView(getContext(),pachivement.get(i).getAchievement().getImageURL(),(int)(ratio * 60),(int)(ratio *60));
       		
    		TableRow row = createRow(image, pachivement.get(i).getAchievement().getName(),pachivement.get(i).getAchievement().getDescription(),
    				pachivement.get(i).getAchievement().getBedollars(), pachivement.get(i).getStatus(),getContext());
			row.setId(i);
			rowList.add(row);
			View spacer = createSpacer(getContext(),1,1);
			spacer.setId(-100);
			rowList.add(spacer);
			View spacer2 = createSpacer(getContext(),2,1);
			spacer2.setId(-100);
			rowList.add(spacer2);
			BeButton b = new BeButton(current.getContext());
			if(i % 2 == 0)
	    		row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 70),BDrawableGradient.LIGHT_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 70),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 70),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			else
				row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 70),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 70),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 70),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			
			row.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					DetailsPopup dp = new DetailsPopup(current.getContext(), v.getId());
					dp.show();
				}
				
			});
	
    	}	 
	    
	    for (View row : rowList) {	      
		      table.addView(row);
		}
	}
	
	public TableRow createRow(View image, String appName,String description, double value, String unlocked, Context activity) {
		  TableRow row = new TableRow(activity);
		  
		  image.setPadding((int)(ratio*10), 0, (int)(ratio*10), 0);
		  ((LinearLayout) image).setGravity(Gravity.LEFT);
		  
		  LinearLayout center = new LinearLayout(row.getContext());
		  center.setOrientation(LinearLayout.VERTICAL);
		  center.setGravity(Gravity.CENTER_VERTICAL);
		  
		  LinearLayout end = new LinearLayout(row.getContext());
		  end.setOrientation(LinearLayout.VERTICAL);
		  end.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		  end.setPadding(0, 0, (int)(ratio*15), 0);
		  
		  LinearLayout img = new LinearLayout(row.getContext());
		  img.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  img.setGravity(Gravity.CENTER);
		  img.addView(image);
		  
		  // GET THE SCREEN ORIENTATION
		  int getOrient = current.getContext().getApplicationContext().getResources().getConfiguration().orientation;
		  
		  // ACHIEVEMENT NAME DESC
		  TextView achievname = new TextView(activity);
		  if(getOrient == Configuration.ORIENTATION_PORTRAIT || getOrient == Configuration.ORIENTATION_SQUARE || getOrient == Configuration.ORIENTATION_UNDEFINED)
			  achievname.setMaxWidth((int)(ratio*170));
		  else
			  achievname.setMaxWidth((int)(ratio*320));
		  achievname.setText(appName);
		  achievname.setPadding(0, 0, 0, 0);
		  if(unlocked.equals("UNLOCKED"))
			  achievname.setTextColor(Color.parseColor("#545859"));
		  else
			  achievname.setTextColor(Color.argb(127, 84, 88, 89));			  
		  achievname.setMaxLines(2);
		  achievname.setTextSize(16);
		  achievname.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  		    
		  // ACHIEVEMENT DESCRIPTION TEXTVIEW
		  TextView achievdesc = new TextView(activity);
		  if(getOrient == Configuration.ORIENTATION_PORTRAIT || getOrient == Configuration.ORIENTATION_SQUARE || getOrient == Configuration.ORIENTATION_UNDEFINED)
			  achievdesc.setMaxWidth((int)(ratio*170));
		  else
			  achievdesc.setMaxWidth((int)(ratio*320));
		  achievdesc.setText(description);	
		  achievdesc.setPadding(0, 0, 0, 0);
		  if(unlocked.equals("UNLOCKED"))
			  achievdesc.setTextColor(Color.parseColor("#787A77"));
		  else
			  achievdesc.setTextColor(Color.argb(127, 84, 88, 89));
		  achievdesc.setMaxLines(2);
		  achievdesc.setTextSize(12);		  	  
		  achievdesc.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));		  
		  
		  center.addView(achievname);
		  center.addView(achievdesc);
		  
		  TextView bedollars = new TextView(activity);		  
		  bedollars.setPadding(0, 0, 0, 0);
		  bedollars.setTextColor(Color.parseColor("#545859"));
		  bedollars.setTextSize(12);
		  bedollars.setText("Bedollars");
		  bedollars.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  
		  TextView movvalue = new TextView(activity);
		  String textvalue;
		  if(value > 0) textvalue = "+"+String.format("%02d", (int)value);
		  else textvalue = String.format("%02d", (int)value); 
		  movvalue.setText(textvalue);
		  movvalue.setPadding(0, 0, 0, 0);
		  movvalue.setTextColor(Color.GRAY);
		  movvalue.setTextSize(30);
		  movvalue.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  
		  end.addView(bedollars);
		  end.addView(movvalue);
		  
		  row.addView(img,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int)(ratio*70)));
		  row.addView(center,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.FILL_PARENT));
		  
		  if(unlocked.equals("UNLOCKED"))
			  row.addView(end,new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.FILL_PARENT)); 
		  
		  
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
	
	private class DetailsPopup extends Dialog {

		public DetailsPopup(Context context, Integer selectedRow) {
			super(context);
			
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(current.getContext().getResources().getIdentifier("achivementdesc", "layout", current.getContext().getPackageName()));
			
			try {
				PlayerAchievement pa = pachivement.get(selectedRow);
				LoaderImageView currentAppPict = (LoaderImageView) findViewById(current.getContext().getResources().getIdentifier("apppict", "id", current.getContext().getPackageName()));
				TextView appname = (TextView) findViewById(current.getContext().getResources().getIdentifier("appname", "id", current.getContext().getPackageName()));
				TextView appdesc = (TextView) findViewById(current.getContext().getResources().getIdentifier("appdesc", "id", current.getContext().getPackageName()));
				TableLayout tv = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
				
				currentAppPict.setImageDrawable(pa.getAchievement().getImageURL(),(int)(ratio * 60),(int)(ratio *60), true);
				currentAppPict.setPadding(0, 0, (int)(ratio *10), 0);
				
				appname.setText(pa.getAchievement().getName());
				appdesc.setText(pa.getAchievement().getDescription());
				
				List<AchievementWrap> blockers = pa.getAchievement().getBlockedBy();
				
				if(blockers == null){ 
					findViewById(current.getContext().getResources().getIdentifier("blockedby", "id", current.getContext().getPackageName())).setVisibility(View.GONE);
				}else{
					for(int i = 0; i < blockers.size(); i++){
						TableRow row = new TableRow(context);		
						row.setPadding(0, (int)(ratio *10), 0, (int)(ratio *10));
						row.setGravity(Gravity.CENTER_VERTICAL);
												
						TextView name = new TextView(context);
						TextView desc = new TextView(context);
						final LoaderImageView image = 
							new LoaderImageView(getContext(),blockers.get(i).getImageURL(),(int)(ratio * 60),(int)(ratio *60));
						image.setPadding(0, 0, (int)(ratio *10), 0);
						
						name.setText(blockers.get(i).getName());
						name.setTextColor(Color.parseColor("#545859"));
						
						StringBuilder achDesc = new StringBuilder(blockers.get(i).getDescription());
						if(blockers.get(i).getApp()!=null){
							achDesc.append("<br />");
							achDesc.append(context.getString(current.getContext().getResources().getIdentifier("onapp", "string", current.getContext().getPackageName())));
							achDesc.append("<a href=\"");
							
							if(blockers.get(i).getApp().getDownload_url().get("ANDROID") != null)
								achDesc.append(blockers.get(i).getApp().getDownload_url().get("ANDROID"));
							else if(blockers.get(i).getApp().getDownload_url().get("WEB") != null)
								achDesc.append(blockers.get(i).getApp().getDownload_url().get("WEB"));
							
							achDesc.append("\">");
							achDesc.append(blockers.get(i).getApp().getName());
							achDesc.append("</a>");
							
							if(blockers.get(i).getApp().getDownload_url().get("WEB") != null)
								desc.setMovementMethod(LinkMovementMethod.getInstance());
							else if(blockers.get(i).getApp().getDownload_url().get("WEB") != null)
								desc.setMovementMethod(LinkMovementMethod.getInstance());
							
							desc.setText(Html.fromHtml(achDesc.toString()));							
						}else						
							desc.setText(achDesc.toString());
						
						desc.setTextColor(Color.parseColor("#545859"));
						
						LinearLayout center = new LinearLayout(row.getContext());
						center.setOrientation(LinearLayout.VERTICAL);
						center.addView(name);
						center.addView(desc);
						
						row.addView(image);
						row.addView(center);
						tv.addView(row);
					}
				}
			}catch(Exception e){ e.printStackTrace(); }				
		}
		
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {	
			  if(msg.what == LOAD_DATA)
				  loadTable();
			  else if(msg.what == EMPTY_TABLE){
				  TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
				  table.removeAllViews();
				  TextView t = new TextView(current.getContext());
				  t.setText(current.getContext().getString(current.getContext().getResources().getIdentifier("achievementempty", "string", current.getContext().getPackageName())));
				  t.setTextColor(Color.GRAY);
				  t.setPadding(15,25,0,0);
				  table.addView(t);	
			  }else if(msg.what == CONNECTION_ERROR){				
				  TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));
				  table.removeAllViews(); 
			  }
			  super.handleMessage(msg);
		  }
	};
	
	public void onClick(View arg0) {
		
	}

}
