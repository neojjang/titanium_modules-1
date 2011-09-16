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


import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class LoaderImageView extends LinearLayout{

	private static final int COMPLETE = 0;
	private static final int FAILED = 1;

	private static Context mContext;
	private Drawable mDrawable;
	private ProgressBar mSpinner;
	private ImageView mImage;
	
	public LoaderImageView(final Context context, final AttributeSet attrSet) {
		super(context, attrSet);
		final String url = attrSet.getAttributeValue(null, "image");
		if(url != null){
			instantiate(context, url);
		} else {
			instantiate(context, null);
		}
	}
	
	public LoaderImageView(final Context context, boolean only) {
		super(context);
	}
	
	public LoaderImageView(final Context context, final String imageUrl) {
		super(context);
		instantiate(context, imageUrl);	
	}
	
	public LoaderImageView(final Context context) {
		super(context);
		instantiate(context, null);	
	}
	
	public LoaderImageView(final Context context, final String imageUrl, int width, int height) {
		super(context);
		instantiate(context, imageUrl, width, height);
	}

	private void instantiate(final Context context, final String imageUrl) {
		this.instantiate(context, imageUrl, 0, 0);
	}
	
	private void instantiate(final Context context, final String imageUrl, int width, int height) {
		mContext = context;
		
		mImage = new ImageView(mContext);
		mImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mSpinner = new ProgressBar(mContext);
		mSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		if(width != 0 && height != 0){
			mImage.setAdjustViewBounds(true);
			mImage.setMaxHeight(height);
			mImage.setMaxWidth(width);
			mImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);			
		}
			
		mSpinner.setIndeterminate(true);
		mSpinner.setIndeterminateDrawable(getContext().getResources().getDrawable(context.getResources().getIdentifier("progress", "drawable", context.getPackageName())));
		
		
		addView(mSpinner);
		addView(mImage);
		
		if(imageUrl != null){
			setImageDrawable(imageUrl);
		}
	}

	public void setImageDrawable(final String imageUrl) {
		setImageDrawable(imageUrl,0,0,false);
	}
	
	public void setImageDrawable(final String imageUrl, final int width, final int height, final boolean scale) {
		mDrawable = null;		
		mSpinner.setVisibility(View.VISIBLE);
		mImage.setVisibility(View.GONE);
		
		if(scale){
			mImage.setAdjustViewBounds(true);
			mImage.setMaxHeight(height);
			mImage.setMaxWidth(width);
			mImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);	
			mSpinner.setLayoutParams(new LayoutParams(width/2, height/2));
		}
		
		new Thread(){
			public void run() {
				try {
					mDrawable = getDrawableFromUrl(imageUrl);
					if(scale){
						try {
							Bitmap bmImg = ((BitmapDrawable)mDrawable).getBitmap();
				            Bitmap resizedbitmap = resize(bmImg, width,height);				            
							mDrawable = new BitmapDrawable(resizedbitmap);
						}catch(Exception e){e.printStackTrace();}
					}
					imageLoadedHandler.sendEmptyMessage(COMPLETE);
				} catch (MalformedURLException e) {
					imageLoadedHandler.sendEmptyMessage(FAILED);
					e.printStackTrace();
				} catch (IOException e) {					
					imageLoadedHandler.sendEmptyMessage(FAILED);
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	private final Handler imageLoadedHandler = new Handler(new Callback() {
		
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case COMPLETE:
				mImage.setImageDrawable(mDrawable);
				mImage.setVisibility(View.VISIBLE);
				mSpinner.setVisibility(View.GONE);
				break;
			case FAILED:
			default:
				// Could change image here to a 'failed' image
				// otherwise will just keep on spinning
				break;
			}
			return true;
		}		
	});

	private static Drawable getDrawableFromUrl(final String url) throws IOException, MalformedURLException {
		try{			
			HttpGet httpRequest = new HttpGet(new URL(url).toURI());
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpClient.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity); 
			final long contentLength = bufHttpEntity.getContentLength();
			if (contentLength >= 0) {
			    InputStream is = bufHttpEntity.getContent();			    
			    return Drawable.createFromStream(is, "name");
			} else {
			    return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void setImageMaxSize(int w, int h){
		mImage.setMaxWidth(w);
		mImage.setMaxHeight(h);
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	private Bitmap resize (Bitmap b, int newWidth, int newHeight){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);
        return resizedBitmap;
	}
	
}
