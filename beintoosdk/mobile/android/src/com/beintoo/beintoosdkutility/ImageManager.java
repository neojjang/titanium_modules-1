package com.beintoo.beintoosdkutility;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

public class ImageManager {
	
	private HashMap<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
	
	private File cacheDir;
	private ImageQueue imageQueue = new ImageQueue();
	private Thread imageLoaderThread = new Thread(new ImageQueueManager());
	
	public ImageManager(Context context) {
		imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
		try{
			String sdState = android.os.Environment.getExternalStorageState();
			if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
				File sdDir = android.os.Environment.getExternalStorageDirectory();		
				cacheDir = new File(sdDir,".beintoo/cache");
			}
			else
				cacheDir = context.getCacheDir();
			
			if(!cacheDir.exists())
				cacheDir.mkdirs();
			
		}catch(Exception e){ }
	}
	   
	public void displayImage(String url, Context context, ImageView imageView) {
		try {
			if(imageMap.containsKey(url)){
				imageView.setImageBitmap(imageMap.get(url));
			}else {
				queueImage(url, context, imageView);
				imageView.setImageResource(context.getResources().getIdentifier("nopict", "drawable", context.getPackageName()));			
			}
		}catch(Exception e){}
	}

	private void queueImage(String url, Context context, ImageView imageView) {
		try {
			imageQueue.Clean(imageView);
			ImageRef p=new ImageRef(url, imageView);
	
			synchronized(imageQueue.imageRefs) {
				imageQueue.imageRefs.push(p);
				imageQueue.imageRefs.notifyAll();
			}
	
			if(imageLoaderThread.getState() == Thread.State.NEW)
				imageLoaderThread.start();
		}catch(Exception e){ } 
	}

	private Bitmap getBitmap(String url) {
		try {
			String filename = String.valueOf(url.hashCode());
			File f = new File(cacheDir, filename);
			
			try{
				// CHECK IF CACHE IS TOO OLD
				if(f!=null){
					if(System.currentTimeMillis() > f.lastModified() + 86400000) // 24 HOURS
						f.delete();
				}
			}catch(Exception e){}
			
			Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
			if(bitmap != null) return bitmap;

			bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
			writeFile(bitmap, f);
			
			return bitmap;
		} catch (Exception ex) {
			return null;
		}
	}
	
	private void writeFile(Bitmap bmp, File f) {
		FileOutputStream out = null;
		
		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
		} catch (Exception e) {
			
		}
		finally { 
			try { if (out != null ) out.close(); }
			catch(Exception ex) {} 
		}
	}
	
	private class ImageRef {
		public String url;
		public ImageView imageView;
		
		public ImageRef(String u, ImageView i) {
			url=u;
			imageView=i;
		}
	}
	
	private class ImageQueue {
		
		private Stack<ImageRef> imageRefs = new Stack<ImageRef>();

		public void Clean(ImageView view) {
			try {
				for(int i = 0 ;i < imageRefs.size();) {
					if(imageRefs.get(i).imageView == view)
						imageRefs.remove(i);
					else ++i;
				}
			}catch (Exception e){}
		}
	}
	
	private class ImageQueueManager implements Runnable {
		@Override
		public void run() {
			try {
				while(true) {
					if(imageQueue.imageRefs.size() == 0) {
						synchronized(imageQueue.imageRefs) {
							imageQueue.imageRefs.wait();
						}
					}
					
					if(imageQueue.imageRefs.size() != 0) {
						ImageRef imageToLoad;

						synchronized(imageQueue.imageRefs) {
							imageToLoad = imageQueue.imageRefs.pop();
						}
						
						Bitmap bmp = getBitmap(imageToLoad.url);
						imageMap.put(imageToLoad.url, bmp);
						Object tag = imageToLoad.imageView.getTag();
						
						if(tag != null && ((String)tag).equals(imageToLoad.url)) {
							BitmapDisplayer bmpDisplayer = 
								new BitmapDisplayer(bmp, imageToLoad.imageView);
							
							
							UIhandler.post(bmpDisplayer);
						}
					}
					
					if(Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {}
		}
	}

	private class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;
		
		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap=b;
			imageView=i;
		}
		
		public void run() {
			if(bitmap != null){
				imageView.setImageBitmap(bitmap);						
			}else{
				//imageView.setImageResource(current.getContext().getResources().getIdentifier("nopict", "drawable", current.getContext().getPackageName()));
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void deleteFiles(File dir){
		try{
			if (dir.isDirectory()) {
		        String[] children = dir.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(dir, children[i]).delete();
		        }
		    }
		}catch(Exception e){}
	}
	
	Handler UIhandler = new Handler(){};
}
