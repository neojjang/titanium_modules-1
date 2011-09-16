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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Timer;
import java.util.TimerTask;

import com.beintoo.beintoosdk.BeintooApp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class ErrorDisplayer{	
	private final static boolean reportingEnable = false;
	public static final String CONN_ERROR = "Connection error.\nPlease check your Internet connection.";
	
	public static void showConnectionError (String Message, final Context ctx, Exception e){

		Handler handler = new Handler(){		     
			public void handleMessage(Message msg) {	
				Toast.makeText(ctx, msg.getData().getString("SOMETHING"), Toast.LENGTH_SHORT).show();  				
			}
		};
	   Message status = handler.obtainMessage();
	   Bundle data = new Bundle();
	   data.putString("SOMETHING",Message);
	   status.setData(data);

	   handler.sendMessage(status);	
	   
	   // REPORT THE ERROR (REMOVE CONNECTION ERRORS
	   if(e!=null && !(e instanceof java.net.SocketTimeoutException) && !(e instanceof java.net.ConnectException))
		   try {
			   errorReport(StackStraceTostring(e));		   
		   }catch(Exception ex){}
	}
	
	public static void showConnectionErrorOnThread (String Message, final Context ctx, Exception e){
		
		Looper.prepare();
		final Looper tLoop = Looper.myLooper();
		showConnectionError (Message,ctx,e);	
		
		// QUIT THE Looper AFTER THE TOAST IS DISMISSED
		final Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				tLoop.quit();
				t.cancel();
			}
		}, 6000);
		
	    Looper.loop();
	}
	
	private static String StackStraceTostring(Exception e){
		try {
			final Writer result = new StringWriter();
	        final PrintWriter printWriter = new PrintWriter(result);
	        e.printStackTrace(printWriter);
	        
	        return result.toString();	        
		}catch(Exception ex){ex.printStackTrace();}
		
		return "";
	}
	
	private static void errorReport (final String stackTrace){
		new Thread(new Runnable(){     					
    		public void run(){ 
    			try{  
    				BeintooApp ba = new BeintooApp();
    		    	ba.ErrorReporting(null, null,  BeintooSdkParams.version + "\n\n" + stackTrace);	
    			}catch(Exception e){e.printStackTrace();}
    		}
		}).start();			
	}
	
	public static void externalReport (Exception e){
		try {
			if(reportingEnable) errorReport(StackStraceTostring(e));
		}catch (Exception e1){e1.printStackTrace();}
	} 
}
