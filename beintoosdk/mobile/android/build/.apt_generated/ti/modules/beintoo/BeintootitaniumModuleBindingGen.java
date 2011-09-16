/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.modules.beintoo;

import org.appcelerator.kroll.KrollArgument;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollConverter;
import org.appcelerator.kroll.KrollInvocation;
import org.appcelerator.kroll.KrollMethod;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollProxyBinding;
import org.appcelerator.kroll.KrollModuleBinding;
import org.appcelerator.kroll.KrollDynamicProperty;
import org.appcelerator.kroll.KrollReflectionProperty;
import org.appcelerator.kroll.KrollInjector;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollDefaultValueProvider;
import org.appcelerator.kroll.util.KrollReflectionUtils;
import org.appcelerator.kroll.util.KrollBindingUtils;
import org.appcelerator.titanium.kroll.KrollBridge;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.util.Log;

import org.mozilla.javascript.Scriptable;

import java.util.HashMap;

import ti.modules.beintoo.BeintootitaniumModule;


/* WARNING: this code is generated for binding methods in Android */
public class BeintootitaniumModuleBindingGen
	extends org.appcelerator.kroll.KrollModuleBindingGen
{
	private static final String TAG = "BeintootitaniumModuleBindingGen";

	private static final String CREATE_ = "create";
	private static final String DYNPROP_Api = "Api";
	private static final String METHOD_GetVgood = "GetVgood";
	private static final String METHOD_submitAchievementScore = "submitAchievementScore";
	private static final String METHOD_submitScore = "submitScore";
	private static final String METHOD_setAchievementScore = "setAchievementScore";
	private static final String METHOD_getPlayerScore = "getPlayerScore";
	private static final String METHOD_isLogged = "isLogged";
	private static final String METHOD_setUseSandbox = "setUseSandbox";
	private static final String METHOD_beintooStart = "beintooStart";
	private static final String METHOD_logout = "logout";
	private static final String METHOD_submitScoreWithVgoodCheck = "submitScoreWithVgoodCheck";
	private static final String METHOD_setApiKey = "setApiKey";
	private static final String METHOD_setDebug = "setDebug";
	private static final String METHOD_playerLogin = "playerLogin";
		
	public BeintootitaniumModuleBindingGen() {
		super();
		// Constants are pre-bound
		
		bindings.put(CREATE_, null);
		bindings.put(DYNPROP_Api, null);
		bindings.put(METHOD_GetVgood, null);
		bindings.put(METHOD_submitAchievementScore, null);
		bindings.put(METHOD_submitScore, null);
		bindings.put(METHOD_setAchievementScore, null);
		bindings.put(METHOD_getPlayerScore, null);
		bindings.put(METHOD_isLogged, null);
		bindings.put(METHOD_setUseSandbox, null);
		bindings.put(METHOD_beintooStart, null);
		bindings.put(METHOD_logout, null);
		bindings.put(METHOD_submitScoreWithVgoodCheck, null);
		bindings.put(METHOD_setApiKey, null);
		bindings.put(METHOD_setDebug, null);
		bindings.put(METHOD_playerLogin, null);
		
	}

	public void bindContextSpecific(KrollBridge bridge, KrollProxy proxy) {
	}

	@Override
	public Object getBinding(String name) {
		Object value = bindings.get(name);
		if (value != null) { 
			return value;
		}

		// Proxy create methods
		if (name.equals(CREATE_)) {
			KrollBindingUtils.KrollProxyCreator creator = new KrollBindingUtils.KrollProxyCreator() {
				public KrollProxy create(TiContext context) {
					return new ti.modules.beintoo.Proxy(context);
				}
			};
			KrollMethod create_method = KrollBindingUtils.createCreateMethod(CREATE_, creator);
			bindings.put(CREATE_, create_method);
			return create_method;
		}




		// Dynamic Properties
		if (name.equals(DYNPROP_Api)) {
			KrollDynamicProperty Api_property = new KrollDynamicProperty(DYNPROP_Api,
				true, false,
				false) {
				
				@Override
				public Object dynamicGet(KrollInvocation __invocation) {
	

	Object __Api_tmp;
	final org.appcelerator.kroll.KrollConverter __Api_converter = org.appcelerator.kroll.KrollConverter.getInstance();
	
	
	ti.modules.beintoo.Api __retVal =
	
	
	((BeintootitaniumModule) __invocation.getProxy()).Api(
);
	return __Api_converter.convertNative(__invocation, __retVal);
				}

				@Override
				public void dynamicSet(KrollInvocation __invocation, Object __value) {
					Log.w(TAG, "Property Beintootitanium.Api isn't writable");
				}
			};
			Api_property.setJavascriptConverter(org.appcelerator.kroll.KrollConverter.getInstance());
			Api_property.setNativeConverter(org.appcelerator.kroll.KrollConverter.getInstance());
			bindings.put(DYNPROP_Api, Api_property);
			return Api_property;
		}

		// Methods
		if (name.equals(METHOD_GetVgood)) {
			KrollMethod GetVgood_method = new KrollMethod(METHOD_GetVgood) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 2, METHOD_GetVgood);

	Object __GetVgood_tmp;
		KrollArgument __codeID_argument = new KrollArgument("codeID");
		java.lang.String codeID;
			__codeID_argument.setOptional(false);
			
				__GetVgood_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], java.lang.String.class);
				try {
					codeID = (java.lang.String) __GetVgood_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"codeID\" in \"GetVgood\", but got " + __GetVgood_tmp);
				}
		__codeID_argument.setValue(codeID);
		__invocation.addArgument(__codeID_argument);
		KrollArgument __isMultiple_argument = new KrollArgument("isMultiple");
		boolean isMultiple;
			__isMultiple_argument.setOptional(false);
			
				__GetVgood_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[1], Boolean.class);
				try {
					isMultiple = (Boolean) __GetVgood_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Boolean type for argument \"isMultiple\" in \"GetVgood\", but got " + __GetVgood_tmp);
				}
		__isMultiple_argument.setValue(isMultiple);
		__invocation.addArgument(__isMultiple_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).GetVgood(
		codeID,
				isMultiple
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_GetVgood, GetVgood_method);
			return GetVgood_method;
		}
		
		if (name.equals(METHOD_submitAchievementScore)) {
			KrollMethod submitAchievementScore_method = new KrollMethod(METHOD_submitAchievementScore) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 4, METHOD_submitAchievementScore);

	Object __submitAchievementScore_tmp;
		KrollArgument __achievement_argument = new KrollArgument("achievement");
		java.lang.String achievement;
			__achievement_argument.setOptional(false);
			
				__submitAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], java.lang.String.class);
				try {
					achievement = (java.lang.String) __submitAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"achievement\" in \"submitAchievementScore\", but got " + __submitAchievementScore_tmp);
				}
		__achievement_argument.setValue(achievement);
		__invocation.addArgument(__achievement_argument);
		KrollArgument __percentage_argument = new KrollArgument("percentage");
		java.lang.Float percentage;
			__percentage_argument.setOptional(false);
			
				__submitAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[1], java.lang.Float.class);
				try {
					percentage = (java.lang.Float) __submitAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.Float type for argument \"percentage\" in \"submitAchievementScore\", but got " + __submitAchievementScore_tmp);
				}
		__percentage_argument.setValue(percentage);
		__invocation.addArgument(__percentage_argument);
		KrollArgument __value_argument = new KrollArgument("value");
		java.lang.Float value;
			__value_argument.setOptional(false);
			
				__submitAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[2], java.lang.Float.class);
				try {
					value = (java.lang.Float) __submitAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.Float type for argument \"value\" in \"submitAchievementScore\", but got " + __submitAchievementScore_tmp);
				}
		__value_argument.setValue(value);
		__invocation.addArgument(__value_argument);
		KrollArgument __showNotification_argument = new KrollArgument("showNotification");
		boolean showNotification;
			__showNotification_argument.setOptional(false);
			
				__submitAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[3], Boolean.class);
				try {
					showNotification = (Boolean) __submitAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Boolean type for argument \"showNotification\" in \"submitAchievementScore\", but got " + __submitAchievementScore_tmp);
				}
		__showNotification_argument.setValue(showNotification);
		__invocation.addArgument(__showNotification_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).submitAchievementScore(
		achievement,
				percentage,
				value,
				showNotification
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_submitAchievementScore, submitAchievementScore_method);
			return submitAchievementScore_method;
		}
		
		if (name.equals(METHOD_submitScore)) {
			KrollMethod submitScore_method = new KrollMethod(METHOD_submitScore) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 3, METHOD_submitScore);

	Object __submitScore_tmp;
		KrollArgument __lastScore_argument = new KrollArgument("lastScore");
		int lastScore;
			__lastScore_argument.setOptional(false);
			
				__submitScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], Integer.class);
				try {
					lastScore = (Integer) __submitScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Integer type for argument \"lastScore\" in \"submitScore\", but got " + __submitScore_tmp);
				}
		__lastScore_argument.setValue(lastScore);
		__invocation.addArgument(__lastScore_argument);
		KrollArgument __codeID_argument = new KrollArgument("codeID");
		java.lang.String codeID;
			__codeID_argument.setOptional(false);
			
				__submitScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[1], java.lang.String.class);
				try {
					codeID = (java.lang.String) __submitScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"codeID\" in \"submitScore\", but got " + __submitScore_tmp);
				}
		__codeID_argument.setValue(codeID);
		__invocation.addArgument(__codeID_argument);
		KrollArgument __showNotification_argument = new KrollArgument("showNotification");
		boolean showNotification;
			__showNotification_argument.setOptional(false);
			
				__submitScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[2], Boolean.class);
				try {
					showNotification = (Boolean) __submitScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Boolean type for argument \"showNotification\" in \"submitScore\", but got " + __submitScore_tmp);
				}
		__showNotification_argument.setValue(showNotification);
		__invocation.addArgument(__showNotification_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).submitScore(
		lastScore,
				codeID,
				showNotification
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_submitScore, submitScore_method);
			return submitScore_method;
		}
		
		if (name.equals(METHOD_setAchievementScore)) {
			KrollMethod setAchievementScore_method = new KrollMethod(METHOD_setAchievementScore) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 4, METHOD_setAchievementScore);

	Object __setAchievementScore_tmp;
		KrollArgument __achievement_argument = new KrollArgument("achievement");
		java.lang.String achievement;
			__achievement_argument.setOptional(false);
			
				__setAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], java.lang.String.class);
				try {
					achievement = (java.lang.String) __setAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"achievement\" in \"setAchievementScore\", but got " + __setAchievementScore_tmp);
				}
		__achievement_argument.setValue(achievement);
		__invocation.addArgument(__achievement_argument);
		KrollArgument __percentage_argument = new KrollArgument("percentage");
		java.lang.Float percentage;
			__percentage_argument.setOptional(false);
			
				__setAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[1], java.lang.Float.class);
				try {
					percentage = (java.lang.Float) __setAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.Float type for argument \"percentage\" in \"setAchievementScore\", but got " + __setAchievementScore_tmp);
				}
		__percentage_argument.setValue(percentage);
		__invocation.addArgument(__percentage_argument);
		KrollArgument __value_argument = new KrollArgument("value");
		java.lang.Float value;
			__value_argument.setOptional(false);
			
				__setAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[2], java.lang.Float.class);
				try {
					value = (java.lang.Float) __setAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.Float type for argument \"value\" in \"setAchievementScore\", but got " + __setAchievementScore_tmp);
				}
		__value_argument.setValue(value);
		__invocation.addArgument(__value_argument);
		KrollArgument __showNotification_argument = new KrollArgument("showNotification");
		boolean showNotification;
			__showNotification_argument.setOptional(false);
			
				__setAchievementScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[3], Boolean.class);
				try {
					showNotification = (Boolean) __setAchievementScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Boolean type for argument \"showNotification\" in \"setAchievementScore\", but got " + __setAchievementScore_tmp);
				}
		__showNotification_argument.setValue(showNotification);
		__invocation.addArgument(__showNotification_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).setAchievementScore(
		achievement,
				percentage,
				value,
				showNotification
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_setAchievementScore, setAchievementScore_method);
			return setAchievementScore_method;
		}
		
		if (name.equals(METHOD_getPlayerScore)) {
			KrollMethod getPlayerScore_method = new KrollMethod(METHOD_getPlayerScore) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 1, METHOD_getPlayerScore);

	Object __getPlayerScore_tmp;
		KrollArgument __codeID_argument = new KrollArgument("codeID");
		java.lang.String codeID;
			__codeID_argument.setOptional(false);
			
				__getPlayerScore_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], java.lang.String.class);
				try {
					codeID = (java.lang.String) __getPlayerScore_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"codeID\" in \"getPlayerScore\", but got " + __getPlayerScore_tmp);
				}
		__codeID_argument.setValue(codeID);
		__invocation.addArgument(__codeID_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).getPlayerScore(
		codeID
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_getPlayerScore, getPlayerScore_method);
			return getPlayerScore_method;
		}
		
		if (name.equals(METHOD_isLogged)) {
			KrollMethod isLogged_method = new KrollMethod(METHOD_isLogged) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	

	Object __isLogged_tmp;
	final org.appcelerator.kroll.KrollConverter __isLogged_converter = org.appcelerator.kroll.KrollConverter.getInstance();
	
	
	boolean __retVal =
	
	
	((BeintootitaniumModule) __invocation.getProxy()).isLogged(
);
	return __isLogged_converter.convertNative(__invocation, __retVal);
				}
			};
			bindings.put(METHOD_isLogged, isLogged_method);
			return isLogged_method;
		}
		
		if (name.equals(METHOD_setUseSandbox)) {
			KrollMethod setUseSandbox_method = new KrollMethod(METHOD_setUseSandbox) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 1, METHOD_setUseSandbox);

	Object __setUseSandbox_tmp;
		KrollArgument __u_argument = new KrollArgument("u");
		boolean u;
			__u_argument.setOptional(false);
			
				__setUseSandbox_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], Boolean.class);
				try {
					u = (Boolean) __setUseSandbox_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Boolean type for argument \"u\" in \"setUseSandbox\", but got " + __setUseSandbox_tmp);
				}
		__u_argument.setValue(u);
		__invocation.addArgument(__u_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).setUseSandbox(
		u
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_setUseSandbox, setUseSandbox_method);
			return setUseSandbox_method;
		}
		
		if (name.equals(METHOD_beintooStart)) {
			KrollMethod beintooStart_method = new KrollMethod(METHOD_beintooStart) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 1, METHOD_beintooStart);

	Object __beintooStart_tmp;
		KrollArgument __goToDashboard_argument = new KrollArgument("goToDashboard");
		boolean goToDashboard;
			__goToDashboard_argument.setOptional(false);
			
				__beintooStart_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], Boolean.class);
				try {
					goToDashboard = (Boolean) __beintooStart_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Boolean type for argument \"goToDashboard\" in \"beintooStart\", but got " + __beintooStart_tmp);
				}
		__goToDashboard_argument.setValue(goToDashboard);
		__invocation.addArgument(__goToDashboard_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).beintooStart(
		goToDashboard
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_beintooStart, beintooStart_method);
			return beintooStart_method;
		}
		
		if (name.equals(METHOD_logout)) {
			KrollMethod logout_method = new KrollMethod(METHOD_logout) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	

	Object __logout_tmp;
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).logout(
);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_logout, logout_method);
			return logout_method;
		}
		
		if (name.equals(METHOD_submitScoreWithVgoodCheck)) {
			KrollMethod submitScoreWithVgoodCheck_method = new KrollMethod(METHOD_submitScoreWithVgoodCheck) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 3, METHOD_submitScoreWithVgoodCheck);

	Object __submitScoreWithVgoodCheck_tmp;
		KrollArgument __lastScore_argument = new KrollArgument("lastScore");
		int lastScore;
			__lastScore_argument.setOptional(false);
			
				__submitScoreWithVgoodCheck_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], Integer.class);
				try {
					lastScore = (Integer) __submitScoreWithVgoodCheck_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Integer type for argument \"lastScore\" in \"submitScoreWithVgoodCheck\", but got " + __submitScoreWithVgoodCheck_tmp);
				}
		__lastScore_argument.setValue(lastScore);
		__invocation.addArgument(__lastScore_argument);
		KrollArgument __threshold_argument = new KrollArgument("threshold");
		int threshold;
			__threshold_argument.setOptional(false);
			
				__submitScoreWithVgoodCheck_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[1], Integer.class);
				try {
					threshold = (Integer) __submitScoreWithVgoodCheck_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Integer type for argument \"threshold\" in \"submitScoreWithVgoodCheck\", but got " + __submitScoreWithVgoodCheck_tmp);
				}
		__threshold_argument.setValue(threshold);
		__invocation.addArgument(__threshold_argument);
		KrollArgument __codeID_argument = new KrollArgument("codeID");
		java.lang.String codeID;
			__codeID_argument.setOptional(false);
			
				__submitScoreWithVgoodCheck_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[2], java.lang.String.class);
				try {
					codeID = (java.lang.String) __submitScoreWithVgoodCheck_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"codeID\" in \"submitScoreWithVgoodCheck\", but got " + __submitScoreWithVgoodCheck_tmp);
				}
		__codeID_argument.setValue(codeID);
		__invocation.addArgument(__codeID_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).submitScoreWithVgoodCheck(
		lastScore,
				threshold,
				codeID
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_submitScoreWithVgoodCheck, submitScoreWithVgoodCheck_method);
			return submitScoreWithVgoodCheck_method;
		}
		
		if (name.equals(METHOD_setApiKey)) {
			KrollMethod setApiKey_method = new KrollMethod(METHOD_setApiKey) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 1, METHOD_setApiKey);

	Object __setApiKey_tmp;
		KrollArgument __apikey_argument = new KrollArgument("apikey");
		java.lang.String apikey;
			__apikey_argument.setOptional(false);
			
				__setApiKey_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], java.lang.String.class);
				try {
					apikey = (java.lang.String) __setApiKey_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"apikey\" in \"setApiKey\", but got " + __setApiKey_tmp);
				}
		__apikey_argument.setValue(apikey);
		__invocation.addArgument(__apikey_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).setApiKey(
		apikey
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_setApiKey, setApiKey_method);
			return setApiKey_method;
		}
		
		if (name.equals(METHOD_setDebug)) {
			KrollMethod setDebug_method = new KrollMethod(METHOD_setDebug) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 1, METHOD_setDebug);

	Object __setDebug_tmp;
		KrollArgument __u_argument = new KrollArgument("u");
		boolean u;
			__u_argument.setOptional(false);
			
				__setDebug_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], Boolean.class);
				try {
					u = (Boolean) __setDebug_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected Boolean type for argument \"u\" in \"setDebug\", but got " + __setDebug_tmp);
				}
		__u_argument.setValue(u);
		__invocation.addArgument(__u_argument);
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).setDebug(
		u
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_setDebug, setDebug_method);
			return setDebug_method;
		}
		
		if (name.equals(METHOD_playerLogin)) {
			KrollMethod playerLogin_method = new KrollMethod(METHOD_playerLogin) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	

	Object __playerLogin_tmp;
	
	
	
	
	((BeintootitaniumModule) __invocation.getProxy()).playerLogin(
);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_playerLogin, playerLogin_method);
			return playerLogin_method;
		}


		return super.getBinding(name);
	}

	private static final String SHORT_API_NAME = "Beintootitanium";
	private static final String FULL_API_NAME = "Beintootitanium";
	private static final String ID = "ti.beintoo";

	public String getAPIName() {
		return FULL_API_NAME;
	}

	public String getShortAPIName() {
		return SHORT_API_NAME;
	}

	public String getId() {
		return ID;
	}

	public KrollModule newInstance(TiContext context) {
		return new BeintootitaniumModule(context);
	}

	public Class<? extends KrollProxy> getProxyClass() {
		return BeintootitaniumModule.class;
	}

	public boolean isModule() {
		return true; 
	}
}