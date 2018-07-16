//https://stackoverflow.com/questions/17732704/how-to-make-the-checkbox-unchecked-by-default-always
//Uncheck all checkboxes
//document.onload = function(){
//	let checkboxes = document.getElementsByTagName('input');
//
//	for (let i=0; i<checkboxes.length; i++)  {
//	  if (checkboxes[i].type == 'checkbox')   {
//	    checkboxes[i].checked = false;
//	  }
//	}
//}

window.onload = function() {
	
	noMoreThanTwoSelectedClass('attack');				//cant checkbox more than two.
	noMoreThanTwoSelectedClass('defence');
	blockSubmit();										//waiting for other player submit
	actionTimer();										//make timer.
}
// type - attack/defence
//function noMoreThanTwoSelected(type) {
//	
//	
//	let checkboxes = document.getElementsByName(type);
//	checkboxes.forEach(e => {
//		e.addEventListener('change', function(){
//			let counter = 0;
//			let checkedCheckboxes = document.getElementsByName(type);
//			checkedCheckboxes.forEach(ce => {
//			if(ce.checked == true) { 
//				counter++;
//			}});
//			if(counter > 2) {
//				this.checked = false;
//			};
//		});
//	});
//	
//}

function noMoreThanTwoSelectedClass(type) {
	
	
	let checkboxes = document.getElementsByClassName(type);
	for(let i=0; i<checkboxes.length; i++) {
	
		checkboxes[i].addEventListener('change', function(){
			let counter = 0;
			let checkedCheckboxes = document.getElementsByClassName(type);
			for(let j=0; j<checkedCheckboxes.length;j++) { 
				if(checkedCheckboxes[j].checked == true) { 
					counter++;
				}
				if(counter > 2) {
					this.checked = false;
				}
			}
		});
	}
	
}
//block submit button and show wait message while waiting for response
function blockSubmit() {											//actions when submit is pressed.
	$('#actionForm').submit(function() {							
		$('#submitButton').attr('disabled', true);					//disable submit button
	    $('#wait').css('visibility', 'visible');
	    stopTimer = true;											//stop autosubmit when time will end.
	    $('#timer').css('visibility', 'hidden');					//hide timer. (the time stoped for that user)
	});
}
//global variable to control timer behavior.
stopTimer = false;

function actionTimer(forceCancel) {
	let timer = 30;
	countdown = setInterval(function () {							//make change every second
		
		if(stopTimer) {												//stop function (counting)
			clearInterval(countdown);								//exit from function
		}
		
	    document.getElementById("timer").innerHTML = --timer;		//add number to page.
	    
	    if(timer <= 0) {											//if time has eded, make autosubmin (with or without actions)
	    	autoSubmit();
	        clearInterval(countdown);
	    }

	}, 1000);														//miliseconds
}

function autoSubmit() {
	$('#actionForm').submit();
}

//function autoSubmitStopper(){
//	stopTimer = true;
//}



