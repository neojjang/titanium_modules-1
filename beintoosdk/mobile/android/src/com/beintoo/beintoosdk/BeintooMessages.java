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
package com.beintoo.beintoosdk;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.beintoo.beintoosdkutility.BeintooSdkParams;
import com.beintoo.beintoosdkutility.HeaderParams;
import com.beintoo.beintoosdkutility.PostParams;
import com.beintoo.wrappers.Message;
import com.beintoo.wrappers.UsersMessage;
import com.google.beintoogson.Gson;
import com.google.beintoogson.reflect.TypeToken;

public class BeintooMessages {
	final public static String READ = "READ";
	final public static String UNREAD = "UNREAD";
	
	String apiPreUrl = null;
	
	public BeintooMessages() {
		if(!BeintooSdkParams.useSandbox)
			apiPreUrl = BeintooSdkParams.apiUrl;
		else
			apiPreUrl = BeintooSdkParams.sandboxUrl;
	}
	
	public Message sendMessage(String from, String to, String text){
		String apiUrl = apiPreUrl+"message";
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		PostParams post = new PostParams();
		
		if(text != null && from != null && to != null){
			post.getKey().add("from");
			post.getValue().add(from);
			post.getKey().add("to");
			post.getValue().add(to);
			post.getKey().add("text");
			post.getValue().add(text);
		}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, post,true);		 
		
		return new Gson().fromJson(json, Message.class);		
	}
	
	public List<UsersMessage> getUserMessages(String to, String status, int start, int rows){
		String apiUrl = apiPreUrl+"message/?start="+start+"&rows="+rows;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);		
		header.getKey().add("to");
		header.getValue().add(to);
		if(status != null){
			header.getKey().add("status");
			header.getValue().add(status);
		}
				 		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);

		List<UsersMessage> messages = new ArrayList<UsersMessage>();
		Type type = new TypeToken<List<UsersMessage>>() {}.getType();        
		messages = new Gson().fromJson(json, type);
		
		return messages;
	}
	
	public UsersMessage markAsRead (String messageID, String userExt){
		String apiUrl = apiPreUrl+"message/"+messageID;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		header.getKey().add("userExt");
		header.getValue().add(userExt);
		
		PostParams post = new PostParams();
		post.getKey().add("status");
		post.getValue().add("READ");		
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, post,true);		 
		
		return new Gson().fromJson(json, UsersMessage.class);	
	}
	
	public UsersMessage markAsArchived (String messageID, String userExt){
		String apiUrl = apiPreUrl+"message/"+messageID;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		header.getKey().add("userExt");
		header.getValue().add(userExt);
		PostParams post = new PostParams();
		post.getKey().add("status");
		post.getValue().add("ARCHIVED");		
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, post,true);		 
		
		return new Gson().fromJson(json, UsersMessage.class);	
	}
	
}
