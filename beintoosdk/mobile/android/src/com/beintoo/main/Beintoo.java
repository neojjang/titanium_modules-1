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
package com.beintoo.main;


import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;



import com.beintoo.activities.BeintooHome;
import com.beintoo.activities.tryBeintoo;
import com.beintoo.activities.tryDialog;
import com.beintoo.beintoosdk.BeintooPlayer;
import com.beintoo.beintoosdk.DeveloperConfiguration;
import com.beintoo.beintoosdkutility.DebugUtility;
import com.beintoo.beintoosdkutility.ErrorDisplayer;
import com.beintoo.beintoosdkutility.MessageDisplayer;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.main.managers.AchievementManager;
import com.beintoo.main.managers.GetVgoodManager;
import com.beintoo.main.managers.PlayerManager;
import com.beintoo.main.managers.SubmitScoreManager;
import com.beintoo.vgood.BeintooRecomBanner;
import com.beintoo.vgood.BeintooRecomBannerHTML;
import com.beintoo.vgood.BeintooRecomDialog;
import com.beintoo.vgood.BeintooRecomDialogHTML;
import com.beintoo.vgood.VGoodGetList;
import com.beintoo.vgood.VgoodAcceptDialog;
import com.beintoo.vgood.VgoodBanner;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.PlayerAchievement;
import com.beintoo.wrappers.PlayerScore;
import com.beintoo.wrappers.Vgood;
import com.beintoo.wrappers.VgoodChooseOne;
import com.google.beintoogson.Gson;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.LinearLayout;

 
public class Beintoo{
	public static Context currentContext;
	
	public final static int GO_HOME = 1;
	public final static int GET_MULTI_VGOOD = 6;
	public final static int GET_VGOOD_BANNER = 2;
	public final static int LOGIN_MESSAGE = 3;
	public final static int SUBMITSCORE_POPUP = 4;
	public final static int GET_VGOOD_ALERT = 7;
	public final static int GET_RECOMM_BANNER = 8;
	public final static int GET_RECOMM_ALERT = 9;
	public final static int UPDATE_USER_AGENT = 10;
	public final static int GET_RECOMM_ALERT_HTML = 11;
	public final static int GET_RECOMM_BANNER_HTML = 12;
	
	public static int VGOOD_NOTIFICATION_BANNER = 1; 
	public static int VGOOD_NOTIFICATION_ALERT = 2;
	public static LinearLayout vgood_container = null;
	
	public static Vgood vgood = null;
	public static VgoodChooseOne vgoodlist = null;
	public static BGetVgoodListener gvl = null;
	private final static int TRY_DIALOG_POPUP = 5;
	
	
	
	public static Dialog currentDialog = null;
	public static Dialog homeDialog = null;
	
	public static AtomicLong LAST_LOGIN = new AtomicLong(0);
	
	public static String[] usedFeatures = null; 
	public static String FEATURE_PROFILE = "profile";
	public static String FEATURE_LEADERBOARD = "leaderboard";
	public static String FEATURE_WALLET = "wallet";
	public static String FEATURE_CHALLENGES = "challenges";
	public static String FEATURE_ACHIEVEMENTS = "achievements";
	
	public static boolean OPEN_DASHBOARD_AFTER_LOGIN = true;
	
	public static String userAgent = null;
	
	/**
	 * Set the developer apikey
	 * 
	 * @param apikey your apikey
	 */
	public static void setApiKey(String apikey){
		DeveloperConfiguration.apiKey = apikey;
	}
	
