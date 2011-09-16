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

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;

import com.beintoo.beintoosdk.DeveloperConfiguration;

import android.content.Context;
import android.telephony.TelephonyManager;


public class DeviceId {
	private final static String BUGGED_ANDROID_ID = "9774d56d682e549c";
		
	public static String getUniqueAndroidDeviceId(Context context){		
	    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    String tmDevice, tmSerial,androidId = null;
	    try {
		    tmDevice = "" + tm.getDeviceId();
		    tmSerial = tm.getSimSerialNumber() != null ? tm.getSimSerialNumber() : "";
		    androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		    // LET'S CHECK IF WE ARE IN THE ANDROID SIMULATOR TO PREVENT SAME DEVICEID FOR EVERY DEVELOPER
		    if(androidId == null){ // EMULATOR
		    	return toSHA1(DeveloperConfiguration.apiKey);
		    }else if(androidId.equals(BUGGED_ANDROID_ID) && ("000000000000000".equals(tmDevice))){ // EMULATOR 2.2 BUGGED
		    	return toSHA1(DeveloperConfiguration.apiKey);
		    }else if((androidId.equals(BUGGED_ANDROID_ID)) && (tmSerial.equals(""))){ // BUGGED 2.2 WITHOUT SIM
		    	return null;
		    }else { // GOOD
		    	UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());	    
		    	String deviceId = deviceUuid.toString();
		    	return deviceId;
		    }	
	    }catch (Exception e){
	    	return null;
	    }
	}  
	
	public static String getRandomIdentifier(){
		return toSHA1(UUID.randomUUID()+":"+System.nanoTime());
	}
	
	public static String toSHA1(String input) {
        try {
            int lenght = 40; // 64 from SHA-256
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = sha.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String out = number.toString(16);
            if (out.length() < lenght) {
                char[] charArray = new char[lenght];
                Arrays.fill(charArray, '0'); 
                out.getChars(0, out.length(), charArray, lenght - out.length());
                out = new String(charArray);
            }
            return out;
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return null;
	}
	
	
	public static String getUniqueDeviceId(Context context){
				
		SDFileManager sdf = new SDFileManager();		
		if(!sdf.isAvailable()){
			return null;
		}
		
		String deviceID = null;
		
		try {
			deviceID = sdf.ReadFile("beintoodeviceid.be");
		}catch (FileNotFoundException fnf){
			 fnf.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		if(deviceID != null){
			return deviceID;
		}else{
			String randomID = DeviceId.getRandomIdentifier();
			
			try {
				sdf.WriteToFile("beintoodeviceid.be", randomID);
			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
			
			return randomID;
		}		
	}
}
