<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="refresh" content="5" />
<!--===============================================================================================-->
<link rel="icon" type="image/png"
	href="resources/images/icons/favicon.png" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="resources/vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="resources/fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="resources/vendor/animate/animate.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="resources/vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="resources/vendor/select2/select2.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="resources/css/news.css">

<!--===============================================================================================-->
<title>News - JFight</title>
</head>
<body>

	<nav class="navbar navbar-expand-sm navbar-dark bg-dark flex-row">
	<a class="navbar-brand mr-auto mb-0 h1" href="/JFight/News">JFight</a>
	<ul class="navbar-nav flex-row mr-lg-0">
		<li class="nav-item dropdown"><a
			class="nav-link dropdown-toggle mr-3 mr-lg-0"
			id="navbarDropdownMenuLink" data-toggle="dropdown"
			aria-haspopup="true" aria-expanded="false"><i
				class="fa fa-user-circle" aria-hidden="true"></i>${userName}<span
				class="caret"></span> </a>
			<div class="dropdown-menu dropdown-menu-right"
				aria-labelledby="navbarDropdownMenuLink">
				<a class="dropdown-item" href="/JFight/user?log=false">Character
					Info</a>
				<div class="dropdown-divider"></div>
				<a class="dropdown-item" href="/JFight/logout"><i
					class="fa fa-sign-out" aria-hidden="true"></i>Logout</a>
			</div></li>
	</ul>
	<button class="navbar-toggler ml-lg-0" type="button"
		data-toggle="collapse" data-target="#navbarSupportedContent"
		aria-controls="navbarSupportedContent" aria-expanded="false"
		aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	</nav>
	<nav class="navbar navbar-expand-md navbar-light py-md-0">
	<div class="collapse navbar-collapse" id="navbarSupportedContent">
		<ul class="navbar-nav ml-1">
			<li class="nav-item mx-2"><a class="nav-link"
				href="/JFight/News">Home</a></li>
			<li class="nav-item mx-2"><a class="nav-link disabled" href="">Items</a></li>
		</ul>
	</div>
	</nav>

	<div class="container">
		<div class="row">
			<div class="col-md-7 col-sm-12">
				<div class="row mx-auto mb-2">
					<div class="col text-center">
						<div class="card">
							<div class="card-body">
								<h5 class="card-title">News title</h5>
								<h6 class="card-subtitle mb-2 text-muted">News subtitle</h6>
								<p class="card-text">Some quick example text to build on the
									card title and make up the bulk of the card's content.</p>
							</div>
						</div>
					</div>
				</div>
				<div class="row mx-auto mb-2">
					<div class="col text-center">
						<div class="card">
							<div class="card-body">
								<h5 class="card-title">News title</h5>
								<h6 class="card-subtitle mb-2 text-muted">News subtitle</h6>
								<p class="card-text">Some quick example text to build on the
									card title and make up the bulk of the card's content.</p>
							</div>
						</div>
					</div>
				</div>
				<div class="row mx-auto mb-2">
					<div class="col text-center">
						<div class="card">
							<div class="card-body">
								<h5 class="card-title">News title</h5>
								<h6 class="card-subtitle mb-2 text-muted">News subtitle</h6>
								<p class="card-text">Some quick example text to build on the
									card title and make up the bulk of the card's content.</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-4 col-sm-12">
				<div class="row">
					<div class="col text-center">
						<h5>Online Players</h5>
					</div>
				</div>
				<div class="row mb-0">
					<div class="col">
						<div class="form-group mb-1">
							<select class="form-control" size="10" id="onlinePlayers">
								<c:forEach items="${onlinePlayers}" var="players">
									<option value="${players.key}">${players.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<div class="col pr-3">
						<button class="btn btn-dark btn-sm float-right" id="info"
							onclick="info()">Info</button>
					</div>
				</div>

				<div class="row">
					<div class="col text-center">
						<h5>Fight Lobby</h5>
					</div>
				</div>
				<div class="row mb-0">
					<div class="col">
						<div class="form-group mb-1">
							<select class="form-control" size="10" id="readyPlayers">
								<c:forEach items="${readyPlayers}" var="players">
									<option value="${players.key}">${players.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col pr-3">
						<button class="btn btn-dark btn-sm float-right ml-1 mb-1"
							id="play" onclick="play()">Play</button>
						<button class="btn btn-dark btn-sm float-right ml-1 mb-1"
							id="refresh" onclick="clicker(this.id)">Refresh</button>
						<button class="btn btn-sm float-right" id="ready"
							onclick="rdyBtn(this.innerText)">${Ready}</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<input id="slaptas" type="hidden"
		value=${(ready == null) ? false : ready}></input>
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