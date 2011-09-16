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
import java.util.Map;

public class App {
	String name;
	String developerName;
    String imageSmallUrl;
    String imageUrl;
    List<Contest> contests;
    Map<String, String> download_url;

    public App(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeveloperName() {
		return developerName;
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}

	public String getImageSmallUrl() {
		return imageSmallUrl;
	}

	public void setImageSmallUrl(String imageSmallUrl) {
		this.imageSmallUrl = imageSmallUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Contest> getContests() {
		return contests;
	}

	public void setContests(List<Contest> contests) {
		this.contests = contests;
	}

	public Map<String, String> getDownload_url() {
		return download_url;
	}

	public void setDownload_url(Map<String, String> download_url) {
		this.download_url = download_url;
	}

	
	
}
