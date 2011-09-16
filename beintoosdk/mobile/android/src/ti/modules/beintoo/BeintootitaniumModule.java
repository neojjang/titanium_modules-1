/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.modules.beintoo;

import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiContext;

import android.view.Gravity;

import com.beintoo.beintoosdkutility.BeintooSdkParams;
import com.beintoo.beintoosdkutility.DebugUtility;
import com.beintoo.main.Beintoo;
import com.beintoo.main.Beintoo.BAchievementListener;
import com.beintoo.main.Beintoo.BGetVgoodListener;
import com.beintoo.main.Beintoo.BPlayerLoginListener;
import com.beintoo.main.Beintoo.BScoreListener;
import com.beintoo.main.Beintoo.BSubmitScoreListener;
import com.beintoo.main.managers.PlayerManager;
import com.beintoo.main.managers.SubmitScoreManager;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.PlayerAchievement;
import com.beintoo.wrappers.PlayerScore;
import com.beintoo.wrappers.VgoodChooseOne;


@Kroll.module(name="Beintootitanium", id="ti.beintoo")
public class BeintootitaniumModule extends KrollModule
{
	TiContext _tiContext;	
	public static Api Api;
	
	public BeintootitaniumModule(TiContext tiContext) {		
		super(tiContext);
		_tiContext = tiContext;		
		Api = new Api();
	}
	
	@Kroll.method
	public void setApiKey(String apikey){
		Beintoo.setApiKey(apikey);
	}
	
	@Kroll.method
	public void setUseSandbox(boolean u){		
		BeintooSdkParams.useSandbox = u;
	}
	
	@Kroll.method
	public void setDebug(boolean u){			
		DebugUtility.isDebugEnable = u;
	}
	
	@Kroll.getProperty
	public Api Api() {
	     return Api;
	}
	
	/**
	 * CORE METHODS
	 */
	
	@Kroll.method
	public void playerLogin(){
		final BPlayerLoginListener listener = new BPlayerLoginListener(){
					@Override
			public void onComplete(Player p) {
				KrollDict data = new KrollDict();
				data.put("player", p);
				fireEvent("onLogin", data);
			}

			@Override
			public void onError() {
				fireEvent("onError", new KrollDict());
			}			
		};
		Beintoo.currentContext = _tiContext.getAndroidContext();
		PlayerManager pm = new PlayerManager(TiContext.getCurrentTiContext().getActivity().getApplicationContext());
		pm.playerLogin(_tiContext.getAndroidContext(), listener);
	}
	
	@Kroll.method
	public void submitScore(final int lastScore, final String codeID, final boolean showNotification){
		final BSubmitScoreListener listener = new BSubmitScoreListener(){
			@Override
			public void onComplete() {
				fireEvent("onSubmitComplete", new KrollDict());
			}
			@Override
			public void onBeintooError(Exception e) {
				fireEvent("onError", new KrollDict());
			}			
		};
		Beintoo.currentContext = _tiContext.getAndroidContext();
		SubmitScoreManager ssm = new SubmitScoreManager();
		ssm.submitScore(_tiContext.getAndroidContext(), lastScore, null, true, Gravity.BOTTOM, listener);
	}
	
	@Kroll.method
	public void submitScoreWithVgoodCheck(final int lastScore, final int threshold, final String codeID){
		final BSubmitScoreListener slistener = new BSubmitScoreListener(){
			@Override
			public void onComplete() {
				fireEvent("onSubmitComplete", new KrollDict());
			}
			@Override
			public void onBeintooError(Exception e) {
				fireEvent("onError", new KrollDict());
			}			
		};
		
		final BGetVgoodListener glistener = new BGetVgoodListener(){
			@Override
			public void onComplete(VgoodChooseOne v) {
				KrollDict data = new KrollDict();
				data.put("Vgood", v);
				fireEvent("onVgood", data);
			}

			@Override
			public void isOverQuota() {
				fireEvent("onVgoodOverquota", new KrollDict());
			}

			@Override
			public void nothingToDispatch() {
				fireEvent("onVgoodNothingToDispatch", new KrollDict());
			}

			@Override
			public void onError() {
				fireEvent("onError", new KrollDict());
			}
		};
		
		Beintoo.currentContext = _tiContext.getAndroidContext();
		SubmitScoreManager ssm = new SubmitScoreManager();
		ssm.submitScoreWithVgoodCheck(_tiContext.getAndroidContext(), lastScore, threshold, codeID, false, null, Beintoo.VGOOD_NOTIFICATION_ALERT, slistener, glistener);
	}
	
