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
	<button class="btn-default" id="play" onclick="clicker(this.id)">Play</button>
	<button class="btn-default" id="ready" onclick="rdyBtn(this.innerText)">${Ready}</button>
	<button class="btn-default" id="logout" onclick="clicker(this.id)">Logout</button>

</div>

</div>
<div>
	<div class = "leftcolumn">
		<c:forEach items="${readyUsers}"></c:forEach>
		<button class="btn-default" id="refresh" onclick="refresh()">Refresh</button>
	</div>
	<div class = "rightcolumn">
		<div class="textField">
put here some text from admin</div>
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
<!--===============================================================================================-->
	<script src="resources/js/news.js"></script>	

</body>
</html>