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


import com.beintoo.beintoosdkutility.ApiCallException;
import com.beintoo.beintoosdkutility.BeintooSdkParams;
import com.beintoo.beintoosdkutility.HeaderParams;
import com.beintoo.beintoosdkutility.PostParams;
import com.beintoo.wrappers.Contest;
import com.beintoo.wrappers.Message;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.PlayerScore;
import com.google.beintoogson.Gson;

public class BeintooPlayer {
	String apiPreUrl = null;
	
	public BeintooPlayer() {
		if(!BeintooSdkParams.useSandbox)
			apiPreUrl = BeintooSdkParams.apiUrl;
		else
			apiPreUrl = BeintooSdkParams.sandboxUrl;
	}

	/**
	 * Player login method. 
	 * 
	 * @param userExt User ext_id
	 * @param guid User guid
	 * @param codeID a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @param deviceUUID unique device ID
	 * @param language player language
	 * @param publicname player public name
	 * @return return a json object of the logged in player
	 */
	public Player playerLogin(String userExt, String guid, String codeID, String deviceUUID, String language, String publicname) {
		String apiUrl = apiPreUrl+"player/login?";
		
		if(language != null) apiUrl = apiUrl  + "language="+language;
		if((language != null) && (publicname != null)) apiUrl = apiUrl + "&";
		if(publicname != null) apiUrl = apiUrl + "publicname="+publicname;
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if(userExt != null){
			header.getKey().add("userExt");
			header.getValue().add(userExt);
		}
		if(codeID != null){
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		
		if(deviceUUID != null){
			header.getKey().add("deviceUUID");
			header.getValue().add(deviceUUID);
		}
		
		if(guid != null){
			header.getKey().add("guid");
			header.getValue().add(guid);
		}
		
		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, null);
		Gson gson = new Gson();
		Player pla = new Player();
		pla = gson.fromJson(json, Player.class);
		
		return pla;
	}
	
	/**
	 * Logout the player from Beintoo
	 * @param guid player guid
	 * @param codeID a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return json message
	 */
	public Message playerLogout (String guid, String codeID){
		String apiUrl = apiPreUrl+"player/logout";
		
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
		Gson gson = new Gson();
		Message msg = new Message();
		msg = gson.fromJson(json, Message.class);
		
		return msg;		
	} 
	
	public Message playerLogout (String guid){
		return playerLogout (guid, null);
	}
	
	/**
	 * Returns a json of the player from the guid
	 * @param guid player guid
	 * @param codeID a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return json object contains data of the requested player
	 */
	public Player getPlayer (String guid, String codeID){
		String apiUrl = apiPreUrl+"player/byguid/"+guid;
		
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
		Player pla = new Player();
		pla = gson.fromJson(json, Player.class);
		
		return pla;		
	}
	
	public Player getPlayer (String guid){
		return getPlayer(guid,null);
	}
	
	/**
	 * Returns a json of the player from the userExt of the associated user
	 * @param userExt user userExt
	 * @param codeID a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @return json object contains data of the requested player
	 */
	public Player getPlayerByUser (String userExt, String codeID){
		String apiUrl = apiPreUrl+"player/byuser/"+userExt;
		
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
		Player pla = new Player();
		pla = gson.fromJson(json, Player.class);
		
		return pla;		
	}
	
	/**
	 * Submit the player score
	 * 
	 * @param guid player guid
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @param deviceUUID (optional) 
	 * @param lastScore the score you want to submit
	 * @param balance (optional) if you have a lifetime total score for that player.
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param language
	 * @return a json message with the status
	 */
	public Message submitScore (String guid, String codeID, String deviceUUID, int lastScore, Integer balance,
			String latitude, String longitude, String radius, String language) {
		
			String apiUrl = apiPreUrl+"player/submitscore/?lastScore="+lastScore;
			
			if(balance != null) apiUrl = apiUrl + "&balance="+balance;
			if(language != null) apiUrl = apiUrl + "&language="+language;
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
			
			if(deviceUUID != null){ 
				header.getKey().add("deviceUUID");
				header.getValue().add(deviceUUID);
			}
			
			BeintooConnection conn = new BeintooConnection();
			
			String json = conn.httpRequest(apiUrl, header, null);
			Gson gson = new Gson();
			Message msg = new Message();
			msg = gson.fromJson(json, Message.class);
			return msg;
	}
	
	/**
	 * Submit the saved player scores
	 * 
	 * @param guid player guid
	 * @param codeID (optional) a string that represents the position in your code. We will use it to indentify different api calls of the same nature.
	 * @param deviceUUID (optional) 
	 * @param lastScore the score you want to submit
	 * @param balance (optional) if you have a lifetime total score for that player.
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param language
	 * @return a json message with the status
	 */
	public Message submitJsonScores (String guid, String codeID, String deviceUUID, String jsonScores,
			String latitude, String longitude, String radius, String language) {
		
			String apiUrl = apiPreUrl+"player/submitscore/";
			
			/*if(language != null) apiUrl = apiUrl + "&language="+language;
			if(latitude != null) apiUrl = apiUrl + "&latitude="+latitude;
			if(longitude != null) apiUrl = apiUrl + "&longitude="+longitude;
			if(radius != null) apiUrl = apiUrl + "&radius="+radius;
			*/
			HeaderParams header = new HeaderParams();
			header.getKey().add("apikey");
			header.getValue().add(DeveloperConfiguration.apiKey);
			header.getKey().add("guid");
			header.getValue().add(guid);
			if(codeID != null){
				header.getKey().add("codeID");
				header.getValue().add(codeID);
			}
			
			if(deviceUUID != null){ 
				header.getKey().add("deviceUUID");
				header.getValue().add(deviceUUID);
			}
			
			PostParams post = new PostParams();		
			post.getKey().add("json");
			post.getValue().add(jsonScores);
			
			BeintooConnection conn = new BeintooConnection();
			
			String json = conn.httpRequest(apiUrl, header, post,true);
			Gson gson = new Gson();
			Message msg = new Message();
			msg = gson.fromJson(json, Message.class);
			
			return msg;			
	}
	
	
	public PlayerScore getScore (String guid){		
		return this.getScoreForContest(guid, "default");		
	}
	
	public PlayerScore getScoreForContest(String guid, String contest){
		try {
			return this.getPlayer(guid).getPlayerScore().get(contest);
		}catch (ApiCallException ace){
			throw new ApiCallException();
		}catch (Exception e){
			PlayerScore p = new PlayerScore();
			p.setBalance(0.0);
			p.setBestscore(0.0);
			p.setLastscore(0.0);
			Contest c = new Contest();
			c.setCodeID(contest!=null?contest:"default");
			c.setDescription("");
			c.setName(contest!=null?contest:"default");
			c.setPublic(false);
			p.setContest(c);
			
			return p;
		}	
	}
	
	
}
