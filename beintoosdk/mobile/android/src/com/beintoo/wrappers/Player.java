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

import java.util.Map;

public class Player {
	 	String guid;
	    String publicName;
	    Language language;
	    User user;
	    Float rank;
	    Map<String, PlayerScore> playerScore;
	    Alliance alliance;
	    boolean isAllianceAdmin;
	    
	    public Player(){
	    	
	    }
	    
	    public String getGuid() {
			return guid;
		}
		public void setGuid(String guid) {
			this.guid = guid;
		}
		public String getPublicName() {
			return publicName;
		}
		public void setPublicName(String publicName) {
			this.publicName = publicName;
		}
		public Language getLanguage() {
			return language;
		}
		public void setLanguage(Language language) {
			this.language = language;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public Float getRank() {
			return rank;
		}
		public void setRank(Float rank) {
			this.rank = rank;
		}
		public Map<String, PlayerScore> getPlayerScore() {
			return playerScore;
		}
		public void setPlayerScore(Map<String, PlayerScore> playerScore) {
			this.playerScore = playerScore;
		}
	    
		public Alliance getAlliance() {
			return alliance;
		}

		public void setAlliance(Alliance alliance) {
			this.alliance = alliance;
		}

		public boolean isAllianceAdmin() {
			return isAllianceAdmin;
		}

		public void setAllianceAdmin(boolean isAllianceAdmin) {
			this.isAllianceAdmin = isAllianceAdmin;
		}
}	
