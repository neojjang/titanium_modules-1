// Beintoo Titanium Module example

var window = Ti.UI.createWindow({
	backgroundColor:'white'
});

var BeintooTitanium = require('ti.beintoo');

// TODO: GET AN APIKEY @ http://business.beintoo.com
BeintooTitanium.setApiKey("PutHereYourAPIkey"); // TODO: change this string with 
 
BeintooTitanium.setDebug(true);

// TODO: Remember to switch to production when testing sessions are over
BeintooTitanium.setUseSandbox(true);

BeintooTitanium.addEventListener('onLogin',function(e){		
	var toast = Titanium.UI.createNotification({
	    duration: 2000,
	    message: "player login callback"+e.player.guid
	});
	toast.show();
});

// LOGIN THE PLAYER ON APP START
BeintooTitanium.playerLogin();

/* BEINTOO START TESTS */
var start = Titanium.UI.createButton({
   title: 'Start Beintoo'
});
start.addEventListener('click',function(e)
{	
	BeintooTitanium.beintooStart(true);    
});


/* SUBMIT SCORE TESTS */
var submitScore = Titanium.UI.createButton({
   title: 'Submit Score',
   top:10,
   left:100
});
submitScore.addEventListener('click',function(e)
{	
	
	BeintooTitanium.addEventListener('onSubmitComplete',function(){
		var toast = Titanium.UI.createNotification({
		    duration: 2000,
		    message: "submit complete callback"
		});
		toast.show();
	});	
	
	BeintooTitanium.submitScore(10, null, true); // SUBMIT 10 POINTS AND DISPLAY MESSAGE
});

/* GET VGOOD TEST */
var getVgood = Titanium.UI.createButton({
   title: 'Get Vgood',
   top:10,
   left:10
});
getVgood.addEventListener('click',function(e)
{	
	BeintooTitanium.addEventListener('onVgood',function(e){
		var toast = Titanium.UI.createNotification({
		    duration: 2000,
		    message: "Vgood complete callback " + e.Vgood.vgoods.get(0).getRealURL 
		});
		toast.show();
		 
	});	
	
	BeintooTitanium.GetVgood(null, true); // GET A MULTIPLE VGOOD 
});

/* SUBMIT SCORE WITH VGOOD TEST */
var submitGetVgood = Titanium.UI.createButton({
   title: 'Submit score with vgood check',
   top:65,
   left:10
});
submitGetVgood.addEventListener('click',function(e)
{	
	
	BeintooTitanium.addEventListener('onVgood',function(e){
		var toast = Titanium.UI.createNotification({
		    duration: 2000,
		    message: "Vgood complete callback " + e.Vgood.vgoods.get(0).getRealURL
		});
		toast.show();
		 
	});	
	
	// TRESHOLD IS 5, THE SCORE IS 10 IT WILL RETRIEVE A VGOOD
	BeintooTitanium.submitScoreWithVgoodCheck(10, 5, null); 
});

window.add(start);
window.add(submitGetVgood);
window.add(submitScore);
window.add(getVgood);
window.open();
