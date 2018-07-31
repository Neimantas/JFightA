
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


	<div class="container mt-1">
		<div class="row">
			<div class="col-sm-4">
				<div class="row mb-1">
					<div
						class="col border border-dark rounded text-center font-weight-bold">${playerAName}</div>
				</div>
				<div class="row mb-1">
					<div class="col border border-dark rounded py-1">
						<h6>Health: ${healthA}</h6>
						<div class="progress">
							<div class="progress-bar bg-danger text-center text-black"
								role="progressbar" style="width: ${healthA}%"
								aria-valuenow="${healthA}" aria-valuemin="0" aria-valuemax="100"></div>
						</div>
					</div>
				</div>
				<div class="row mb-1">
					<div class="col border border-dark rounded py-1">
						<div class="text-center">
							<img src="imageServlet?id=${idA}&user=a" class="rounded"
								width=80%>
						</div>
					</div>
				</div>
				<div class="row mb-1">
					<div class="col-6 border border-dark rounded py-1">
						<div class="list-group">
							<div class="text-center">Attack Item</div>
							<div class="text-center">
								<img src="itemImageServlet?itemId=${attackItemA}"
									class="rounded" width=90%>
							</div>
						</div>
					</div>
					<div class="col-6 border border-dark rounded py-1">
						<div class="list-group">
							<div class="text-center">Defence Item</div>
							<div class="text-center">
								<img src="itemImageServlet?itemId=${defenceItemA}"
									class="rounded" width=90%>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-3 mx-1">
				<div class="row mb-1">
					<div
						class="col border border-dark rounded text-center font-weight-bold">Actions</div>
				</div>

				<form id="actionForm" method="post" action="/JFight/fight">
					<div class="row mb-1">
						<div class="col-sm-6 border border-dark rounded text-center">
							Attack:
							<div class="form-check">
								<input class="form-check-input attack mx-1" type="checkbox"
									name="attackHead" id="attackAction1" value="head"> <label
									class="form-check-label ml-0" for="attackAction1">Head</label>
							</div>
							<div class="form-check">
								<input class="form-check-input attack mx-1" type="checkbox"
									name="attackBody" id="attackAction2" value="body"> <label
									class="form-check-label ml-0" for="attackAction2">Body</label>
							</div>
							<div class="form-check">
								<input class="form-check-input attack mx-1" type="checkbox"
									name="attackArms" id="attackAction3" value="arms"> <label
									class="form-check-label ml-0" for="attackAction3">Arms</label>
							</div>
							<div class="form-check">
								<input class="form-check-input attack mx-1" type="checkbox"
									name="attackLegs" id="attackAction4" value="legs"> <label
									class="form-check-label ml-0" for="attackAction4">Legs</label>
							</div>
						</div>

						<div class="col-sm-6 border border-dark rounded text-center">
							Defend:
							<div class="form-check">
								<input class="form-check-input defence mx-1" type="checkbox"
									name="defenceHead" id="defenceAction1" value="head"> <label
									class="form-check-label" for="defenceAction1">Head</label>
							</div>
							<div class="form-check">
								<input class="form-check-input defence mx-1" type="checkbox"
									name="defenceBody" id="defenceAction2" value="body"> <label
									class="form-check-label" for="defenceAction2">Body</label>
							</div>
							<div class="form-check">
								<input class="form-check-input defence mx-1" type="checkbox"
									name="defenceArms" id="defenceAction3" value="arms"> <label
									class="form-check-label" for="defenceAction3">Arms</label>
							</div>
							<div class="form-check">
								<input class="form-check-input defence mx-1" type="checkbox"
									name="defenceLegs" id="defenceAction4" value="legs"> <label
									class="form-check-label" for="defenceAction4">Legs</label>
							</div>
						</div>
					</div>
					<div class="row mb-1">
						<div class="col text-center">
							<input class="btn btn-dark" id="submitButton" type="submit"
								value="Attack!">
						</div>
					</div>
				</form>
				<div class="row mb-1">
					<div class="col-12 text-center" id="timer">30</div>
					<div class="col-12 text-center" id="wait"
						style="visibility: hidden;">WAITING</div>
				</div>
			</div>
			<div class="col-sm-4">
				<div class="row mb-1">
					<div
						class="col border border-dark rounded text-center font-weight-bold">${playerBName}</div>
				</div>
				<div class="row mb-1">
					<div class="col border border-dark rounded py-1">
						<h6>Health: ${healthB}</h6>
						<div class="progress">
							<div class="progress-bar bg-danger text-center text-black"
								role="progressbar" style="width: ${healthB}%"
								aria-valuenow="${healthB}" aria-valuemin="0" aria-valuemax="100"></div>
						</div>
					</div>
				</div>
				<%-- <div>
					<img src="imageServlet?id=${idA}&user=a" height="300" width="200">
				</div> --%>
				<div class="row mb-1">
					<div class="col border border-dark rounded py-1">
						<div class="text-center">
							<img src="imageServlet?id=${idB}&user=b" class="rounded"
								width=80%>
						</div>
					</div>
				</div>
				<div class="row mb-1">
					<div class="col-6 border border-dark rounded py-1">
						<div class="list-group">
							<div class="text-center">Attack Item</div>
							<div class="text-center">
								<img src="itemImageServlet?itemId=${attackItemB}"
									class="rounded" width=90%>
							</div>
						</div>
					</div>
					<div class="col-6 border border-dark rounded py-1">
						<div class="list-group">
							<div class="text-center">Defence Item</div>
							<div class="text-center">
								<img src="itemImageServlet?itemId=${defenceItemB}"
									class="rounded" width=90%>
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
	<script src="resources/js/main.js"></script>
	<!--===============================================================================================-->
	<script src="resources/js/fightPageControls.js"></script>

</body>
</html>