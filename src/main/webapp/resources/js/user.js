function showLog() {
	var url_string = window.location.href
	var url = new URL(url_string);
	var l = url.searchParams.get("log");
	if (l == "true") {
		var x = document.getElementById("logBtn");
		x.style.display = "none";
	}
}

showLog();