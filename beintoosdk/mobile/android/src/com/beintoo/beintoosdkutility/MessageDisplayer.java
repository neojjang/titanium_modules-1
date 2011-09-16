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
package com.beintoo.beintoosdkutility;

 

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageDisplayer{
	static Dialog toast = null;
	static WindowManager wM;
	static View v;
	static Handler mHandler;
	public static void showMessage (Context ctx, String message, int g){
		try {
			wM = (WindowManager) ctx.getApplicationContext()
	         .getSystemService(Context.WINDOW_SERVICE);
			if(v != null){
				if(v.isShown()){
					wM.removeView(v);
					mHandler.removeCallbacks(mRunnable);
				}
			}
				
			WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
			 LayoutInflater inflate = (LayoutInflater) ctx
			         .getApplicationContext().getSystemService(
			                 Context.LAYOUT_INFLATER_SERVICE);
			v = inflate.inflate(ctx.getResources().getIdentifier("notification", "layout", ctx.getPackageName()), null);
			
			TextView tw = (TextView) v.findViewById(ctx.getResources().getIdentifier("message", "id", ctx.getPackageName()));
			tw.setText(message);
			
			LinearLayout ll = (LinearLayout) v.findViewById(ctx.getResources().getIdentifier("main", "id", ctx.getPackageName()));
			ll.setBackgroundColor(Color.argb(200, 65, 65, 65)); 
			
			mParams.gravity = g;
			mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
			mParams.width = WindowManager.LayoutParams.FILL_PARENT;
			mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
			mParams.format = PixelFormat.TRANSLUCENT;
			mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
			mParams.windowAnimations = android.R.style.Animation_Toast;		
			wM.addView(v, mParams);
		
		
			mHandler = new Handler();
			int delay = getDelay(tw.length());
			mHandler.postDelayed(mRunnable, delay);
		}catch (Exception e){}
	}
	
	private static Runnable mRunnable = new Runnable()
	{
	    public void run()
	    {	    	
	    	try {
	    		wM.removeView(v);
	    	}catch(Exception e){}
	    }
	 };
	
	 private static int getDelay(Integer lenght){
		 if(wM.getDefaultDisplay().getWidth() < wM.getDefaultDisplay().getHeight())			 
			 if(lenght < 40)
				 return 3000;
			 else if(lenght >=40 && lenght < 60)
				 return 6000;
			 else if(lenght >=60 && lenght < 80)
				 return 11000;
			 else if(lenght >=80 && lenght < 100)
				 return 16000;
			 else if(lenght >=100 && lenght < 120)
				 return 21000;
			 else if(lenght >=120 && lenght < 140)
				 return 26000;
			 else if(lenght >=140 && lenght < 160)
				 return 31000;
			 else if(lenght >=160 && lenght < 180)
				 return 36000;
			 else
				 return 41000; 
		 else
			 if(lenght < 60)
				 return 3000;
			 else if(lenght >=60 && lenght <80)
				 return 6000;
			 else if(lenght >=80 && lenght <100)
				 return 11000;
			 else if(lenght >=100 && lenght <120)
				 return 16000;
			 else if(lenght >=120 && lenght <140)
				 return 21000;
			 else if(lenght >=140 && lenght <160)
				 return 26000;
			 else 
				 return 31000;
	 }
	 
	@SuppressWarnings("unused")
	private static ShapeDrawable messageBackground(){
		//RoundRectShape rect = new RoundRectShape( new float[] {15,15, 15,15, 15,15, 15,15}, null, null);
		RoundRectShape rect = new RoundRectShape( new float[] {0,0, 0,0, 0,0, 0,0}, null, null);
		ShapeDrawable bg = new ShapeDrawable(rect);		
		
		Paint p = new Paint();
		int [] colors = {0xffC6CACE, 0xff9EA6AF};
		float [] positions = {0.0f,0.5f};
	    p.setShader(new LinearGradient(0, 0, 0, 50, colors, positions,  Shader.TileMode.MIRROR));	    	    
	    bg.getPaint().set(p);
	    bg.getPaint().setAlpha(230); 
	    
	    return bg;
	}
}
