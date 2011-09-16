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


import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.main.Beintoo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;


public class tryDialog extends AlertDialog{
	AlertDialog alert;
	public tryDialog(final Context context) {
		super(context);
		
		/*ImageView img = new ImageView(context);
		img.setImageResource(current.getContext().getResources().getIdentifier("blogo", "drawable", current.getContext().getPackageName()));
			*/	
		final LinearLayout main = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
		main.setPadding(0, 0, 0, 0);
        main.setLayoutParams(params);        
        main.setOrientation(LinearLayout.VERTICAL);
        
        View view = LayoutInflater.from(context).inflate(context.getResources().getIdentifier("beintoobar", "layout", context.getPackageName()),
                null);
        
        double ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);		
		double pixels = ratio * 47;
		view.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
        main.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*155),BDrawableGradient.LIGHT_GRAY_GRADIENT));		
        main.addView(view);
                
        View spacer = new View(context);
		spacer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,(int)(1*ratio)));
		spacer.setBackgroundColor(Color.WHITE);		
		main.addView(spacer);
        
		TextView message = new TextView(context);
		message.setPadding(20, 20, 10, 20);
		message.setText("Beintoo transforms your passion for apps into real rewards.\nIt's extremely easy!\n" +
				"Simple use your Facebook account to login and you will be rewarded with exceptional virtual and real goods while" +
				" using apps and playing games.");
		message.setGravity(Gravity.CENTER);
        message.setTextColor(Color.parseColor("#1B1B1C"));
        main.addView(message);
        
        
        View spacer2 = new View(context);
		spacer2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,(int)(1*ratio)));
		spacer2.setBackgroundColor(Color.GRAY);		
		main.addView(spacer2);
        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
               .setCancelable(false)
               .setPositiveButton("Try it!", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Beintoo.BeintooStart(context, true);
                   }
               })
               .setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dialog.cancel();
                   }
               });
        alert = builder.create();
        alert.setView(main, 0, 0, 0, 0);
        
//        builder.setInverseBackgroundForced(true);
		/*this.setView(main)
	       .setCancelable(false)
	       .setPositiveButton("Try it!", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                Beintoo.BeintooStart(context);
	           }
	       })
	       .setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	           }
	       });
		
	       */
		//AlertDialog alert = this.create();
		//alert.show();
	}
	public void open(){
		alert.show();
	}
	/*
	@Override
	public Builder setView(View view) {
		// TODO Auto-generated method stub
		return super.setView(view);
	}  */
	
}
