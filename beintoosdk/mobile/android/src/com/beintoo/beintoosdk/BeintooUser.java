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
import java.net.URLEncoder;
import java.util.List;

import com.beintoo.beintoosdkutility.ApiCallException;
import com.beintoo.beintoosdkutility.BeintooSdkParams;
import com.beintoo.beintoosdkutility.HeaderParams;
import com.beintoo.beintoosdkutility.PostParams;
import com.beintoo.wrappers.Challenge;
import com.beintoo.wrappers.Message;
import com.beintoo.wrappers.User;
import com.beintoo.wrappers.UserCredit;
import com.google.beintoogson.Gson;
import com.google.beintoogson.JsonParseException;
import com.google.beintoogson.reflect.TypeToken;

public class BeintooUser {
	String apiPreUrl = null;
	
	public BeintooUser() {
		if(!BeintooSdkParams.useSandbox)
			apiPreUrl = BeintooSdkParams.apiUrl;
		else
			apiPreUrl = BeintooSdkParams.sandboxUrl;
	}
	
	/**
	 * Returns an array of users from the same deviceUDID
	 * @param deviceUDID the requested device unique identifier
	 * @return a json array of users
	 */
	
	public User[] getUsersByDeviceUDID (String deviceUDID) {
		
		String apiUrl = apiPreUrl+"user/bydeviceUDID/"+deviceUDID;
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
	
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		Gson gson = new Gson();
		
		User[] users  = gson.fromJson(json, User[].class);
		
		return users;
	}
	
	/**
	 * Return a user by email and password
	 * @param email user email
	 * @param password user password
	 * @return json object of the requested user
	 */
	public User getUsersByEmail (String email, String password) {
		
		String apiUrl = apiPreUrl+"user/byemail";
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		header.getKey().add("email");
		header.getValue().add(email);
		header.getKey().add("password");
		header.getValue().add(password);

 		BeintooConnection conn = new BeintooConnection();
 		Gson gson = new Gson();
				
		User user = null;
		try {
			String json = conn.httpRequest(apiUrl, header, null);
			user = gson.fromJson(json, User.class);
		} catch (final JsonParseException e) {  
			return user;
		} catch (Exception e){
			return user;
		}
		 
		return user;
	}
	
	/**
	 * Return a user by his userExt
	 * @param userExt the user unique id identifier 
	 * @param (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return json object of the requested user
	 */
	public User getUserByUserExt (String userExt, String codeID) {
		
		String apiUrl = apiPreUrl+"user/"+userExt;
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
 		BeintooConnection conn = new BeintooConnection();
 		Gson gson = new Gson();
				
		User user = null;
		try {
			String json = conn.httpRequest(apiUrl, header, null);
			user = gson.fromJson(json, User.class);
		} catch (final JsonParseException e) {  
			return user;
		} catch (Exception e){
			return user;
		}
		 
		return user;
	}
	
	/**
	 * Returns challenges of the requested user 
	 * @param userExt usr_ext of the request user 
	 * @param status (path parameter) one of TO_BE_ACCEPTED, STARTED, ENDED. 
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return
	 */
	public Challenge[] challengeShow (String userExt, String status, String codeID){
		String apiUrl = apiPreUrl+"user/challenge/show/"+userExt+"/"+status;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		return new Gson().fromJson(json, Challenge[].class);		
	}
	
	public Challenge[] challengeShow (String userExt, String status){
		return challengeShow(userExt, status, null);
	}
	
	/**
	 * Manage challenges, used for accept, invite and refuse challenges
	 * @param userExtFrom (path parameter) the unique beintoo id of registered user
	 * @param userExtTo (path parameter) the unique beintoo id of registered user
	 * @param action action (path parameter) one of INVITE, ACCEPT, REFUSE
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a json object of request challenges
	 * @throws Exception 
	 */
	public Challenge challenge (String userExtFrom, String userExtTo, String action, String codeID){
		String apiUrl = apiPreUrl+"user/challenge/"+userExtFrom+"/"+action+"/"+userExtTo;
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Gson gson = new Gson();
		
		Challenge challenge = gson.fromJson(json, Challenge.class);
		
		/*if (challenge.getStatus() == null){
			Message msg = gson.fromJson(json, Message.class);
			if(msg.getMessageID() == -15)
				throw new Exception("-15");
		}*/
		
		return challenge;	
	}
	
	public Challenge challenge (String userExtFrom, String userExtTo, String action){
		return challenge (userExtFrom, userExtTo, action, null);
	}
	
	
	/**
	 * Get friends of a given user
	 *  
	 * @param userExt requested user
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature
	 * @return an array of Users
	 */
	public User[] getUserFriends(String userExt, String codeID){
		String apiUrl = apiPreUrl+"user/friend/"+userExt;
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Gson gson = new Gson();
		
		User [] user = gson.fromJson(json, User[].class);
		
		return user;		
	}
	
