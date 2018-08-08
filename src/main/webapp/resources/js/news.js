url = "News?";

function clicker(btnName){																//For Play and Logout functions.
//			url = "News?button=" + btnName;
			location.href = url;
}

function info(){
	var readyPlayers = document.getElementById("onlinePlayers");
	var selectas = readyPlayers.options[readyPlayers.selectedIndex];
	if(selectas != undefined){
		var url2 ="user?userId="+selectas.value;
		location.href = url2;
	}else{
		location.href = url;															//if there is no selection - refresh page
	}
}

function play(){
	var readyPlayers = document.getElementById("readyPlayers");
	var selectas = readyPlayers.options[readyPlayers.selectedIndex];
	if(selectas != undefined){
		var url2 ="News?button=play&selectedPlayer="+selectas.value;
		location.href = url2;
	}else{
		location.href = url;															//if there is no selection - refresh page
	}
}
	
function rdyBtn(text){																	//When ready/Not Ready button is pressed do:
		var tempText = "";
		if(text == "Ready"){ 
			tempText = false;															//If button text is ready, the user status is not Ready. Param ready must be true.				    
		}																				
		else{																			//If button text is else - user is Ready. Param ready must be false.
			tempText = true;
			
		}
		url = "News?ready=" + tempText;													//Set String var with param.
		location.href = url;															//Set param to Url, that in future, we would see user is ready or not.
}
	
function readyFunc(){	
		url = new URL(location.href); 													//Put current Url to variable "url"
		var ready = url.searchParams.get("ready") == null ? false : url.searchParams.get("ready");		//gettin parameters from url, 
																						//if there is no ready parameter -ready becomes false. 
																						//if not null - var ready = current param.
		if(ready == false || ready == 'false'){											//If false is got as a param, its in String format.
			document.getElementById('ready').innerText= 'Not Ready';					//If user is not ready - show buttons text "Not Ready"
			document.getElementById('ready').classList.add("btn-danger")				//make button red.
		      
		} else {
			document.getElementById('ready').innerText= 'Ready';						//If user is ready - show button text "Ready""
			document.getElementById('ready').classList.add("btn-success");				//make button green, green as grass.
		}
}
	
$( document ).ready(function() { 														//Starts this on Load
		readyFunc();																	//Starts readyFunc() on page load
});