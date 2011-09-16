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

import java.util.Collection;

public class Vgood {
	private String getRealURL;
    private String showURL;
    private String description;
    private String descriptionSmall;
    private String id; //extId actually
    private String imageUrl;
    private String imageSmallUrl;
    private String startdate;
    private String enddate;
    private String name;
    private Collection<VgoodPoi> vgoodPOIs;
    private Collection<User> whoAlsoConverted;
    private boolean isBanner;
    private String contentType;
    private String content;
    private String rewardText;
    
	public String getGetRealURL() {
		return getRealURL;
	}
	public void setGetRealURL(String getRealURL) {
		this.getRealURL = getRealURL;
	}
	public String getShowURL() {
		return showURL;
	}
	public void setShowURL(String showURL) {
		this.showURL = showURL;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionSmall() {
		return descriptionSmall;
	}
	public void setDescriptionSmall(String descriptionSmall) {
		this.descriptionSmall = descriptionSmall;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageSmallUrl() {
		return imageSmallUrl;
	}
	public void setImageSmallUrl(String imageSmallUrl) {
		this.imageSmallUrl = imageSmallUrl;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<VgoodPoi> getVgoodPOIs() {
		return vgoodPOIs;
	}
	public void setVgoodPOIs(Collection<VgoodPoi> vgoodPOIs) {
		this.vgoodPOIs = vgoodPOIs;
	}
	public Collection<User> getWhoAlsoConverted() {
		return whoAlsoConverted;
	}
	public void setWhoAlsoConverted(Collection<User> whoAlsoConverted) {
		this.whoAlsoConverted = whoAlsoConverted;
	}
	public boolean isBanner() {
		return isBanner;
	}
	public void setBanner(boolean isBanner) {
		this.isBanner = isBanner;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setRewardText(String rewardText) {
		this.rewardText = rewardText;
	}
	public String getRewardText() {
		return rewardText;
	}
}