	/** 
	 * Starts the Beintoo Main app where the user can login into Beintoo
	 * and manage their goods, profile, leaderboard etc. 
	 * 
	 * @param ctx current Context
	 */
	public static void BeintooStart(final Context ctx, boolean goToDashboard){
		currentContext = ctx;
		OPEN_DASHBOARD_AFTER_LOGIN = goToDashboard;
		
		try{
			final Gson gson = new Gson();
			boolean isLogged = PreferencesHandler.getBool("isLogged", ctx);
			String savedPlayer = PreferencesHandler.getString("currentPlayer", ctx);
			final Player currentPlayer;
			if(savedPlayer != null){
				currentPlayer = gson.fromJson(savedPlayer, Player.class);
			}else currentPlayer = null;
			
			// AVOID BAD LOGIN
			if(isLogged == true && (savedPlayer == null || savedPlayer == "")) logout(ctx);
			
			if(isLogged == true && currentPlayer.getUser() != null){
				final ProgressDialog  dialog = ProgressDialog.show(ctx, "", ctx.getString(ctx.getResources().getIdentifier("loadingBeintoo", "string", ctx.getPackageName())),true);
				Thread t = new Thread(new Runnable(){     					
            		public void run(){ 
            			try{   	
							BeintooPlayer player = new BeintooPlayer();
							// DEBUG
							DebugUtility.showLog("current saved player: "+PreferencesHandler.getString("currentPlayer", ctx));											
 
							// LOGIN TO BEINTOO							
							Player newPlayer = player.getPlayer(currentPlayer.getGuid());
							if(newPlayer.getUser().getId() != null){				
								PreferencesHandler.saveString("currentPlayer", gson.toJson(newPlayer), ctx);
								// GO HOME
								UIhandler.sendEmptyMessage(GO_HOME);
							}							
            			}catch (Exception e ){
            				dialog.dismiss();
            				e.printStackTrace();
            				ErrorDisplayer.showConnectionErrorOnThread(ErrorDisplayer.CONN_ERROR, ctx,e);
            				logout(ctx);
            			}
            			dialog.dismiss();
            		}
				});
				t.start();
			}else{
				tryBeintoo tryBe = new tryBeintoo(ctx);
				currentDialog = tryBe;
				tryBe.show();
			}
		}catch (Exception e){e.printStackTrace(); ErrorDisplayer.showConnectionError(ErrorDisplayer.CONN_ERROR, ctx, e); logout(ctx);}
	} 
	
	/**
	 * Show a dialog with the try Beintoo message. The dialog is shown interval of days passed in daysInterval
	 * 
	 * @param ctx current Context
	 * @param daysInterval interval of days you want to show the dialog
	 */
	public static void tryBeintooDialog (Context ctx, int daysInterval){
		currentContext = ctx;
		try { 
			long lastDay = PreferencesHandler.getLong("lastTryDialog", ctx);
			long now = System.currentTimeMillis();
			long daysIntervalMillis = 86400000 * daysInterval; //60000   
			long nextPopup = lastDay + daysIntervalMillis;
			boolean isLogged = PreferencesHandler.getBool("isLogged", ctx);
			
			if((lastDay == 0 || now >= nextPopup) && !isLogged){
				UIhandler.sendEmptyMessage(TRY_DIALOG_POPUP);
				PreferencesHandler.saveLong("lastTryDialog", now, ctx);
			}
		}catch (Exception e){}
	} 
	
	/** 
	 * Send a Vgood to the user it runs in background on a thread. 
	 * It retrieves the user location used to find a Vgood in nearby 
	 * 
	 * @param ctx current Context
	 * @param isMultiple if true retrieve a list of vgood in wich the player can choose a virtual good
	 */	
	public static void GetVgood(final Context ctx, final String codeID, final boolean isMultiple, final LinearLayout container, final int notificationType, final BGetVgoodListener listener){
		currentContext = ctx;			
		gvl = listener;
		GetVgoodManager gvm = new GetVgoodManager(ctx);
		gvm.GetVgood(ctx, codeID, isMultiple, container, notificationType, listener);		
	}
	
	public static void GetVgood(final Context ctx) {
		GetVgood(ctx, null, false, null, Beintoo.VGOOD_NOTIFICATION_ALERT, null);
	}
	
	public static void GetVgood(final Context ctx, final boolean isMultiple, final LinearLayout container, final int notificationType){
		GetVgood(ctx, null, isMultiple, container, notificationType, null);
	}
	
	
	
