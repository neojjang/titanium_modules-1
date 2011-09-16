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

import java.util.List;

public class AchievementWrap {
	
	String id;
    String name;
    String description;
    String imageURL;
    App app;
    Double bedollars;
    Boolean isSecret;
    List<AchievementWrap> blockedBy;
    List<AchievementWrap> blocks;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public Double getBedollars() {
		return bedollars;
	}
	public void setBedollars(Double bedollars) {
		this.bedollars = bedollars;
	}
	public Boolean getIsSecret() {
		return isSecret;
	}
	public void setIsSecret(Boolean isSecret) {
		this.isSecret = isSecret;
	}
	public List<AchievementWrap> getBlockedBy() {
		return blockedBy;
	}
	public void setBlockedBy(List<AchievementWrap> blockedBy) {
		this.blockedBy = blockedBy;
	}
	public List<AchievementWrap> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<AchievementWrap> blocks) {
		this.blocks = blocks;
	}
}
