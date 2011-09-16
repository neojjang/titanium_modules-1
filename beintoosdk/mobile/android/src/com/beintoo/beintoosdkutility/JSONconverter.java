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
package com.beintoo.beintoosdkutility;

import com.beintoo.wrappers.Player;
import com.google.beintoogson.Gson;

public class JSONconverter {
	public static String playerToJson(Player p){
		Gson gson = new Gson();		
		return gson.toJson(p);
	}
	
	public static Player playerJsonToObject(String p){
		Gson gson = new Gson();		
		return gson.fromJson(p, Player.class);
	}
}
