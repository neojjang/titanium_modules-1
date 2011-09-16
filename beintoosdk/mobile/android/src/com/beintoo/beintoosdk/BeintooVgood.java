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

import com.beintoo.beintoosdkutility.BeintooSdkParams;
import com.beintoo.beintoosdkutility.HeaderParams;

import com.beintoo.main.Beintoo;
import com.beintoo.wrappers.Message;
import com.beintoo.wrappers.Vgood;
import com.beintoo.wrappers.VgoodChooseOne;
import com.google.beintoogson.Gson;

public class BeintooVgood {
	String apiPreUrl = null;
	
	public BeintooVgood() {
		if(!BeintooSdkParams.useSandbox)
			apiPreUrl = BeintooSdkParams.apiUrl;
		else
			apiPreUrl = BeintooSdkParams.sandboxUrl;
	}

	/**
	 * Retrieve a vgood for the requested user
	 * 
	 * @param guid user guid
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param privategood a bool to set if the vgood has to be private
	 * @return a json object with vgood data
	 */
	public Vgood getVgood(String guid, String codeID,String latitude, String longitude, String radius, boolean privategood) {
		
		String apiUrl = apiPreUrl+"vgood/get/byguid/"+guid+"/?allowBanner=true";
		
		if(privategood == true) apiUrl = apiUrl + "&private=true"; else apiUrl = apiUrl + "&private=false";  
		if(latitude != null) apiUrl = apiUrl + "&latitude="+latitude;
		if(longitude != null) apiUrl = apiUrl + "&longitude="+longitude;
		if(radius != null) apiUrl = apiUrl + "&radius="+radius;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		header.getKey().add("guid");
		header.getValue().add(guid);
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		
		try {
		// ADD THE USER AGENT
			String userAgent = Beintoo.userAgent;
			if(userAgent != null){
				header.getKey().add("User-Agent");
				header.getValue().add(userAgent);
			}
		}catch (Exception e){e.printStackTrace();}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		Gson gson = new Gson();
		Vgood vgood = new Vgood();
		vgood = gson.fromJson(json, Vgood.class);
		
		return vgood;
	}
	
	/**
	 * Retrieve a vgood for the requested user
	 * 
	 * @param guid user guid
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param privategood a bool to set if the vgood has to be private
	 * @return a json object with vgood data
	 */
	public VgoodChooseOne getVgoodList(String guid, String codeID,String latitude, String longitude, String radius, boolean privategood) {
		
		String apiUrl = apiPreUrl+"vgood/byguid/"+guid+"/?allowBanner=true";
		
		if(privategood == true) apiUrl = apiUrl + "&private=true"; else apiUrl = apiUrl + "&private=false";  
		if(latitude != null) apiUrl = apiUrl + "&latitude="+latitude;
		if(longitude != null) apiUrl = apiUrl + "&longitude="+longitude;
		if(radius != null) apiUrl = apiUrl + "&radius="+radius;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		header.getKey().add("guid");
		header.getValue().add(guid);
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		
		try {
		// ADD THE USER AGENT
			String userAgent = Beintoo.userAgent;
			if(userAgent != null){
				header.getKey().add("User-Agent");
				header.getValue().add(userAgent);
			}
		}catch (Exception e){e.printStackTrace();}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		VgoodChooseOne vgoods = new Gson().fromJson(json, VgoodChooseOne.class);
		
		return vgoods;
	}

	
	/**
	 * Returns an array with all the vgoods earned by the user
	 * 
	 * @param userExt usr_ext unique identifier
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a Vgood object array
	 */
	public Vgood [] showByUser(String userExt, String codeID, boolean onlyConverted){
		String apiUrl = null;
		if(!onlyConverted)
			apiUrl = apiPreUrl+"vgood/show/byuser/"+userExt;
		else
			apiUrl = apiPreUrl+"vgood/show/byuser/"+userExt+"/?onlyConverted=true";
		
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
		
		Vgood[] vgood = gson.fromJson(json, Vgood[].class);
		
		return vgood;		
	}
	
