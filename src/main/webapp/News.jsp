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
	function clicker(btnName){
	
		//console.log('veikia');	
		url = "News?button=" + btnName;
		location.href = url;
		//request.setAttribute("Ready", "Not_Ready");
	}
	
	function rdyBtn(text){
		var tempText = "";
		if(text == "Ready") tempText = true;
		else{
			tempText = false;
		}
		console.log(tempText);
		url = "News?ready=" + tempText;
		location.href = url;
	}
	
	function readyFunc(){	
		var url_string = location.href; //window.location.href
		var url = new URL(url_string);
		var ready = url.searchParams.get("ready") == null ? false : url.searchParams.get("ready");
		//var ready = document.getElementById('slaptas').value == "" ? false : document.getElementById('slaptas').value;
		
		if(ready == false){
			//url = "News?button=" + btnName + "&ready=" + getReady;
			//location.href = url;
			// request.setAttribute("Ready", "Not_Ready");
			document.getElementById('ready').innerText= 'Ready';
			ready = true;
		}
		else
		{
			//url = "News?button=" //+ btnName;
			//location.href = url;
			//request.setAttribute("Ready", "Set_Ready");
			//ready = false;
			document.getElementById('ready').innerText= 'Not ready';
		}
	}
	
	$( document ).ready(function() {
		readyFunc();
	});
	</script>
</body>
</html>