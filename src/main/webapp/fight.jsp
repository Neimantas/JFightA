
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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


	<div class="container">
		<div class="row">
			<div class="col-sm-4">
			
			${playerAName} <br/>
			
			Health:
			${healthA}
			
			</div>
			<div class="col-sm-4">
				Actions
				<form method="post" action="/JFight/fight">
					<div class="row">

						<div class="col-sm-6">

							Attack:
							<div class="form-check">
								<input class="form-check-input attack" type="checkbox"
									name="attackHead" id="attackAction1" value="head"> <label
									class="form-check-label" for="attackAction1"> Head </label>
							</div>
							<div class="form-check">
								<input class="form-check-input attack" type="checkbox"
									name="attackBody" id="attackAction2" value="body"> <label
									class="form-check-label" for="attackAction2"> Body </label>
							</div>
							<div class="form-check">
								<input class="form-check-input attack" type="checkbox"
									name="attackArms" id="attackAction3" value="arms"> <label
									class="form-check-label" for="attackAction3"> Arms </label>
							</div>
							<div class="form-check">
								<input class="form-check-input attack" type="checkbox"
									name="attackLegs" id="attackAction4" value="legs"> <label
									class="form-check-label" for="attackAction4"> Legs </label>
							</div>
						</div>

						<div class="col-sm-6">
							Defend:
							<div class="form-check">
								<input class="form-check-input defence" type="checkbox"
									name="defenceHead" id="defenceAction1" value="head">
								<label class="form-check-label" for="defenceAction1">
									Head </label>
							</div>
							<div class="form-check">
								<input class="form-check-input defence" type="checkbox"
									name="defenceBody" id="defenceAction2" value="body">
								<label class="form-check-label" for="defenceAction2">
									Body </label>
							</div>
							<div class="form-check">
								<input class="form-check-input defence" type="checkbox"
									name="defenceArms" id="defenceAction3" value="arms">
								<label class="form-check-label" for="defenceAction3">
									Arms </label>
							</div>
							<div class="form-check">
								<input class="form-check-input defence" type="checkbox"
									name="defenceLegs" id="defenceAction4" value="legs">
								<label class="form-check-label" for="defenceAction4">
									Legs </label>
							</div>

						</div>


					</div>
					<div class="row">
						
						<div class="col-sm-4"></div>
						<div class="col-sm-4">
							
							<input type="submit" value="Attack!">
							
						</div>
						<div class="col-sm-4"></div>
					</div>
					
					
				</form>

			</div>
			<div class="col-sm-4">
			
			${playerBName} </br>
			
			Health:
			${healthB}
			
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