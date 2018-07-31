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
<link rel="stylesheet" type="text/css" href="resources/css/user.css">
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
					class="fa fa-user-circle" aria-hidden="true"></i>${currentUserName}<span
					class="caret"></span> </a>
				<div class="dropdown-menu dropdown-menu-right"
					aria-labelledby="navbarDropdownMenuLink">
					<a class="dropdown-item" href="/JFight/user?log=false">Character
						Info</a>
					<div class="dropdown-divider"></div>
					<a class="dropdown-item" href="/JFight/logout"><i class="fa fa-sign-out"
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

	<div class="container w-50 mt-1">
		<div class="row mb-1">
			<div class="col-lg-5 col-md-12 mx-lg-1">
				<div class="row mb-1">
					<div
						class="col border border-dark rounded font-weight-bold text-center py-1">${userName}</div>
				</div>
				<div class="row mb-1">
					<div class="col border border-dark rounded text-center py-1">Level:
						${level}</div>
				</div>
				<div class="row mb-1 ">
					<div class="col border border-dark rounded py-1">
						Experience:
						<div class="progress">
							<div class="progress-bar bg-warning text-center text-black"
								role="progressbar" style="width: ${experience}%"
								aria-valuenow="${experience}" aria-valuemin="0"
								aria-valuemax="100">${experience}/100</div>
						</div>
					</div>
				</div>
				<div class="row mb-1">
					<div class="col border border-dark rounded py-1">
						Health Points:
						<div class="progress">
							<div class="progress-bar bg-danger text-center text-black"
								role="progressbar" style="width: ${healthPoints}%"
								aria-valuenow="${healthPoints}" aria-valuemin="0"
								aria-valuemax="100">${healthPoints}</div>
						</div>
					</div>
				</div>
				<div class="row mb-1">
					<div class="col border border-dark rounded text-center py-1">
						<div class="col-12 font-weight-bold">Character Stats</div>
						<div class="col-12">Strength: ${strenght}</div>
					</div>
				</div>
			</div>
			<div class="col-lg col-md-12">
				<div class="row mb-1">
					<div class="col border border-dark rounded py-1">
						<div class="text-center">
							<img src="imageServlet?id=${userId}" class="rounded"
								width=80%>
						</div>
					</div>
				</div>
				<div class="row mb-1">
					<div class="col-6 border border-dark rounded py-1">
						<div class="list-group">
							<div class="text-center">Attack Item</div>
							<div class="text-center">
								<img src="itemImageServlet?itemId=${attackItem}" class="rounded"
									width=90%>
							</div>
						</div>
					</div>
					<div class="col-6 border border-dark rounded py-1">
						<div class="list-group">
							<div class="text-center">Defence Item</div>
							<div class="text-center">
								<img src="itemImageServlet?itemId=${defenceItem}"
									class="rounded" width=90%>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row mb-1">
			<div class="col p-0">
				<!-- Button trigger modal -->
				<button type="button" class="btn btn-dark float-right"
					data-toggle="modal" data-target="#exampleModalLong" id="logBtn">Fight
					Log</button>

				<!-- Modal -->
				<div class="modal fade" id="exampleModalLong" tabindex="-1"
					role="dialog" aria-labelledby="exampleModalLongTitle"
					aria-hidden="true">
					<div class="modal-dialog modal-lg" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title mx-auto" id="exampleModalLongTitle">Fight
									History</h5>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<c:forEach items="${tableString}" var="block">
    								${block}
								</c:forEach>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
							</div>
						</div>
					</div>
				</div>
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
	<script src="resources/js/user.js"></script>
	<!--===============================================================================================-->
	
</body>
</html>