	/**
	 * Check if the user is eligible for a new virtual good
	 * 
	 * @param ctx
	 * @param el
	 */
	public static void isEligibleForVgood(final Context ctx, final BEligibleVgoodListener el){
		currentContext = ctx;
		
		GetVgoodManager gvm = new GetVgoodManager(ctx);
		gvm.isEligibleForVgood(ctx, el);		
	}
	
	
	/**
	 * Do a player login and save player data 
	 *  
	 * @param ctx current Context
	 */	
	public static void playerLogin(final Context ctx, final BPlayerLoginListener listener){
		currentContext = ctx;	
		PlayerManager pm = new PlayerManager(ctx);
		pm.playerLogin(ctx, listener);
	}
	
	public static void playerLogin(final Context ctx){
		playerLogin(ctx, null);
	}
	
	/**
	 * True if the user is logged in, false if not
	 * 
	 * @param ctx current Context
	 * @return boolean 
	 */
	public static boolean isLogged(Context ctx){
		try {
			Player currentPlayer = new Gson().fromJson(PreferencesHandler.getString("currentPlayer", ctx), Player.class);
			boolean isLogged = PreferencesHandler.getBool("isLogged", ctx);
			if(currentPlayer != null && isLogged)
				return true;
			else 
				return false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Logout a user from Beintoo
	 * 
	 * @param ctx current Context
	 */
	public static void logout(Context ctx){
		PlayerManager pm = new PlayerManager(ctx);
		pm.logout(ctx);
	}
	
	/**
	 * Submit a score with a treshold and check if the user reach that treshold. If yes automatically assign a vgood by
	 * calling getVGood
	 * 
	 * @param ctx current Context
	 * @param score score to submit
	 * @param threshold the score threshold for a new vgood
	 */
	public static void submitScoreWithVgoodCheck (final Context ctx, int score, int threshold, String codeID, boolean isMultiple,
			LinearLayout container, int notificationType, final BSubmitScoreListener slistener, final BGetVgoodListener glistener){
		currentContext = ctx;
		System.out.println("CONTEXT INSIDE: "+currentContext + " CTX: "+ctx);
		SubmitScoreManager ssm = new SubmitScoreManager();
		ssm.submitScoreWithVgoodCheck(ctx, score, threshold, codeID, isMultiple, container, notificationType, slistener, glistener);
	}
	
	public static void submitScoreWithVgoodCheck (final Context ctx, int score, int threshold){
		submitScoreWithVgoodCheck (ctx, score, threshold,null, true, null, Beintoo.VGOOD_NOTIFICATION_ALERT, null, null);
	}
	
	public static void submitScoreWithVgoodCheck (final Context ctx, int score, int threshold, boolean isMultiple,
		LinearLayout container, int notificationType){
		submitScoreWithVgoodCheck (ctx, score, threshold,null, isMultiple,container, notificationType, null, null);
	}
	
	/**
	 * Submit user score, check the user location and then call submitScoreHelper wich make the api call on a thread
	 * 
	 * @param ctx the current Context
	 * @param lastScore the score to submit
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @param showNotification if true it will show a notification to the user when receive a submit score 
	 */	
	public static void submitScore(final Context ctx, final int lastScore, final String codeID, final boolean showNotification, int gravity, final BSubmitScoreListener listener){
		currentContext = ctx;
		
		SubmitScoreManager ssm = new SubmitScoreManager();
		ssm.submitScore(ctx, lastScore, codeID, showNotification, gravity, listener);
	}
	
	public static void submitScore(Context ctx, int lastScore, boolean showNotification, int gravity){
		submitScore(ctx, lastScore, null, showNotification, gravity, null);
	}
	
	public static void submitScore(Context ctx, int lastScore, boolean showNotification, int gravity, final BSubmitScoreListener listener){
		submitScore(ctx, lastScore, null, showNotification, gravity, listener);
	}
	
	public static void submitScore(Context ctx, int lastScore, boolean showNotification){
		submitScore(ctx, lastScore, null, showNotification, Gravity.BOTTOM, null);
	}
		
	public static void submitScore(Context ctx, int lastScore, String codeID, boolean showNotification){
		submitScore(ctx, lastScore, codeID, showNotification, Gravity.BOTTOM, null);
	}	
	
	
	/**
	 * Submit multiple user score, check the user location and then call submitScoreMultipleHelper wich make the api call on a thread
	 * 
	 * @param ctx the current Context
	 * @param scores the scores to submit
	 * @param showNotification if true it will show a notification to the user when receive a submit score 
	 * @param BSubmitScoreListener callback interface 
	 */	
	public static void submitScoreMultiple(final Context ctx, Map<String,Integer> scores, final boolean showNotification, int gravity, final BSubmitScoreListener listener){
		currentContext = ctx;
		
		SubmitScoreManager ssm = new SubmitScoreManager();
		ssm.submitScoreMultiple(ctx, scores, showNotification, gravity, listener);
	}
	
	public static void submitScoreMultiple(final Context ctx, Map<String,Integer> scores, final boolean showNotification){
		submitScoreMultiple(ctx, scores, showNotification, Gravity.BOTTOM, null);
	}
	
	
	/** 
	 * Get the current player score
	 * 
	 * @param ctx
	 * @param codeID
	 * @return
	 */
	public static PlayerScore getPlayerScore(final Context ctx, final String codeID){
		currentContext = ctx;
		
		PlayerManager pm = new PlayerManager(ctx);
		return pm.getPlayerScore(ctx, codeID);
	}
	 
	public static PlayerScore getPlayerScore(final Context ctx){
		return getPlayerScore(ctx,null);
	}
	
	/** 
	 * Get the current player score with a callback
	 * 
	 * @param ctx
	 * @param codeID
	 * @return
	 */ 
	public static void getPlayerScoreAsync(final Context ctx, final String codeID, final BScoreListener listener){
		currentContext = ctx;
		
		PlayerManager pm = new PlayerManager(ctx);
		pm.getPlayerScoreAsync(ctx, codeID, listener);
	}
		 
	/**
	 *  Submit achievement score 
	 *  
	 * @param achievement the achievement id
	 * @param percentage (optional) completion percentage of the achievement
	 * @param value (optional) current value of the achievement
	 * @param showNotification show a notification to the user 
	 * @param gravity position of the notification
	 */
	public static void submitAchievementScore(Context ctx, final String achievement, final Float percentage, final Float value, 
			boolean increment, final boolean showNotification, final int gravity, final BAchievementListener listener){
		currentContext = ctx;
		AchievementManager am = new AchievementManager(ctx);
		am.submitAchievementScore(achievement, percentage, value, increment, showNotification, gravity, listener);		
	}
	
	public static void submitAchievementScore(Context ctx, final String achievement, final Float percentage, final Float value){
		submitAchievementScore(ctx, achievement, percentage, value, true, true, Gravity.BOTTOM, null);
	}
	
	public static void submitAchievementScore(Context ctx, final String achievement, final Float percentage, final Float value,final BAchievementListener listener){
		submitAchievementScore(ctx, achievement, percentage, value, true, true, Gravity.BOTTOM, listener);
	}
	
	public static void setAchievementScore(Context ctx, final String achievement, final Float percentage, final Float value){
		submitAchievementScore(ctx, achievement, percentage, value, false, true, Gravity.BOTTOM, null);
	}
	
	public static void setAchievementScore(Context ctx, final String achievement, final Float percentage, final Float value,final BAchievementListener listener){
		submitAchievementScore(ctx, achievement, percentage, value, false, true, Gravity.BOTTOM, listener);
	}
	
	
	/**
	 * Se which features to use in your app
	 * @param features an array of features avalaible features are: profile, leaderboard, wallet, challenge 
	 */
	public static void setFeaturesToUse(String[] features){
		usedFeatures = features;
	}
	public static void featureToUse(String[] features){
		setFeaturesToUse(features);
	}
	
	/*
	 * Handler for managing user interfaces in a Thread
	 */
	public static Handler UIhandler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  switch (msg.what) {	
			  	case GO_HOME: // GO HOME ON START BEINTOO (GO_HOME)
	            	BeintooHome beintooHome = new BeintooHome(currentContext);
	            	currentDialog = beintooHome;
					beintooHome.show();
	            break;
			  	case LOGIN_MESSAGE:	            	
	            	MessageDisplayer.showMessage(currentContext, msg.getData().getString("Message"), msg.getData().getInt("Gravity"));
	            break;
	            case TRY_DIALOG_POPUP:
	            	tryDialog t = new tryDialog(currentContext);
	            	t.open();
	            break;
	            case SUBMITSCORE_POPUP:
	            	MessageDisplayer.showMessage(currentContext, msg.getData().getString("Message"), msg.getData().getInt("Gravity"));
	            break;
	            case GET_VGOOD_BANNER: // GET A VGOOD (GET_VGOOD)
					VgoodBanner vb = new VgoodBanner(currentContext,vgood_container, vgoodlist);
	            	vb.show();
	            break;
	            case GET_VGOOD_ALERT: // GET A VGOOD (GET_VGOOD)
	            	System.out.println("CONTEXT INSIDE: "+currentContext);
	            	VgoodAcceptDialog d = new VgoodAcceptDialog(currentContext, vgoodlist);
	  			  	d.show();
	            break;
	            case GET_MULTI_VGOOD:
	            	VGoodGetList getVgoodList = new VGoodGetList(currentContext, vgoodlist);
		    		Beintoo.currentDialog = getVgoodList;
		    		getVgoodList.show();
	            break;
	            case GET_RECOMM_BANNER:
	            	BeintooRecomBanner brb = new BeintooRecomBanner(currentContext,vgood_container,vgoodlist,gvl);
	            	brb.loadBanner();
	            break;
	            case GET_RECOMM_ALERT:
	            	BeintooRecomDialog brd = new BeintooRecomDialog(currentContext,vgoodlist,gvl);
	            	brd.loadAlert();
	            break;	
	            case GET_RECOMM_BANNER_HTML:
	            	BeintooRecomBannerHTML brbh = new BeintooRecomBannerHTML(currentContext,vgood_container,vgoodlist,gvl);
	            	brbh.loadRecomm();
	            break;
	            case GET_RECOMM_ALERT_HTML:
	            	BeintooRecomDialogHTML brah = new BeintooRecomDialogHTML(currentContext,vgoodlist,gvl);
	            	brah.loadAlert();
	            break;
	            case UPDATE_USER_AGENT:
	            	getUA();
	            break;
		    }
	        super.handleMessage(msg);
		  }
	};
	
