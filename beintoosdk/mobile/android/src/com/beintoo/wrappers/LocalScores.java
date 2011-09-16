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
package com.beintoo.wrappers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.google.beintoogson.Gson;
import com.google.beintoogson.reflect.TypeToken;

public class LocalScores {
	String codeID;
	Integer lastScore;
	
	public LocalScores(){}
	
	public String getCodeID() {
		return codeID;
	}
	public void setCodeID(String codeID) {
		this.codeID = codeID;
	}
	public Integer getScore() {
		return lastScore;
	}
	public void setScore(Integer score) {
		this.lastScore = score;
	}
	
	public static List<LocalScores> getLocallySavedScores (Context ctx){
		String jsonScores = PreferencesHandler.getString("localScores", ctx);
		
		List<LocalScores> scores;		
		Type mapType = new TypeToken<List<LocalScores>>() {}.getType();		
		if(jsonScores != null)
			 scores = new Gson().fromJson(jsonScores, mapType);
		else
			scores = new ArrayList<LocalScores>();
		
		return scores;
	}
	
	public static void saveLocalScore (Context ctx, int score, String codeID){
		String jsonScores = PreferencesHandler.getString("localScores", ctx);
		List<LocalScores> scores;		
		Type mapType = new TypeToken<List<LocalScores>>() {}.getType();		
		if(jsonScores != null)
			 scores = new Gson().fromJson(jsonScores, mapType);
		else
			scores = new ArrayList<LocalScores>();
		
		if(codeID == null) codeID = "default";

		LocalScores currentScore = new LocalScores();
		currentScore.setCodeID(codeID);
		currentScore.setScore(score);		
		scores.add(currentScore);
		PreferencesHandler.saveString("localScores", new Gson().toJson(scores), ctx);	
	}
}
