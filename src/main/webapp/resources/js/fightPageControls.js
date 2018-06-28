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
	
	noMoreThanTwoSelected('attackAction');
	noMoreThanTwoSelected('defenceAction');
	
}
// type - attack/defence
function noMoreThanTwoSelected(type) {
	
	
	let checkboxes = document.getElementsByName(type);
	checkboxes.forEach(e => {
		e.addEventListener('change', function(){
			let counter = 0;
			let checkedCheckboxes = document.getElementsByName(type);
			checkedCheckboxes.forEach(ce => {
			if(ce.checked == true) { 
				counter++;
			}});
			if(counter > 2) {
				this.checked = false;
			};
		});
	});
	
}
