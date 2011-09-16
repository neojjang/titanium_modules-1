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
package com.beintoo.vgood;

import java.util.ArrayList;


import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdk.BeintooVgood;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkui.BeintooBrowser;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.LoaderImageView;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.User;
import com.beintoo.wrappers.Vgood;
import com.beintoo.wrappers.VgoodChooseOne;
import com.google.beintoogson.Gson;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class VGoodGetList extends Dialog implements OnClickListener{
	Dialog current;
	double ratio;
	final int OPEN_FRIENDS_FROM_VGOOD = 1;
	VgoodChooseOne vgoodList;
	String vgoodExtId;
	User[] friends;
	
	public VGoodGetList(Context ctx, final VgoodChooseOne vgoodlist) {
		super(ctx, ctx.getResources().getIdentifier("ThemeBeintoo", "style", ctx.getPackageName()));
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(ctx.getResources().getIdentifier("vgoodlist", "layout", ctx.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		current = this;
		vgoodList = vgoodlist;
		 
		// GETTING DENSITY PIXELS RATIO
		ratio = (ctx.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);						
		// SET UP LAYOUTS
		double pixels = ratio * 47;
		LinearLayout beintooBar = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobar", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		try {
			prepareList();	
		}catch (Exception e){e.printStackTrace();}
	}
	
	
	public void prepareList(){
		TableLayout table = (TableLayout) findViewById(current.getContext().getResources().getIdentifier("table", "id", current.getContext().getPackageName()));

		table.removeAllViews();
		
		int count = 0;
		final ArrayList<View> rowList = new ArrayList<View>();
	    for (int i = 0; i < vgoodList.getVgoods().size(); i++){
	    	
    		final LoaderImageView image = new LoaderImageView(getContext(), vgoodList.getVgoods().get(i).getImageUrl(),(int)(ratio *70),(int)(ratio *70));
    		
    		TableRow row = createRow(image, vgoodList.getVgoods().get(i).getName(),vgoodList.getVgoods().get(i).getDescription(),vgoodList.getVgoods().get(i).getEnddate(), table.getContext());
			row.setId(count);
			rowList.add(row);
			BeButton b = new BeButton(getContext());
			if(count % 2 == 0)
	    		row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 120),BDrawableGradient.LIGHT_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 120),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 120),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			else
				row.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 120),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 120),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 120),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			
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
		      if(row.getId() != -100) // IF IS NOT A SPACER IT'S CLICKABLE
		    	  row.setOnClickListener(this);
		      table.addView(row);
		}
	}
	
	public TableRow createRow(View image, String goodtitle, String gooddescription, String end,  Context activity) {
		  TableRow row = new TableRow(activity);
		  row.setGravity(Gravity.CENTER);
		  
		  image.setPadding((int)(ratio*10), 0, 0, 0);
		  ((LinearLayout) image).setGravity(Gravity.LEFT);

		  LinearLayout main = new LinearLayout(row.getContext());
		  main.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  main.setPadding((int)(ratio*5), 0, (int)(ratio*10), (int)(ratio*10));
		  main.setOrientation(LinearLayout.VERTICAL);
		  main.setGravity(Gravity.CENTER_VERTICAL);
		  
		  LinearLayout firstinnerrow = new LinearLayout(row.getContext());
		  firstinnerrow.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  firstinnerrow.setPadding(0, (int)(ratio*15), 0, 0);
		  firstinnerrow.setOrientation(LinearLayout.HORIZONTAL);
		  firstinnerrow.setGravity(Gravity.CENTER_VERTICAL);
		  
		  
		  TextView title = new TextView(activity);
		  title.setText(goodtitle);
		  title.setMaxLines(3);
		  title.setPadding((int)(ratio*10), 0, 0, 0);
		  title.setTextColor(Color.parseColor("#000000"));
		  title.setGravity(Gravity.CENTER_VERTICAL);
		  title.setTextSize(14);
		  title.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		  
		  
		  TextView description = new TextView(activity);		  
		/*  if(gooddescription.length() > 146)
			  gooddescription =  gooddescription.substring(0, 146) + "..."; */ 		  		  
		  description.setText(gooddescription);
		  description.setEllipsize(TextUtils.TruncateAt.END);
		  description.setMaxLines(3);
		  description.setLines(3);
		  description.setPadding((int)(ratio*10), (int)(ratio*8), 0, 0);
		  description.setTextColor(Color.parseColor("#333133"));
		  description.setTextSize(14);
		  
		  
		  firstinnerrow.addView(image);
		  firstinnerrow.addView(title);
		  
		  main.addView(firstinnerrow);
		  main.addView(description);
		  
		  row.addView(main,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
		  
		  return row;
	}
	
	// CALLED WHEN A ROW IS CLICKED
	public void onClick(final View v) {
		vgoodExtId = vgoodList.getVgoods().get(v.getId()).getId();
		showConfirmAlert(v.getId());
	}
	
	public void showConfirmAlert(final int row){
		
		final Vgood vgood = vgoodList.getVgoods().get(row);
		
		final CharSequence[] items = {current.getContext().getString(current.getContext().getResources().getIdentifier("vgoodgetcoupondialog", "string", current.getContext().getPackageName())), 
				current.getContext().getString(current.getContext().getResources().getIdentifier("vgoodsendgiftdialog", "string", current.getContext().getPackageName())), current.getContext().getString(current.getContext().getResources().getIdentifier("vgooddetaildialog", "string", current.getContext().getPackageName()))};

		AlertDialog.Builder builder = new AlertDialog.Builder(current.getContext());
		builder.setTitle(current.getContext().getString(current.getContext().getResources().getIdentifier("vgoodchoosedialog", "string", current.getContext().getPackageName())));
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        if(item == 0){ // GET COUPON
		        	try {
		        		BeintooBrowser getVgood = new BeintooBrowser(current.getContext(),vgood.getGetRealURL());
		        		Beintoo.currentDialog = getVgood;
		        		getVgood.show();
		        		current.dismiss();
		        	}catch(Exception e){e.printStackTrace();}
		        }else if(item == 1){ // SEND AS A GIFT
		        	final ProgressDialog  loading = ProgressDialog.show(getContext(), "", getContext().getString(current.getContext().getResources().getIdentifier("friendLoading", "string", current.getContext().getPackageName())),true);
		        	new Thread(new Runnable(){      
			    		public void run(){
			    			try{     						    				
			    				BeintooUser u = new BeintooUser();
			    				Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer",getContext()));
			    				friends = u.getUserFriends(p.getUser().getId(), null);
			    				UIhandler.sendEmptyMessage(1);
			    			}catch (Exception e){
			    				e.printStackTrace();
			    			}
			    			loading.dismiss();
			    		}
					}).start();	
		        }else if(item == 2){ // MORE DETAILS
		        	VGoodGetDialog m = new VGoodGetDialog(current.getContext(), vgood, current);
		        	m.show();
		        }
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private View createSpacer(Context activity, int color, int height) {
		  View spacer = new View(activity);
		  spacer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,height));
		  if(color == 1)
			  spacer.setBackgroundColor(Color.parseColor("#8F9193"));
		  else if(color == 2)
			  spacer.setBackgroundColor(Color.WHITE);

		  return spacer;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	try {
				// ASSIGN THE VGOOD WHEN THE USER CLOSE THE DIALOG WITHOUT CHOOSING ANY VGOOD
				final Player loggedPlayer = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", current.getContext()), Player.class);
				if(loggedPlayer.getUser() != null){
					new Thread(new Runnable(){     					
		        		public void run(){	
		    				try {
		    					BeintooVgood bv = new BeintooVgood();	    					
		    					bv.acceptVgood(vgoodList.getVgoods().get(0).getId(), loggedPlayer.getUser().getId(), null);
		    				}catch(Exception e){e.printStackTrace();}
		        		}	
		        	}).start();	
					
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {			  
			  VgoodSendToFriend f = new VgoodSendToFriend(getContext(),OPEN_FRIENDS_FROM_VGOOD, friends);
			  f.previous = current;
			  f.vgoodID = vgoodExtId;
			  f.show();
			  super.handleMessage(msg);
		  }
	};
	
}
