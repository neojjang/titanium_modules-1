package com.beintoo.beintoosdkutility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.graphics.Typeface;

//TEST
//Typeface font = CustomFonts.handWriteFont(ctx);
//if(font!=null)
//	info.setTypeface(font);  
public class CustomFonts {
	private final static String fontName = "markerfelt.ttf";
	
	public static Typeface handWriteFont(Context context){
		try {
			File fontFile = null;
			String sdState = android.os.Environment.getExternalStorageState();
			
			if(checkIfExists(context)){
				if (sdState.equals(android.os.Environment.MEDIA_MOUNTED))
					fontFile = new File(android.os.Environment.getExternalStorageDirectory(),".beintoo/fonts/"+fontName);
				else
					fontFile = new File(context.getCacheDir(),".beintoo/fonts/"+fontName);
				
				// IF THERE'S A PREVIOUS FONT IN CACHE AND THE MEDIA IS MOUNTED DELETE IT
				if(!fontFile.exists()) new File(context.getCacheDir(),".beintoo/fonts/"+fontName).delete();
			}else{
				loadRemoteFont(context);
				if (sdState.equals(android.os.Environment.MEDIA_MOUNTED))
					fontFile = new File(android.os.Environment.getExternalStorageDirectory(),".beintoo/fonts/"+fontName);
				else
					fontFile = new File(context.getCacheDir(),".beintoo/fonts/"+fontName);
					
			}
			Typeface font = Typeface.createFromFile(fontFile); 
			
			return font;
		}catch(Exception e){
			return null;
		}
	}
	
	private static void loadRemoteFont(Context context){
		try {
			
			String path ="http://static.beintoo.com/sdk/android/font/"+fontName;
		    String sdState = android.os.Environment.getExternalStorageState();
		    File fontDir;
		    String fontFilePath;
		    
		    if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
				File sdDir = android.os.Environment.getExternalStorageDirectory();		
				fontDir = new File(sdDir,".beintoo/fonts/");
				fontFilePath = fontDir + "/" + fontName;
			}else{
				fontDir = new File(context.getCacheDir(),".beintoo/fonts/");
				fontFilePath = fontDir + "/" + fontName;
			}
			
			if(!fontDir.exists())
				fontDir.mkdirs();
		    
		    URL u = new URL(path);
		    HttpURLConnection c = (HttpURLConnection) u.openConnection();
		    c.setRequestMethod("GET");
		    c.setDoOutput(true);
		    c.connect();
		    FileOutputStream f = new FileOutputStream(new File(fontFilePath));
	        InputStream in = c.getInputStream();
	        byte[] buffer = new byte[1024];
	        int len1 = 0;
	        
	        while ( (len1 = in.read(buffer)) > 0 ) {
	        	f.write(buffer,0, len1);
		    }
	        
		    f.close();
		    
		    DebugUtility.showLog("Downloaded font: "+path);
	    } catch (Exception e) {

	    } 
	}
	
	private static boolean checkIfExists(Context context){
		if(new File(android.os.Environment.getExternalStorageDirectory(),".beintoo/fonts/"+fontName).exists() || new File(context.getCacheDir(),".beintoo/fonts/"+fontName).exists())
			return true;
		else
			return false;
	}
}
