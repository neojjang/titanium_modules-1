package com.beintoo.beintoosdk;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import android.net.Uri;

import com.beintoo.beintoosdkutility.BeintooSdkParams;
import com.beintoo.beintoosdkutility.HeaderParams;
import com.beintoo.beintoosdkutility.PostParams;
import com.beintoo.wrappers.Alliance;
import com.beintoo.wrappers.LeaderboardContainer;
import com.beintoo.wrappers.Message;
import com.beintoo.wrappers.User;
import com.google.beintoogson.Gson;
import com.google.beintoogson.reflect.TypeToken;

public class BeintooAlliances {
	String apiPreUrl = null;

	public BeintooAlliances() {
		if (!BeintooSdkParams.useSandbox)
			apiPreUrl = BeintooSdkParams.apiUrl;
		else
			apiPreUrl = BeintooSdkParams.sandboxUrl;
	}

	public List<Alliance> listByApp(String query, Integer start, Integer rows, String codeID){
		Uri.Builder apiUrl = Uri.parse(apiPreUrl+"alliance/").buildUpon();
		
		if(query != null){
			apiUrl.appendQueryParameter("query", query);
		}
		
		if(rows != null && rows != null){
			apiUrl.appendQueryParameter("start", start.toString());
			apiUrl.appendQueryParameter("rows", rows.toString());
		}
		
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}

		BeintooConnection conn = new BeintooConnection();

		String json = conn.httpRequest(apiUrl.toString(), header, null);
		Gson gson = new Gson();
		Type mapType = new TypeToken<List<Alliance>>() {}.getType();
		List<Alliance> alliances = gson.fromJson(json, mapType);
		
		return alliances;
	}
	
	public Alliance createAlliance(String userExt, String name, String codeID) {
		String apiUrl = apiPreUrl + "alliance";

		// Set the auth request header
		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);

		PostParams post = new PostParams();
		post.getKey().add("userExt");
		post.getValue().add(userExt);
		post.getKey().add("name");
		post.getValue().add(name);

		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}

		BeintooConnection conn = new BeintooConnection();
		String json = conn.httpRequest(apiUrl, header, post, true);
		Gson gson = new Gson();
		Alliance alliance = gson.fromJson(json, Alliance.class);

		return alliance;
	}

	public Alliance getAllianceInfo(String extID, Integer start, Integer rows,
			String codeID) {
		String apiUrl = apiPreUrl + "alliance/" + extID + "/?start=" + start
				+ "&rows=" + rows;

		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}

		BeintooConnection conn = new BeintooConnection();

		String json = conn.httpRequest(apiUrl, header, null);
		Gson gson = new Gson();
		Alliance alliance = gson.fromJson(json, Alliance.class);

		return alliance;
	}

	public Message joinAlliance(String extID, String userExt, String codeID) {
		String apiUrl = apiPreUrl + "alliance/" + extID + "/addme/" + userExt;

		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		
		BeintooConnection conn = new BeintooConnection();

		String json = conn.httpRequest(apiUrl, header, null, true);
		Gson gson = new Gson();
		Message msg = gson.fromJson(json, Message.class);

		return msg;
	}
	
	public Message quitAlliance(String extID, String userExt, String codeID) {
		String apiUrl = apiPreUrl + "alliance/" + extID + "/removeme/" + userExt;

		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}

		BeintooConnection conn = new BeintooConnection();

		String json = conn.httpRequest(apiUrl, header, null, true);
		Gson gson = new Gson();
		Message msg = gson.fromJson(json, Message.class);

		return msg;
	}

	public List<User> showPendingUsersRequests(String extID, String userExt, String status,
			Integer start, Integer rows, String codeID) {
		String apiUrl = apiPreUrl + "alliance/" + extID + "/"+userExt+"/" + status+"/?start="+start+"&rows="+rows;

		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}
		
		BeintooConnection conn = new BeintooConnection();		
		String json = conn.httpRequest(apiUrl, header, null);
		
		Type mapType = new TypeToken<List<User>>() {}.getType();
		Gson gson = new Gson();
		List<User> users = gson.fromJson(json, mapType);
		
		return users;
	}
	
	public Message acceptPendingUser(String extID, String userExt, String userExt2, String codeID) {
		String apiUrl = apiPreUrl + "alliance/" + extID + "/" + userExt + "/ACCEPT/" + userExt2;

		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}

		BeintooConnection conn = new BeintooConnection();

		String json = conn.httpRequest(apiUrl, header, null);
		Gson gson = new Gson();
		Message msg = gson.fromJson(json, Message.class);

		return msg;
	}
	
	public Message removeUser(String extID, String userExt, String userExt2, String codeID) {
		String apiUrl = apiPreUrl + "alliance/" + extID + "/" + userExt + "/REMOVE/" + userExt2;

		HeaderParams header = new HeaderParams();
		header.getKey().add("apikey");
		header.getValue().add(DeveloperConfiguration.apiKey);
		if (codeID != null) {
			header.getKey().add("codeID");
			header.getValue().add(codeID);
		}

		BeintooConnection conn = new BeintooConnection();

		String json = conn.httpRequest(apiUrl, header, null);
		Gson gson = new Gson();
		Message msg = gson.fromJson(json, Message.class);

		return msg;
	}
	
	public Map<String, LeaderboardContainer> getLeaderboard(String allianceExtId, String latitude, String longitude, Integer start, Integer rows, String codeID) {
			Uri.Builder apiUrl = Uri.parse(apiPreUrl+"alliance/topscore/").buildUpon();
			
			if(allianceExtId != null)
				apiUrl.appendQueryParameter("id", allianceExtId);
			
			if(latitude != null && longitude != null){
				apiUrl.appendQueryParameter("latitude", latitude);
				apiUrl.appendQueryParameter("longitude", longitude);
			}
						
			if(rows != null && start != null){
				apiUrl.appendQueryParameter("start", start.toString());
				apiUrl.appendQueryParameter("rows", rows.toString());
			}
			
			HeaderParams header = new HeaderParams();
			header.getKey().add("apikey");
			header.getValue().add(DeveloperConfiguration.apiKey);
			if (codeID != null) {
				header.getKey().add("codeID");
				header.getValue().add(codeID);
			}

			BeintooConnection conn = new BeintooConnection();
			String json = conn.httpRequest(apiUrl.build().toString(), header, null);
			Gson gson = new Gson();
	        Type mapType = new TypeToken<Map<String, LeaderboardContainer>>() {
	        }.getType();
	        Map<String, LeaderboardContainer> leaders = gson.fromJson(json,mapType);
	        
	        return leaders;
	}

}
