package com.beintoo.activities.leaderboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


import com.beintoo.activities.Friends;
import com.beintoo.activities.UserProfile;
import com.beintoo.activities.alliances.UserAlliance;
import com.beintoo.beintoosdk.BeintooAlliances;
import com.beintoo.beintoosdk.BeintooApp;
import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdkutility.ApiCallException;
import com.beintoo.beintoosdkutility.BDrawableGradient;
import com.beintoo.beintoosdkutility.ImageManager;
import com.beintoo.beintoosdkutility.MessageDisplayer;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.LeaderboardContainer;
import com.beintoo.wrappers.Player;
import com.google.beintoogson.Gson;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class Leaderboard extends Dialog implements OnItemClickListener, OnScrollListener{
	
	private Dialog current;
	private Context currentContext;
	
	private ArrayList<String> usersExts;
	private ArrayList<String> usersNicks;
	private ArrayList<Leaders> leaderslist;
	private Map<String, LeaderboardContainer> leaders;
	
	private String codeID; // THE CODE ID OF THE SELECTED CONTEST
	private String leader_kind;
	
	private ImageManager imageManager;
	private ListView listView;
	private LeaderboardAdapter adapter;
	
	private Player player = null;
	private Leaders currentUser = null;
	
	private final double ratio;
	private final int SHOW_MESSAGE = 1;
	private final int MAX_REQ_ROWS = 20;
	private int currentPageRows = 1;   
		
	private boolean isLoading = false;
	private boolean hasReachedEnd = false;
	
	
	public Leaderboard(Context context, Map<String, LeaderboardContainer> l, String kind) {
		super(context, context.getResources().getIdentifier("ThemeBeintoo", "style", context.getPackageName()));
		setContentView(context.getResources().getIdentifier("leaderboardlist", "layout", context.getPackageName()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		current = this;
		currentContext = context;
		leaders = l;
		leader_kind = kind;
		
		// GETTING DENSITY PIXELS RATIO
		ratio = (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160d);	
		// SET UP LAYOUTS
		double pixels = ratio * 40;
		RelativeLayout beintooBar = (RelativeLayout) findViewById(current.getContext().getResources().getIdentifier("beintoobarsmall", "id", current.getContext().getPackageName()));
		beintooBar.setBackgroundDrawable(new BDrawableGradient(0,(int)pixels,BDrawableGradient.BAR_GRADIENT));
		
		 
        listView = (ListView) findViewById(current.getContext().getResources().getIdentifier("listView", "id", current.getContext().getPackageName()));
        listView.setVisibility(LinearLayout.GONE);
        listView.setCacheColorHint(0);
        
		LinearLayout content = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("goodcontent", "id", current.getContext().getPackageName()));
        content.addView(progressLoading());       
		
		LinearLayout tip = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("tip", "id", current.getContext().getPackageName()));
		tip.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio*27),BDrawableGradient.LIGHT_GRAY_GRADIENT));
		if(!Beintoo.isLogged(context)) // IF THE USER IS NOT LOGGED HIDE TIPS FOR CHALLENGE
			tip.setVisibility(LinearLayout.GONE);
		
		if(leader_kind != null && leader_kind.equals("ALLIANCES")){
			findViewById(current.getContext().getResources().getIdentifier("tip", "id", current.getContext().getPackageName())).setVisibility(View.GONE);
			findViewById(current.getContext().getResources().getIdentifier("spacer", "id", current.getContext().getPackageName())).setVisibility(View.GONE);
		}
		
		imageManager = new ImageManager(currentContext);
		
		usersExts = new ArrayList<String>();
		usersNicks = new ArrayList<String>();		                
        
		listView.setOnItemClickListener(this);        
        listView.setOnScrollListener(this);
        
        new Handler().postDelayed(r, 200); // GIVE THE TIME TO DISPLAY THE DIALOG UNTIL LOCK THE UI
	}
	
	private Runnable r = new Runnable(){
		@Override
		public void run() {
			try {
				LinearLayout content = (LinearLayout) findViewById(current.getContext().getResources().getIdentifier("goodcontent", "id", current.getContext().getPackageName()));
				leaderslist = getLeaders();
		        content.findViewWithTag(1000).setVisibility(LinearLayout.GONE);
				listView.setVisibility(LinearLayout.VISIBLE);
		        
		        // SET THE CURRENT USER
		        if(currentUser != null){        
		        	View header = setCurrentUserRow();
		        	listView.addHeaderView(header, null, false);
		        }else listView.addHeaderView(new View(currentContext), null, false); // empty view, without we can't add a footer :/
		        
		        
		        adapter = new LeaderboardAdapter(currentContext, current.getContext().getResources().getIdentifier("leaderboarditem", "layout", current.getContext().getPackageName()), leaderslist);
		        listView.setAdapter(adapter);
			}catch(Exception e){ 
				e.printStackTrace(); 
			}
		}
	};
	
	private ArrayList<Leaders> getLeaders (){
		final ArrayList<Leaders> lead = new ArrayList<Leaders>();
		
		try {
			player = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", getContext()), Player.class);
			
			Iterator<?> it = leaders.entrySet().iterator();		
			int selectedContest = Integer.parseInt(PreferencesHandler.getString("selectedContest", currentContext));
			// SET TITLE
			TextView contestName = (TextView)findViewById(current.getContext().getResources().getIdentifier("dialogTitle", "id", current.getContext().getPackageName()));
	
			int count = 0;
			
			
		    while (it.hasNext()) {	     	
		    	@SuppressWarnings("unchecked")
				Map.Entry<String, LeaderboardContainer> pairs = (Map.Entry<String, LeaderboardContainer>) it.next();		    	
			    LeaderboardContainer arr = pairs.getValue();			    		
		    	if(selectedContest == count){
		    		contestName.setText(arr.getContest().getName());
		    		String feed = arr.getContest().getFeed();
		    		codeID = pairs.getKey();
		    		 
		    		// ADD THE CURRENT USER
		    		if(arr.getCurrentUser() != null && arr.getCurrentUser().getUser() != null){
		    			currentUser = new Leaders(arr.getCurrentUser().getUser().getUsersmallimg(),
		    					arr.getCurrentUser().getUser().getNickname(), arr.getCurrentUser().getScore().toString(),
			    				feed,arr.getCurrentUser().getPos().toString(), true);
		    		}else if(arr.getCurrentUser() != null && arr.getCurrentUser().getAlliance() != null){
		    			currentUser = new Leaders(null,
		    					arr.getCurrentUser().getAlliance().getName(), arr.getCurrentUser().getScore().toString(),
			    				feed,arr.getCurrentUser().getPos().toString(), true);
		    		}
		    		
		    		// ADD THE LEADERBOARD USERS
			    	for(int i = 0; i<arr.getLeaderboard().size(); i++){
			    		if(leader_kind == null || leader_kind.equals("FRIENDS")){
			    			Leaders l = new Leaders(arr.getLeaderboard().get(i).getUser().getUsersmallimg(),
				    				arr.getLeaderboard().get(i).getUser().getNickname(), arr.getLeaderboard().get(i).getScore().toString(),
				    				feed,arr.getLeaderboard().get(i).getPos().toString(), false);
				    		lead.add(l);
			    		}else if(leader_kind.equals("ALLIANCES")){
			    			Leaders l = new Leaders(null,
				    				arr.getLeaderboard().get(i).getAlliance().getName(), arr.getLeaderboard().get(i).getScore().toString(),
				    				feed,arr.getLeaderboard().get(i).getPos().toString(), false);
				    		lead.add(l);			    			
			    		}
			    		
			    		// ADD THE USER EXT TO THE USERS ARRAY
			    		if(leader_kind == null || leader_kind.equals("FRIENDS")){
			    			usersExts.add(arr.getLeaderboard().get(i).getUser().getId());
							usersNicks.add(arr.getLeaderboard().get(i).getUser().getNickname());
			    		}else if(leader_kind.equals("ALLIANCES")){
			    			// ADD THE ALLIANCE ID TO THIS ARRAY AND USE IT IN THE ON ITEM CLICK
			    			usersExts.add(arr.getLeaderboard().get(i).getAlliance().getId());
			    		}
			    	}
		    	}
		    	
		    	count++;
		    }
		    
		    return lead;
		}catch (Exception e){
			e.printStackTrace();
			return lead;
		}
	}

	public void startLoadingMore (){
		new Thread(new Runnable(){      
    		public void run(){
    			try{       		
    				Map<String, LeaderboardContainer> moreleaders = null;
    				if(leader_kind == null || leader_kind.equals("FRIENDS")){
    					BeintooApp app = new BeintooApp();
    					String userExt = null;
        				if(player != null && player.getUser() != null)
        					userExt = player.getUser().getId();        				
    					moreleaders = app.getLeaderboard(userExt, leader_kind, codeID, currentPageRows*MAX_REQ_ROWS, MAX_REQ_ROWS);
    				}else if(leader_kind.equals("ALLIANCES")){
    					BeintooAlliances ba = new BeintooAlliances();
    					String allianceExtId = null;
        				if(player != null && player.getAlliance() != null)
        					allianceExtId = player.getAlliance().getId();    					
    					moreleaders = ba.getLeaderboard(allianceExtId, null, null, currentPageRows*MAX_REQ_ROWS, MAX_REQ_ROWS, null);
    				}
    				
    				if(moreleaders != null && moreleaders.size() > 0){
    					loadMore(moreleaders);
    					UIHandler.post(new Runnable(){
    						@Override
    						public void run() {
    							listView.removeFooterView(listView.findViewWithTag(2000));
    							adapter.notifyDataSetChanged();							
    					}}); 
    				}else{
    					hasReachedEnd = true;
    					System.out.println("vuoto");
    					UIHandler.post(new Runnable(){
    						@Override
    						public void run() {
    							listView.removeFooterView(listView.findViewWithTag(2000));							
    					}});
    				}
    				
    				isLoading = false;
    			}catch(Exception e){e.printStackTrace();}
    		}
		}).start();	
	}
	
	private void loadMore(Map<String, LeaderboardContainer> moreleaders){		
		try {
			Iterator<?> it = moreleaders.entrySet().iterator();		
			int count = 0;
		    while (it.hasNext()) {	    	
		    	@SuppressWarnings("unchecked")
				Map.Entry<String, LeaderboardContainer> pairs = (Map.Entry<String, LeaderboardContainer>) it.next();		    	
			    LeaderboardContainer arr = pairs.getValue();			    	
			    	
	    		String feed = arr.getContest().getFeed();
	    		codeID = pairs.getKey();
	    		// ADD THE LEADERBOARD USERS
	    		for(int i = 0; i<arr.getLeaderboard().size(); i++){
	    			if(leader_kind == null || leader_kind.equals("FRIENDS")){
	    				Leaders l = new Leaders(arr.getLeaderboard().get(i).getUser().getUsersmallimg(),
			    				arr.getLeaderboard().get(i).getUser().getNickname(), arr.getLeaderboard().get(i).getScore().toString(),
			    				feed,arr.getLeaderboard().get(i).getPos().toString(), false);
		    			leaderslist.add(l);
		    		}else if(leader_kind.equals("ALLIANCES")){
		    			Leaders l = new Leaders(null,
			    				arr.getLeaderboard().get(i).getAlliance().getName(), arr.getLeaderboard().get(i).getScore().toString(),
			    				feed,arr.getLeaderboard().get(i).getPos().toString(), false);
			    		leaderslist.add(l);		    			
		    		}
	    			
					// ADD THE USER EXT TO THE USERS ARRAY	    			
	    			if(leader_kind == null || leader_kind.equals("FRIENDS")){		    		
	    				usersExts.add(arr.getLeaderboard().get(i).getUser().getId());
						usersNicks.add(arr.getLeaderboard().get(i).getUser().getNickname());
	    			}else if(leader_kind.equals("ALLIANCES")){
		    			// ADD THE ALLIANCE ID TO THIS ARRAY AND USE IT IN THE ON ITEM CLICK
		    			usersExts.add(arr.getLeaderboard().get(i).getAlliance().getId());
		    		}
		    	}	 	
		    	count++;
		    }
		    currentPageRows++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {			
		try {
			if(leader_kind == null || leader_kind.equals("FRIENDS")){
				if(listView.getHeaderViewsCount() > 0)
					position--;
				try {
					if(player != null && player.getUser() != null){
						if(!player.getUser().getId().equals(usersExts.get(position))){			    						
							showOptionAlert(position);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(leader_kind.equals("ALLIANCES")){
				UserAlliance ua = new UserAlliance(currentContext, UserAlliance.SHOW_ALLIANCE, usersExts.get(position-1));
				ua.show();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private View setCurrentUserRow(){
		View header1 = getLayoutInflater().inflate(current.getContext().getResources().getIdentifier("leaderboarditem", "layout", current.getContext().getPackageName()), null, false);
		
		TextView p = (TextView) header1.findViewById(current.getContext().getResources().getIdentifier("position", "id", current.getContext().getPackageName()));
		p.setTextColor(Color.WHITE);
		p.setText(currentUser.position);		
		TextView nickname = (TextView) header1.findViewById(current.getContext().getResources().getIdentifier("nickname", "id", current.getContext().getPackageName()));
		nickname.setText(currentUser.name);
		TextView score = (TextView) header1.findViewById(current.getContext().getResources().getIdentifier("score", "id", current.getContext().getPackageName()));
		
		NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());		
		String formattedScore = nf.format(Double.parseDouble(currentUser.score));
		
		if(currentUser.feed == null)
			score.setText(currentContext.getString(current.getContext().getResources().getIdentifier("leadScore", "string", current.getContext().getPackageName()))+"\n"+formattedScore);
		else			
			score.setText(currentUser.feed+":\n"+formattedScore);
		
		header1.findViewById(current.getContext().getResources().getIdentifier("image", "id", current.getContext().getPackageName())).setVisibility(LinearLayout.GONE);
		header1.findViewById(current.getContext().getResources().getIdentifier("whiteline", "id", current.getContext().getPackageName())).setVisibility(LinearLayout.GONE);
		try {
			if(currentUser.imageUrl != null){
				ImageView imageView = new ImageView(currentContext);
				imageView.setTag(currentUser.imageUrl);
				int size = (int) (50 * currentContext.getResources().getDisplayMetrics().density + 0.5f);
				imageView.setMaxHeight(size);
				imageView.setMaxWidth(size);
				imageView.setMinimumHeight(size);
				imageView.setMinimumWidth(size);
				imageManager.displayImage(currentUser.imageUrl, currentContext, imageView);
				
				LinearLayout a = (LinearLayout)header1.findViewById(current.getContext().getResources().getIdentifier("item", "id", current.getContext().getPackageName()));
				a.addView(imageView,0);
			}else{
				header1.findViewById(current.getContext().getResources().getIdentifier("item", "id", current.getContext().getPackageName())).setPadding((int)(ratio * 5), (int)(ratio * 5), 0, (int)(ratio * 5));
			}
		}catch(Exception e){ }
		
		LinearLayout positionView = (LinearLayout) header1.findViewById(current.getContext().getResources().getIdentifier("post", "id", current.getContext().getPackageName()));
		positionView.setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 35),BDrawableGradient.HIGH_GRAY_GRADIENT));
		header1.findViewById(current.getContext().getResources().getIdentifier("item", "id", current.getContext().getPackageName())).setBackgroundDrawable(new BDrawableGradient(0,(int)(ratio * 60),BDrawableGradient.LIGHT_GRAY_GRADIENT));

		LinearLayout spacer = (LinearLayout) header1.findViewById(current.getContext().getResources().getIdentifier("space", "id", current.getContext().getPackageName()));
		spacer.setVisibility(LinearLayout.VISIBLE);

		return header1;
	}
	
	public void sendChallenge  (final String userExtFrom, final String userExtTo, final String action, final int id){		
		new Thread(new Runnable(){      
    		public void run(){
    			try{             				
    				BeintooUser user = new BeintooUser();
    				user.challenge(userExtFrom, userExtTo, action, codeID);
    				// SHOW THE NOTIFICATION
    				Bundle b = new Bundle();
    				b.putString("msg",getContext().getString(current.getContext().getResources().getIdentifier("challSent", "string", current.getContext().getPackageName()))+usersNicks.get(id));
    				final Message msg = new Message();
    				msg.what = SHOW_MESSAGE;
    				msg.setData(b);
    				UIHandler.post(new Runnable(){
						@Override
						public void run() {
							MessageDisplayer.showMessage(currentContext, msg.getData().getString("msg"), Gravity.BOTTOM);	
						}    					
    				});
    			}catch (ApiCallException e){
    				// SHOW THE NOTIFICATION
    				Bundle b = new Bundle();
    				b.putString("msg",e.getMessage());
    				final Message msg = new Message();
    				msg.what = SHOW_MESSAGE;
    				msg.setData(b);
    				UIHandler.post(new Runnable(){
						@Override
						public void run() {
							MessageDisplayer.showMessage(currentContext, msg.getData().getString("msg"), Gravity.BOTTOM);	
						}    					
    				}); 
    			}catch (Exception e){
    				e.printStackTrace();
    			}
    			
    		}
		}).start();		
	}
	
	public void showOptionAlert(final int row){
		
		final CharSequence[] items = {current.getContext().getString(current.getContext().getResources().getIdentifier("leadSendChall", "string", current.getContext().getPackageName())), 
				current.getContext().getString(current.getContext().getResources().getIdentifier("leadViewProfile", "string", current.getContext().getPackageName())), current.getContext().getString(current.getContext().getResources().getIdentifier("leadAddFriend", "string", current.getContext().getPackageName()))};
		AlertDialog.Builder builder = new AlertDialog.Builder(current.getContext());
		builder.setTitle(current.getContext().getString(current.getContext().getResources().getIdentifier("vgoodchoosedialog", "string", current.getContext().getPackageName())));
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	final Player p = player;
		        if(item == 0){ 
		        	try {
		        		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		        		builder.setMessage(getContext().getString(current.getContext().getResources().getIdentifier("challSend", "string", current.getContext().getPackageName())))
		        		       .setCancelable(false)
		        		       .setPositiveButton(getContext().getString(current.getContext().getResources().getIdentifier("yes", "string", current.getContext().getPackageName())), new DialogInterface.OnClickListener() {
		        		           public void onClick(DialogInterface dialog, int id) {		 		        		        	   		       			
		        		       			sendChallenge(p.getUser().getId(),usersExts.get(row),"INVITE", row);  
		        		           }
		        		       })
		        		       .setNegativeButton(getContext().getString(current.getContext().getResources().getIdentifier("no", "string", current.getContext().getPackageName())), new DialogInterface.OnClickListener() {
		        		           public void onClick(DialogInterface dialog, int id) {
		        		        	   
		        		           }
		        		       });
		        		AlertDialog alert = builder.create();
		        		alert.show();
		        	}catch(Exception e){e.printStackTrace();}
		        }else if(item == 1){ 
		        	try {
		        		UserProfile userProfile = new UserProfile(getContext(),usersExts.get(row));
		    			userProfile.show();
		        	}catch(Exception e){e.printStackTrace();}
		        }else if(item == 2){ 
		        	try {
		        		Friends f = new Friends(current.getContext(),current,0,0);
		        		f.friendshipThread(usersExts.get(row), "invite");
		        	}catch(Exception e){e.printStackTrace();}		        	
		        }
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private ProgressBar progressLoading (){
		ProgressBar pb = new ProgressBar(getContext());
		pb.setIndeterminateDrawable(getContext().getResources().getDrawable(current.getContext().getResources().getIdentifier("progress", "drawable", current.getContext().getPackageName())));
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int)(ratio * 100), 0, 0);
		params.gravity = Gravity.CENTER;
		params.height = (int)(ratio * 45);
		params.width = (int)(ratio * 45);
		pb.setLayoutParams(params); 
		pb.setTag(1000);
		return pb;
	}
	
	private LinearLayout loadingFooter(){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(35, 35);
		ProgressBar pb = new ProgressBar(getContext());
		pb.setIndeterminateDrawable(getContext().getResources().getDrawable(current.getContext().getResources().getIdentifier("progress", "drawable", current.getContext().getPackageName())));				
		pb.setLayoutParams(params);	            	            
        LinearLayout loading = new LinearLayout(currentContext);
        loading.addView(pb);
        loading.setPadding(0, 10, 0, 10);
        loading.setGravity(Gravity.CENTER);
        loading.setTag(2000);
        return loading;
	}
	
	public class Leaders {
		String imageUrl;
		String name;
		String score;
		String feed;
		String position;
		boolean isCurrentUser;
		
		public Leaders(String image, String name, String score, String feed,String position, boolean isCurrentUser) {
			this.imageUrl = image;
			this.name = name;
			this.score = score;
			this.feed = feed;
			this.position = position;
			this.isCurrentUser = isCurrentUser;
		}
	} 
 
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)  {
		try {
			if(!isLoading && !hasReachedEnd){
				if(firstVisibleItem != 0 && (firstVisibleItem > totalItemCount - 10)){
					isLoading=true;
		            listView.addFooterView(loadingFooter());
					startLoadingMore();
				}		
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		
	}
	
	Handler UIHandler = new Handler(){};
}
