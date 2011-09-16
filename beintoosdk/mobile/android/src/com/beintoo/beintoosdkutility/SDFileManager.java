package com.beintoo.beintoosdkutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.os.Environment;

public class SDFileManager {
	public SDFileManager() {

	}

	public boolean isAvailable() {
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageWriteable = true;
		} else {
			mExternalStorageWriteable = false;
		}

		return mExternalStorageWriteable;
	}

	public boolean WriteToFile(String fileName, String textToWrite) throws IOException {
		File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/.beintoo");
        dir.mkdirs();
        File file = new File(dir, fileName);
		
        ObjectOutputStream oos = null;
		boolean success = false;

		try {
			OutputStream os = new FileOutputStream(file);
			oos = new ObjectOutputStream(os);
			oos.writeObject(textToWrite);
			success = true;
		} finally {
			try {
				if (null != oos)
					oos.close();
			} catch (IOException ex) {}
		}
		
		return success;
	}
	
	public String ReadFile(String fileName) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = null;  
        String result = null;  
  
        try  
        {  
        	File root = Environment.getExternalStorageDirectory();
    		File file = new File(root.getAbsolutePath(),".beintoo/"+fileName);  
            FileInputStream fis = new FileInputStream(file);  
            ois = new ObjectInputStream(fis);  
            result = (String) ois.readObject();  
            ois.close();  
        }  
        finally {  
            try {if (null != ois) ois.close();} catch (IOException ex) {}  
        }  
  
        return result;
	}
}