	public Message detachUserFromDevice (String deviceID,String userID){
		String apiUrl = apiPreUrl+"user/removeUDID/"+deviceID+"/"+userID;
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Gson gson = new Gson();
		
		Message msg = gson.fromJson(json, Message.class);
		
		return msg;		
	}
	
	public List<UserCredit> getUserBalance (String userExt, int start, int rows){
		String apiUrl = apiPreUrl+"user/balance/"+userExt+"/";
		if(start != -1 && rows != -1){
			String params = "?start="+start+"&rows="+rows;			
			apiUrl = apiUrl + params;			
		} 
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Gson gson = new Gson();
		
		Type type = new TypeToken<List<UserCredit>>() {}.getType();
		List<UserCredit> balance = gson.fromJson(json, type);
		
		return balance;				
	}

	/**
	 * Return pending friend requests 
	 * 
	 * @param userExt the unique beintoo id of registered user.
	 * @param codeID (optional)  a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a list of users
	 */
	public User[] getUserFriendshipRequests(String userExt, String codeID){
		String apiUrl = apiPreUrl+"user/friendshiprequest/"+userExt+"/";
		
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}		
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Gson gson = new Gson();
		User[] friendship = gson.fromJson(json, User[].class);
		
		return friendship;		
	}
	
	/**
	 * Respond to a friendship request 
	 * 
	 * @param userExtFrom the unique beintoo id of the current registered user.
	 * @param userExtTo the unique beintoo id of registered user to respond the request.
	 * @param action accept, refuse, invite
	 * @param codeID (optional)  a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a message
	 */
	public Message respondToFriendshipRequests(String userExtFrom, String userExtTo, String action, String codeID){
		String apiUrl = apiPreUrl+"user/friendshiprequest/"+userExtFrom+"/"+action+"/"+userExtTo;
		
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}		
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Gson gson = new Gson();
		Message msg = gson.fromJson(json, Message.class);
		
		return msg;		
	}
	
	/**
	 * Find a friend from nickname, name or email
	 * 
	 * @param query Data to search
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a list of users
	 */
	public User[] findFriendsByQuery(String query, String userExt, Boolean skipFriends, String codeID){
		@SuppressWarnings("deprecation")
		String apiUrl = apiPreUrl+"user/byquery/?query="+URLEncoder.encode(query) + ((userExt!=null) ? ("&userExt="+userExt+"&skipFriends="+skipFriends) : "");
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}		
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Gson gson = new Gson();
		User[] queryres = gson.fromJson(json, User[].class);
		
		return queryres;		
	}
	
	/**
	 * You can use this method to automatize the user-registration of one of your player, providing us some information.
	 * 
	 * @param guid the current player guid
	 * @param email mandatory, return error if a user with that email is already registered
	 * @param nickname mandatory
	 * @param password (optional), will be generated automatically if not provided
	 * @param name mandatory
	 * @param address (optional)
	 * @param country (optional)
	 * @param gender (optional), integer
	 * @param sendGreetingsEmail (optional), default true, whether or not to send a greeting email to the new user
	 * @param imageurl (optional), URL of userimage
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a user wrapper
	 */
	public User setOrUpdateUser(String action, String guid, String userExt, String email, String nickname, String password, String name, String address, String country, Integer gender, Boolean sendGreetingsEmail, String imageurl, String codeID){
		
		String apiUrl;
		if(action.equals("set"))
			apiUrl = apiPreUrl+"user/set";
		else if(action.equals("update"))
			apiUrl = apiPreUrl+"user/update";
		else
			throw new ApiCallException();
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);	
		
		if(guid != null){
			header.getKey().add("guid");
			header.getValue().add(guid);
		}
		
		if(userExt != null){
			header.getKey().add("userExt");
			header.getValue().add(userExt);
		}
		
		if(codeID != null) {
			header.getKey().add("guid");
			header.getValue().add(guid);
		}
		
		PostParams post = new PostParams();
		post.getKey().add("email");
		post.getValue().add(email);	
		post.getKey().add("nickname");
		post.getValue().add(nickname);
		if(password != null) {
			post.getKey().add("password");
			post.getValue().add(password);			
		}		
		if(name != null) {
			post.getKey().add("name");
			post.getValue().add(name);			
		}
		if(address != null) {
			post.getKey().add("address");
			post.getValue().add(address);			
		}
		if(country != null) {
			post.getKey().add("country");
			post.getValue().add(country);			
		}
		if(gender != null) {
			post.getKey().add("gender");
			post.getValue().add(gender.toString());			
		}
		if(sendGreetingsEmail != null) {
			post.getKey().add("sendGreetingsEmail");
			post.getValue().add(sendGreetingsEmail.toString());			
		}
		if(imageurl != null) {
			post.getKey().add("imageurl");
			post.getValue().add(imageurl);			
		}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, post,true);		 
		
		return new Gson().fromJson(json, User.class);		
	}
}