	/**
	 * Determinate if a player is eligible to receive a new virtual good
	 * 
	 * @param guid user guid
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param privategood a bool to set if the vgood has to be private
	 * @return
	 */
	public Message isEligibleForVgood(String guid, String codeID,String latitude, String longitude, String radius) {
		
		String apiUrl = apiPreUrl+"vgood/iseligible/byguid/"+guid+"/?";
		
		if(latitude != null) apiUrl = apiUrl + "latitude="+latitude;
		if(longitude != null) apiUrl = apiUrl + "&longitude="+longitude;
		if(radius != null) apiUrl = apiUrl + "&radius="+radius;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		header.getKey().add("guid");
		header.getValue().add(guid);
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		
		Message resp = new Gson().fromJson(json, Message.class);
		
		return resp;
	}
	
	/**
	 * Send a Vgood as a gift from a user to another user
	 * 
	 * @param vgoodExt the vgood id 
	 * @param userExt the user id of the gift sender
	 * @param userExt_to the user id of the gift sender
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a Message object with the status of the sendAsAgift operation
	 */
	public Message sendAsAGift (String vgoodExt, String userExt, String userExt_to, String codeID){
			
		String apiUrl = apiPreUrl+"vgood/sendasgift/"+vgoodExt+"/"+userExt+"/"+userExt_to;
	
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
	 * Accept a vgood from a user
	 * 
	 * @param vgoodExt the vgood id 
	 * @param userExt the user id of the gift sender
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return a Message object with the status of the sendAsAgift operation
	 */
	public Vgood acceptVgood (String vgoodExt, String userExt, String codeID){
			
		String apiUrl = apiPreUrl+"vgood/accept/"+vgoodExt+"/"+userExt;
	
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
		
		Vgood v = gson.fromJson(json, Vgood.class);
		
		return v;	
	}
	
	/**
	 * Post a user vgood on the marketplace
	 * 
	 * @param vgoodExt the user id of the vgood
	 * @param userExt the user id of the user
	 * @return a message with the operation result
	 */
	public Message sellVgood (String vgoodExt, String userExt){
		String apiUrl = apiPreUrl+"vgood/marketplace/sell/"+vgoodExt+"/"+userExt;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null,true);
		Gson gson = new Gson();
		
		Message msg = gson.fromJson(json, Message.class);
		
		return msg;	
	}
	
	/**
	 * Return a list of vgoods on sale in the marketplace
	 * @param latitude the user latitude
	 * @param longitude the user longitude
	 * @param rows how many vgood you want to retrieve
	 * @param featured 
	 * @return a list of vgoods
	 */
	public Vgood[] showMarketPlace (String latitude, String longitude, int rows, boolean featured){
		String apiUrl = "";
		if(featured)
			apiUrl = apiPreUrl+"vgood/marketplace/show/?featured=true";
		else
			apiUrl = apiPreUrl+"vgood/marketplace/show";
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		if(latitude != null && longitude != null){
			header.getKey().add("latitude");
			header.getValue().add(latitude);
			header.getKey().add("longitude");
			header.getValue().add(longitude);	
		}
		
		if(rows > 0){
			header.getKey().add("rows");
			header.getValue().add(Integer.toString(rows));
		}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		Gson gson = new Gson();
		
		Vgood[] goods = gson.fromJson(json, Vgood[].class);
		
		return goods;	
	}
	
	public Vgood buyVgood(String vgoodExt, String userExt, boolean featured){
		String apiUrl = "";
		if(featured)
			apiUrl = apiPreUrl+"vgood/marketplace/buy/"+vgoodExt+"/"+userExt;
		else
			apiUrl = apiPreUrl+"vgood/marketplace/featured/buy/"+vgoodExt+"/"+userExt;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null,true);
		Gson gson = new Gson();
		
		Vgood vgood = gson.fromJson(json, Vgood.class);
		
		return vgood;
	}
}
