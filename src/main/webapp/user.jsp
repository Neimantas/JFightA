
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

	<div class="container w-50 mt-5">
		<div class="row mb-1">
			<div
				class="col border border-dark rounded font-weight-bold text-center py-1">${userId}</div>
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
			<div class="col-6 border border-dark rounded text-center py-1">Health
				Points: ${healthPoints}</div>
			<div class="col-6 border border-dark rounded text-center py-1">Strength:
				${strenght}</div>
		</div>
		<div class="row mb-1">
			<div class="col border border-dark rounded py-1">
				<div class="text-center">
					<img src="..." class="rounded">
				</div>
			</div>
		</div>
		<div class="row mb-1">
			<div class="col-6 border border-dark rounded py-1">Attack Item</div>
			<div class="col-6 border border-dark rounded py-1">Defense Item</div>
		</div>
		<div class="row mb-1">
			<div class="col p-0">
				<button type="button" class="btn btn-outline-dark float-right">Log</button>
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