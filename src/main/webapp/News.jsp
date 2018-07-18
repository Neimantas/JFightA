<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--===============================================================================================-->	
	<link rel="icon" type="image/png" href="resources/images/icons/favicon.png"/>
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="resources/vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="resources/fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="resources/vendor/animate/animate.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="resources/vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="resources/vendor/select2/select2.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="resources/css/news.css">

<!--===============================================================================================-->
<title>News</title>
</head>
<body>
<div class="container">
<div class= "menuContainer">
<div class = "logoutMenuLeft">
<div class="message">${ReadyMessage}</div>
</div>
<!-- 
<div class="logoutMenuCenter">UserName</div>
 -->

<div class = "logoutMenuRight">
	<button id="play" onclick="clicker(this.id)">Play</button>
	<button id="ready" onclick="rdyBtn(this.innerText)">${Ready}</button>
	<button id="logout" onclick="clicker(this.id)">Logout</button>

</div>

</div>
<div>
	<div class = "leftcolumn">
Online players. Get it somehow
	
	</div>
	<div class = "rightcolumn">
		<div class="textField">put here some text from admin</div>
	</div>
</div>
</div>

<input id="slaptas" type="hidden" value=${(ready == null) ? false : ready} ></input>
<!--===============================================================================================-->	
	<script src="resources/vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
	<script src="resources/vendor/bootstrap/js/popper.js"></script>
	<script src="resources/vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
	<script src="resources/vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
	<script src="resources/js/main.js"></script>
	<script>
		function clicker(btnName){														//For Play and Logout functions.
			url = "News?button=" + btnName;
			location.href = url;

		}
	
	function rdyBtn(text){																//When ready/Not Ready button is pressed do:
		var tempText = "";
		if(text == "Ready"){ 
			tempText = true;															//If button text is ready, the user status is not Ready. Param ready must be true.
		}																				
		else{																			//If button text is else - user is Ready. Param ready must be false.
			tempText = false;
		}
		console.log(tempText);
		url = "News?ready=" + tempText;													//Set String var with param.
		location.href = url;															//Set param to Url, that in future, we would see user is ready or not.
	}
	
	function readyFunc(){	
		var url = new URL(location.href); 												//Put current Url to variable "url"
		var ready = url.searchParams.get("ready") == null ? false : url.searchParams.get("ready");		//gettin parameters from url, 
																										//if there is no ready parameter -ready becomes false. 
																										//if not null - var ready = current param.
		if(ready == false || ready == 'false')											//If false is got as a param, its in String format.
		{											
			document.getElementById('ready').innerText= 'Ready';						//If user is not ready - show buttons text "Ready"
		}
		else
		{

			document.getElementById('ready').innerText= 'Not ready';					//If user is ready - show button text "Not ready""
		}
	}
	
	$( document ).ready(function() { 													//Starts this on Load
		readyFunc();																	//Starts readyFunc() on page load
	});
	</script>
</body>
</html>