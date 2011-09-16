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

public class User {
	String id;
	Language language;
	String name;
	String nickname;
	String country;
	String locale;
	Integer gender;
	String userimg;
	String usersmallimg;
	Boolean isverified;
	String lastupdate;
	Integer level;
	Double bedollars;
	Double bescore;
	Integer messages;
    Integer unreadMessages;
    Integer pendingFriendRequest;
    
    public User() {
    	
    }
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public Boolean getIsverified() {
		return isverified;
	}

	public void setIsverified(Boolean isverified) {
		this.isverified = isverified;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Double getBedollars() {
		return bedollars;
	}

	public void setBedollars(Double bedollars) {
		this.bedollars = bedollars;
	}

	public Double getBescore() {
		return bescore;
	}

	public void setBescore(Double bescore) {
		this.bescore = bescore;
	}

	public String getUsersmallimg() {
		return usersmallimg;
	}

	public void setUsersmallimg(String usersmallimg) {
		this.usersmallimg = usersmallimg;
	}

	public Integer getMessages() {
		return messages;
	}

	public void setMessages(Integer messages) {
		this.messages = messages;
	}

	public Integer getUnreadMessages() {
		return unreadMessages;
	}

	public void setUnreadMessages(Integer unreadMessages) {
		this.unreadMessages = unreadMessages;
	}

	public Integer getPendingFriendRequest() {
		return pendingFriendRequest;
	}

	public void setPendingFriendRequest(Integer pendingFriendRequest) {
		this.pendingFriendRequest = pendingFriendRequest;
	}
}