	@Kroll.method
	public boolean isLogged(){
		Beintoo.currentContext = _tiContext.getAndroidContext();
		return Beintoo.isLogged(_tiContext.getAndroidContext());
	}
	
	@Kroll.method
	public void logout(){
		Beintoo.currentContext = _tiContext.getAndroidContext();
		Beintoo.logout(_tiContext.getAndroidContext());
	}
	
	@Kroll.method
	public void beintooStart(boolean goToDashboard){
		Beintoo.currentContext = _tiContext.getAndroidContext();
		Beintoo.BeintooStart(_tiContext.getAndroidContext(), goToDashboard);
	}
	
	@Kroll.method
	public void getPlayerScore(final String codeID){
		BScoreListener listener = new BScoreListener(){

			@Override
			public void onComplete(PlayerScore p) {
				KrollDict data = new KrollDict();
				data.put("PlayerScore", p);
				fireEvent("onPlayerScore", data);
			}

			@Override
			public void onBeintooError(Exception e) {
				fireEvent("onError", new KrollDict());
			}
			
		};
		
		Beintoo.currentContext = _tiContext.getAndroidContext();
		Beintoo.getPlayerScoreAsync(_tiContext.getAndroidContext(), codeID, listener);
	}
	
	@Kroll.method
	public void submitAchievementScore(final String achievement, final Float percentage, final Float value, final boolean showNotification){
		final BAchievementListener listener = new BAchievementListener(){

			@Override
			public void onComplete(List<PlayerAchievement> a) {
				KrollDict data = new KrollDict();
				data.put("PlayerAchievement", a);
				fireEvent("onPlayerAchievement", data);
			}

			@Override
			public void onAchievementUnlocked(List<PlayerAchievement> a) {
				KrollDict data = new KrollDict();
				data.put("PlayerAchievement", a);
				fireEvent("onPlayerAchievementUnlocked", data);
			}

			@Override
			public void onBeintooError(Exception e) {
				fireEvent("onError", new KrollDict());
			}			
		}; 
		
		Beintoo.currentContext = _tiContext.getAndroidContext();
		Beintoo.submitAchievementScore(_tiContext.getAndroidContext(), achievement, percentage, value,listener);		
	}
	
	@Kroll.method
	public void setAchievementScore(final String achievement, final Float percentage, final Float value, final boolean showNotification){
		final BAchievementListener listener = new BAchievementListener(){

			@Override
			public void onComplete(List<PlayerAchievement> a) {
				KrollDict data = new KrollDict();
				data.put("PlayerAchievement", a);
				fireEvent("onPlayerAchievement", data);
			}

			@Override
			public void onAchievementUnlocked(List<PlayerAchievement> a) {
				KrollDict data = new KrollDict();
				data.put("PlayerAchievement", a);
				fireEvent("onPlayerAchievementUnlocked", data);
			}

			@Override
			public void onBeintooError(Exception e) {
				fireEvent("onError", new KrollDict());
			}			
		}; 
		
		Beintoo.currentContext = _tiContext.getAndroidContext();
		Beintoo.setAchievementScore(_tiContext.getAndroidContext(), achievement, percentage, value, listener);		
	}
	
	@Kroll.method
	public void GetVgood(final String codeID, final boolean isMultiple){
		BGetVgoodListener listener = new BGetVgoodListener(){

			@Override
			public void onComplete(VgoodChooseOne v) {
				KrollDict data = new KrollDict();
				data.put("Vgood", v);
				fireEvent("onVgood", data);
			}

			@Override
			public void isOverQuota() {
				fireEvent("onVgoodOverquota", new KrollDict());
			}

			@Override
			public void nothingToDispatch() {
				fireEvent("onVgoodNothingToDispatch", new KrollDict());
			}

			@Override
			public void onError() {
				fireEvent("onError", new KrollDict());
			}
			
		};
		
		Beintoo.currentContext = _tiContext.getAndroidContext();
		Beintoo.GetVgood(_tiContext.getAndroidContext(), codeID, isMultiple, null, Beintoo.VGOOD_NOTIFICATION_ALERT, listener);
	}
}