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

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHandler {
	public static void saveString (String key, String value, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(key,value);
	    editor.commit();
	}
	
	public static String getString (String key, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
		return settings.getString(key,null);		
	}
	
	public static void saveBool (String key, boolean value, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(key,value);
	    editor.commit();
	}
	
	public static void saveBool (String file, String key, boolean value, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(file, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(key,value);
	    editor.commit();
	}
	
	public static boolean getBool (String key, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
		return settings.getBoolean(key,false);		
	}
	
	public static boolean getBool (String file, String key, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(file, 0);
		return settings.getBoolean(key,false);		
	}
	
	public static void saveInt (String key, int value, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(key,value);
	    editor.commit();
	}
	
	public static int getInt (String key, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
		return settings.getInt(key,0);
	}
	
	public static void saveLong (String key, long value, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putLong(key,value);
	    editor.commit();
	}
	
	public static long getLong (String key, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
		return settings.getLong(key,0);
	}
	
	public static void clearPref (String key, Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(key, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}
}
