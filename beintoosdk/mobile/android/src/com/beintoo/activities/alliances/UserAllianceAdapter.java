package com.beintoo.activities.alliances;

import java.util.ArrayList;
import java.util.List;
import com.beintoo.activities.alliances.UserAlliance.ListItem;
import com.beintoo.beintoosdkui.BeButton;
import com.beintoo.beintoosdkutility.BDrawableGradient;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserAllianceAdapter extends ArrayAdapter<ListItem>{
	private List<ListItem> list;
	private Context currentContext;
	private final double ratio;
	
	public UserAllianceAdapter(Context context, ArrayList<ListItem> list) {
		super(context, 0, list);
		this.list = list;
		this.currentContext = context;
		this.ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		try {			
			final ListItem item = list.get(position);	
			View v = new CustomAdapterView(currentContext,item);
			BeButton b = new BeButton(getContext());
			if(position % 2 == 0)
	    		v.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.LIGHT_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			else
				v.setBackgroundDrawable(b.setPressedBackg(
			    		new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.HIGH_GRAY_GRADIENT),
						new BDrawableGradient(0,(int)(ratio * 40),BDrawableGradient.HIGH_GRAY_GRADIENT)));
			return v;		
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	private class CustomAdapterView extends LinearLayout {
		public CustomAdapterView(Context context, final ListItem l)
		{
			super(context);
			
			this.setOrientation(LinearLayout.HORIZONTAL);
			this.setGravity(Gravity.CENTER_VERTICAL);
			this.setPadding((int)(ratio * 10), (int)(ratio * 10), 0, (int)(ratio * 10));
			
			TextView left = new TextView(context);
			left.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			left.setLayoutParams(params);
			left.setGravity(Gravity.LEFT);
			left.setTextColor(Color.parseColor("#787A77"));
			left.setText(l.leftText);
			addView(left);
			
			if(l.rightText != null){
				TextView right = new TextView(context);
				right.setGravity(Gravity.RIGHT);
				right.setPadding(0, 0, (int)(ratio * 10), 0);
				right.setTextColor(Color.parseColor("#787A77"));
				right.setText(l.rightText);
				addView(right);
				
			}

			if(l.showCheck){
				CheckBox cb = new CheckBox(context);
				cb.setGravity(Gravity.RIGHT);
				cb.setLayoutParams(new LinearLayout.LayoutParams(40,40));
				cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						l.isChecked = true;
					}					
				});
				if(l.isChecked) cb.setChecked(true);
				addView(cb);
			}
		}
	} 
	
	@Override
	public ListItem getItem(int position) {
		return super.getItem(position);
	}

}
