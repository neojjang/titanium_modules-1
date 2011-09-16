package com.beintoo.activities.leaderboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


import com.beintoo.activities.leaderboard.Leaderboard.Leaders;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.ImageManager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeaderboardAdapter extends ArrayAdapter<Leaders> {
	private ArrayList<Leaders> leaders;
	public ImageManager imageManager;
	private Context currentContext;
	private final double ratio;
	
	public LeaderboardAdapter(Context context, int textViewResourceId, ArrayList<Leaders> leaders) {
		super(context, textViewResourceId, leaders);
		this.leaders = leaders;
		this.currentContext = context;
		this.ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);
		
		imageManager = new ImageManager(context);
	}
	
	public static class ViewHolder{
		public TextView nickname;
		public TextView score;
		public TextView position;
		public ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		
		try {
			if (v == null) {		
				LayoutInflater vi = 
					(LayoutInflater)currentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(currentContext.getResources().getIdentifier("leaderboarditem", "layout", currentContext.getPackageName()), null);
				holder = new ViewHolder();
				holder.nickname = (TextView) v.findViewById(currentContext.getResources().getIdentifier("nickname", "id", currentContext.getPackageName()));
				holder.score = (TextView) v.findViewById(currentContext.getResources().getIdentifier("score", "id", currentContext.getPackageName()));
				holder.position = (TextView) v.findViewById(currentContext.getResources().getIdentifier("position", "id", currentContext.getPackageName()));
				holder.image = (ImageView) v.findViewById(currentContext.getResources().getIdentifier("image", "id", currentContext.getPackageName()));
				v.setTag(holder);
			}
			else
				holder=(ViewHolder)v.getTag();
	
			final Leaders leader = leaders.get(position);
			if (leader != null) {
				holder.nickname.setText(leader.name);
				
				NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());		
				String formattedScore = nf.format(Double.parseDouble(leader.score));
				
				if(leader.feed == null)
					holder.score.setText(currentContext.getString(currentContext.getResources().getIdentifier("leadScore", "string", currentContext.getPackageName()))+"\n"+formattedScore);
				else{			
					holder.score.setText(leader.feed+":\n"+formattedScore);
				}
				holder.position.setText(leader.position);
				
				
				try {
					if(leader.imageUrl != null){
						holder.image.setTag(leader.imageUrl);
						imageManager.displayImage(leader.imageUrl, currentContext, holder.image);
					}else{
						holder.image.setVisibility(View.GONE);
						v.findViewById(currentContext.getResources().getIdentifier("item", "id", currentContext.getPackageName())).setPadding((int)(ratio * 5), (int)(ratio * 5), 0, (int)(ratio * 5));
					}
				}catch (Exception e){}
				
				LinearLayout positionView = (LinearLayout) v.findViewById(currentContext.getResources().getIdentifier("post", "id", currentContext.getPackageName()));			
				setPositionBackground(positionView);
				setRowBackground(v, position);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return v;
	}
	 
	private void setRowBackground(View v, int position){
		BeButton b = new BeButton(getContext());
		if(position % 2 == 0)
    		v.findViewById(currentContext.getResources().getIdentifier("item", "id", currentContext.getPackageName())).setBackgroundDrawable(b.setPressedBackg(
		    		new BDrawableGradient(0,(int)(ratio * 60),BDrawableGradient.LIGHT_GRAY_GRADIENT),
					new BDrawableGradient(0,(int)(ratio * 60),BDrawableGradient.HIGH_GRAY_GRADIENT),
					new BDrawableGradient(0,(int)(ratio * 60),BDrawableGradient.HIGH_GRAY_GRADIENT)));
		else
			v.findViewById(currentContext.getResources().getIdentifier("item", "id", currentContext.getPackageName())).setBackgroundDrawable(b.setPressedBackg(
		    		new BDrawableGradient(0,(int)(ratio * 60),BDrawableGradient.GRAY_GRADIENT),
					new BDrawableGradient(0,(int)(ratio * 60),BDrawableGradient.HIGH_GRAY_GRADIENT),
					new BDrawableGradient(0,(int)(ratio * 60),BDrawableGradient.HIGH_GRAY_GRADIENT)));
		
	}
	
	private void setPositionBackground(LinearLayout l){		
		 l.setBackgroundColor(Color.argb(150, 255, 255, 255));
	}
}
