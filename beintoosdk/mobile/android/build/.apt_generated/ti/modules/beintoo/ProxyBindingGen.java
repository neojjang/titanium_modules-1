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

import ti.modules.beintoo.Proxy;


/* WARNING: this code is generated for binding methods in Android */
public class ProxyBindingGen
	extends org.appcelerator.kroll.KrollProxyBindingGen
{
	private static final String TAG = "ProxyBindingGen";

	private static final String METHOD_printMessage = "printMessage";
		
	public ProxyBindingGen() {
		super();
		// Constants are pre-bound
		
		bindings.put(METHOD_printMessage, null);
		
	}

	public void bindContextSpecific(KrollBridge bridge, KrollProxy proxy) {
	}

	@Override
	public Object getBinding(String name) {
		Object value = bindings.get(name);
		if (value != null) { 
			return value;
		}






		// Methods
		if (name.equals(METHOD_printMessage)) {
			KrollMethod printMessage_method = new KrollMethod(METHOD_printMessage) {
				public Object invoke(KrollInvocation __invocation, Object[] __args) throws Exception
				{
	
	KrollBindingUtils.assertRequiredArgs(__args, 1, METHOD_printMessage);

	Object __printMessage_tmp;
		KrollArgument __message_argument = new KrollArgument("message");
		java.lang.String message;
			__message_argument.setOptional(false);
			
				__printMessage_tmp = org.appcelerator.kroll.KrollConverter.getInstance().convertJavascript(__invocation, __args[0], java.lang.String.class);
				try {
					message = (java.lang.String) __printMessage_tmp;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("Expected java.lang.String type for argument \"message\" in \"printMessage\", but got " + __printMessage_tmp);
				}
		__message_argument.setValue(message);
		__invocation.addArgument(__message_argument);
	
	
	
	
	((Proxy) __invocation.getProxy()).printMessage(
		message
		);
		return KrollProxy.UNDEFINED;
				}
			};
			bindings.put(METHOD_printMessage, printMessage_method);
			return printMessage_method;
		}


		return super.getBinding(name);
	}

	private static final String SHORT_API_NAME = "";
	private static final String FULL_API_NAME = "Beintootitanium.";
	private static final String ID = "ti.modules.beintoo.Proxy";

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
		return null;
	}

	public Class<? extends KrollProxy> getProxyClass() {
		return Proxy.class;
	}

	public boolean isModule() {
		return false; 
	}
}