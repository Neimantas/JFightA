<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>J Fight</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
<link rel="stylesheet" type="text/css" href="resources/css/util.css">
<link rel="stylesheet" type="text/css" href="resources/css/main.css">
<!--===============================================================================================-->
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
					<a class="dropdown-item" href="/JFight/LogOutServlet"><i class="fa fa-sign-out"
						aria-hidden="true"></i>Logout</a>
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
			<div class="col-sm-12 text-center">
				<img
					src="https://vignette.wikia.nocookie.net/simpsons/
				images/e/e9/Nelson_Ha-Ha.jpg/revision/latest
				/scale-to-width-down/350?cb=20121205194057" />
				<h2>HAHA!YOU LOST!</h2>
			</div>

		</div>
	</div>



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
	<script src="resources/js/fightPageControls.js"></script>

</body>
</html>