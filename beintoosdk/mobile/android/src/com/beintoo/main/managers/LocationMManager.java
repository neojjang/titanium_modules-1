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
package com.beintoo.main.managers;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.beintoo.beintoosdkutility.DebugUtility;
import com.beintoo.beintoosdkutility.LocationManagerUtils;
import com.beintoo.beintoosdkutility.PreferencesHandler;

public class LocationMManager {
	
	public static void savePlayerLocation(final Context ctx){		
		try {
			final LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
	    	if(LocationManagerUtils.isProviderSupported("network", locationManager) &&
	    			locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	    		new Thread(new Runnable(){     					
	        		public void run(){	
	    				try {
	        			Looper.prepare();
	        			final Timer t = new Timer();
						final LocationListener locationListener = new LocationListener() {
						    public void onLocationChanged(Location location) {
						    	// SAVE PLAYER LOCATION
						    	locationManager.removeUpdates(this);
						    	t.cancel();
						    	saveLocationHelper (ctx, location);
						    	// QUIT THE THREAD
						    	Looper.myLooper().quit();
						    } 
							public void onProviderDisabled(String provider) {}
		
							public void onProviderEnabled(String provider) {}
		
							public void onStatusChanged(String provider,int status, Bundle extras) {}
						};   
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener, Looper.myLooper());						
						timeoutLocationManager(locationManager, locationListener, Looper.myLooper(),t); // START A TIMEOUT TIMER
						Looper.loop();
	    				}catch(Exception e){e.printStackTrace();}
	        		}	
	        	}).start();		
    		}
		}catch(Exception e){ 
			e.printStackTrace(); 				
		}		    
	}
	
	public static void saveLocationHelper (Context ctx, Location location){
		PreferencesHandler.saveString("playerLatitude",Double.toString(location.getLatitude()), ctx);
    	PreferencesHandler.saveString("playerLongitude", Double.toString(location.getLongitude()), ctx);
    	PreferencesHandler.saveString("playerAccuracy", Float.toString(location.getAccuracy()), ctx);
    	PreferencesHandler.saveString("playerLastTimeLocation", Long.toString(location.getTime()), ctx);    	
    	DebugUtility.showLog("SAVED PLAYER LOCATION: "+location);
	}
	
	public static Location getSavedPlayerLocation(Context ctx){
		try {
			Double latitude = Double.parseDouble(PreferencesHandler.getString("playerLatitude", ctx));
			Double longitude = Double.parseDouble(PreferencesHandler.getString("playerLongitude", ctx));
			Float accuracy = Float.parseFloat(PreferencesHandler.getString("playerAccuracy", ctx));
			Long lastTimeLocation = Long.parseLong(PreferencesHandler.getString("playerLastTimeLocation", ctx));
			
			Location pLoc = new Location("network");
			pLoc.setLatitude(latitude);
			pLoc.setLongitude(longitude);
			pLoc.setAccuracy(accuracy);
			pLoc.setTime(lastTimeLocation);
			// CHECK THE LAST TIME POSITION IF > 15 min UPDATE
			Long currentTime = System.currentTimeMillis();
			if((currentTime - pLoc.getTime()) > 900000){ // TEST 20000 (20 seconds)
				savePlayerLocation(ctx);
			}
			
			return pLoc;
		
		}catch (Exception e){
			return null;
		}		
	} 
	
	public static void timeoutLocationManager (final LocationManager lm, final LocationListener ll, Looper looper, final Timer t){
		final Looper l = looper; 
		t.schedule(
		new TimerTask() {
           public void run() {
        	   try {
        		   lm.removeUpdates(ll);   
        		   if(l.getThread().isAlive())                		
        			   l.quit();
        		   t.cancel();     
            	}catch(Exception e) {}
        }},60000);
	}
}
