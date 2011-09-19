// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var window = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label1 = Titanium.UI.createLabel({
	color:'#999',
	text:'Beintoo API test',
	font:{fontSize:20,fontFamily:'Helvetica Neue'},
	textAlign:'center',
	top:300,
	width:'auto'
});

var button1 = Titanium.UI.createButton({
	title:'PlayerLogin',
	top:10,
	left:10,
	width:110,
    height:40
});

var button2 = Titanium.UI.createButton({
	title:'PlayerLoginWithUser',
	top:10,
	left:130,
	width:170,
    height:40
});

var button3 = Titanium.UI.createButton({
	title:'SubmitScore',
	top:55,
	left:10,
	width:110,
    height:40
})

var button4 = Titanium.UI.createButton({
	title:'GetScore',
	top:55,
	left:125,
	width:90,
    height:40
})

var button5 = Titanium.UI.createButton({
	title:'SetBalance',
	top:55,
	left:220,
	width:90,
    height:40
})

var button6 = Titanium.UI.createButton({
	title:'GetLoggedUser',
	top:110,
	left:10,
	width:140,
    height:40
})

var button7 = Titanium.UI.createButton({
	title:'GetUserByMail&Pass',
	top:110,
	left:155,
	width:160,
    height:40
})

var button8 = Titanium.UI.createButton({
	title:'RegisterUser',
	top:155,
	left:10,
	width:100,
    height:40
})

var button9 = Titanium.UI.createButton({
	title:'getVgood',
	top:210,
	left:10,
	width:100,
    height:40
}) 

var button10 = Titanium.UI.createButton({
	title:'playerLogout',
	top:210,
	left:190,
	width:120,
    height:40
})

window.add(label1);
window.add(button1);
window.add(button2);
window.add(button3);
window.add(button4);
window.add(button5);
window.add(button6);
window.add(button7);
window.add(button8);
window.add(button9);
window.add(button10);

window.open();

var beintoo = require('ti.beintoo');
beintoo.initBeintoo("YOUR_APIKEY_HERE");

// PlayerProxy definition
var playerProxy = beintoo.createPlayer();

playerProxy.addEventListener('onPlayerLogin',function(e){
	Ti.API.info("Beintoo player login result is " + e.player.guid);
});

playerProxy.addEventListener('onPlayerSubmitScore',function(e){
	Ti.API.info("Beintoo submit score result is " + e.result);
});

playerProxy.addEventListener('onPlayerSetBalance',function(e){
		Ti.API.info("Beintoo set balance result is " + e.result);
})

playerProxy.addEventListener('onPlayerGetscore',function(e){
	for (var key in e.result) {
		// Access all the contests value of the score here
		// every key is a contest name.
		for (var key2 in e.result[key]) {
			Ti.API.info("Beintoo value for key " + key2 + " is " + e.result[key][key2] ); // "default" is the name of your contest
		}
	}
});

// UserProxy definition

var userProxy = beintoo.createUser();

userProxy.addEventListener('onUserGetuser',function(e){
	for (var key in e.result) {
			Ti.API.info("Beintoo user value for key " + key + " is " + e.result[key]); 
		}
});

userProxy.addEventListener('onUserGetuserByMandP',function(e){
	for (var key in e.result) {
			Ti.API.info("Beintoo user value for key " + key + " is " + e.result[key]); 
		}
	playerProxy.loginWithUserID(e.result['id']); // to login an user that has given username and password to authenticate.
});

userProxy.addEventListener('onUserRegistration',function(e){
	for (var key in e.result) {
			Ti.API.info("Beintoo user registration result for key " + key + " is " + e.result[key]); 
		}
});

// VgoodProxy definition

var vgoodProxy = beintoo.createVgood();

vgoodProxy.addEventListener('onVgoodGenerated',function(e){
	// With the result you should show the virtual good to the user
	for (var key in e.result) {
			Ti.API.info("Beintoo vgood value for key " + key + " is " + e.result[key]); 
		}
});

// Button events

// *** PLAYER ****
// PlayerLogin
button1.addEventListener('click',function(e)
{
	playerProxy.login();	
});

// PlayerLogin with user id. to be used when you know the id of the user that you want to login
button2.addEventListener('click',function(e)
{
	playerProxy.loginWithUserID("USER_ID_HERE");	
});

// to submit a score for a player. You need to pass the contest code to assign the score to a certain contest
button3.addEventListener('click',function(e){
	playerProxy.submitScore(5,"default");	
});

// Get score. to obtain all the scores of a player on your app (for every contest)
button4.addEventListener('click',function(e)
{
	playerProxy.getScore();
});

// Set Balance. to set the score of your player to a certain value
button5.addEventListener('click',function(e){
	playerProxy.setBalance(0,"default");	
});

// *** USER ***
button6.addEventListener('click',function(e){
	userProxy.getUser();	
});

button7.addEventListener('click',function(e){
	userProxy.getUserByMailAndPass("test@example.com","testPassword");	
});

button8.addEventListener('click',function(e){
	userProxy.registerUserWithMailAndNickname("test@example.com","testnick");	
});

// *** VGOOD ***

button9.addEventListener('click',function(e){
	vgoodProxy.getVgood();	
});

button10.addEventListener('click',function(e)
{
	playerProxy.logout();
});