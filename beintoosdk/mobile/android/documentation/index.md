# Beintoo Titanium Module

## Description

BeintooSDK for Titanium

## Accessing the BeintooTitanium Module

To access this module from JavaScript, you would do the following:

	var BeintooTitanium = require('ti.beintoo');

The BeintooTitanium variable is a reference to the Module object.	

## Permissions
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />    
	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

## Reference

* We distinguish two types of methods:

 * Core methods that are the main ones used in most cases. This methods have a logic in them and do the most important functions on the SDK.
 * API methods which are used to making calls to our API's. This methods doesn’t have a logic inside, only API calls and returns the response in an wrapper object.

http://documentation.beintoo.com/home/android-sdk/methods

## Usage

	var BeintooTitanium = require('ti.beintoo');

	BeintooTitanium.setApiKey("YOUR-BEINTOO-APIKEY");
	
	BeintooTitanium.addEventListener('onLogin',function(e){		
		var toast = Titanium.UI.createNotification({
	    	duration: 2000,
		    message: "player login callback"+e.player.guid
		});
		toast.show();
	});

	// LOGIN THE PLAYER ON APP START
	BeintooTitanium.playerLogin();

See the app.js example for further examples

## Callbacks

|Method|Callback name|Description
|:---------|:----------|:----------|    
|playerLogin|onLogin|Called when the user is logged in
|playerLogin|onError|Called when something went wrong
|submitScore|onSubmitComplete|Called when the submit score is completed
|submitScore|onError|Called when something went wrong
|submitScoreWithVgoodCheck|onSubmitComplete|Called when the submit score is completed
|submitScoreWithVgoodCheck|onVgood|Called when the vgood is assigned
|submitScoreWithVgoodCheck|onVgoodOverquota|Called when the user received too many rewards
|submitScoreWithVgoodCheck|onVgoodNothingToDispatch|Called when the aren't available rewards in the user area
|submitScoreWithVgoodCheck|onError|Called when something went wrong
|getPlayerScore|onPlayerScore|Called when the player score is ready
|getPlayerScore|onError|Called when something went wrong
|submitAchievementScore|onPlayerAchievement|Called when the submit is completed
|submitAchievementScore|onPlayerAchievementUnlocked|Called when the achievement is unlocked
|submitAchievementScore|onError|Called when something went wrong
|setAchievementScore|onPlayerAchievement|Called when the submit is completed
|setAchievementScore|onPlayerAchievementUnlocked|Called when the achievement is unlocked
|setAchievementScore|onError|Called when something went wrong
|GetVgood|onVgood|Called when the vgood is assigned
|GetVgood|onVgoodOverquota|Called when the user received too many rewards
|GetVgood|onVgoodNothingToDispatch|Called when the aren't available rewards in the user area
|GetVgood|onError|Called when something went wrong

## License

This open source Java library allows you to integrate Beintoo into your Android application. 
Except as otherwise noted, the Beintoo Android SDK is licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)