	public static void getUA(){
		try{
			WebView wv = new WebView(Beintoo.currentContext);		
			userAgent = wv.getSettings().getUserAgentString();		
			wv.pauseTimers();		
			wv.destroy();
		}catch (Exception e){e.printStackTrace();}
	}
	
	public static void clearBeintoo (){
		if(currentDialog != null)
			currentDialog.dismiss();
	} 
	
	public static void realodBeintoo (){
		if(currentDialog != null)
			currentDialog.show();
	}
	
	/** 
	 *  Listener for playerLogin callback
	 */
	public static interface BPlayerLoginListener {
		public void onComplete(Player p);
		public void onError();
	}
	
	/** 
	 *  Listener for getVgood callback
	 */	
	public static interface BGetVgoodListener {
		 public void onComplete(VgoodChooseOne v);
		 public void isOverQuota();
		 public void nothingToDispatch();
		 public void onError();
	}
	
	/** 
	 *  Listener for submitScore callback
	 */	
	public static interface BSubmitScoreListener {
		 public void onComplete();		 
		 public void onBeintooError(Exception e);
	 }
	
	/** 
	 *  Listener for getPlayerScore callback
	 */	
	 public static interface BScoreListener {
		 public void onComplete(PlayerScore p);
		 public void onBeintooError(Exception e);
	 }
	 
	 /** 
	  *  Listener for submitAchievementScore callback
	  */
	 public static interface BAchievementListener {
		 public void onComplete(List<PlayerAchievement> a);
		 public void onAchievementUnlocked(List<PlayerAchievement> a);
		 public void onBeintooError(Exception e);
	 }
	 
	 /**
	  * Listener for isEligible callback 
	  */
	 public static interface BEligibleVgoodListener {
		 public void onComplete(com.beintoo.wrappers.Message msg, Player p);
		 public void vgoodAvailable(Player p);
		 public void isOverQuota(Player p);
		 public void nothingToDispatch(Player p);
		 public void onError();
	 }
}
