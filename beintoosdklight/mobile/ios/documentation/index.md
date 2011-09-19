# Beintoo Titanium Module

## Description

BeintooSDK for Titanium

## Accessing the BeintooTitanium Module

To access this module from JavaScript, you would do the following:

	var BeintooTitanium = require('ti.beintoo');
	
or
	Ti.Beintoo = require('ti.beintoo');

## Reference

http://documentation.beintoo.com/home/ios-sdk/api-description

## Usage

	var BeintooTitanium = require('ti.beintoo');
	BeintooTitanium.initBeintoo("YOUR_APIKEY_HERE");

PlayerProxy definition

	var playerProxy = BeintooTitanium.createPlayer();

UserProxy definition

	var userProxy = BeintooTitanium.createUser();

VgoodProxy definition
	
	var vgoodProxy = BeintooTitanium.createVgood();

See the app.js example for further examples

## Callbacks

|Proxy name|Callback name|Description
|:---------|:----------|:----------|    
|playerProxy|onPlayerLogin|Called when the user is logged in
|playerProxy|onPlayerLoginError|Called when something went wrong
|playerProxy|onPlayerSubmitScore|Called when the submit score is completed
|playerProxy|onPlayerSubmitScoreError|Called when something went wrong
|playerProxy|onPlayerGetscore|Called when the player score is retrieved
|playerProxy|onPlayerGetscoreError|Called when something went wrong
|playerProxy|onPlayerSetBalance|Called when the player score balance is set
|playerProxy|onPlayerSetBalanceError|Called when something went wrong
|userProxy|onUserGetuser|Called when the logged user data is retrieved
|userProxy|onUserGetuserByMandP|Called when you want to login a user given mail and password
|userProxy|onUserRegistration|Called when an user registration is complete
|vgoodProxy|onVgoodGenerated|Called when the vgood is assigned
|vgoodProxy|onVgoodGeneratedError|Called when something went wrong

## Author

Ferdinando Messina <fmessina@beintoo.com>, Beintoo

## License

This open source Java library allows you to integrate Beintoo into your Android application. 
Except as otherwise noted, the Beintoo Android SDK is licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)

