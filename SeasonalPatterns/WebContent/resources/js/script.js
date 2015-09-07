if(window.onload) {
	var tempOnload = window.onload;
	var newOnload = function() {
		tempOnload();
		init();
	}
	window.onload = newOnload;
} else {
	window.onload = init;
}

function init() {

}
var login = {
	
	showLogin : function() {
		login.fadeBackground();
	},

	fadeBackground : function() {
		var elem = document.createElement("div");
		var body = document.querySelector("body");
		elem.addEventListener("click", function() {
			body.removeChild(this);
		}, false);
		body.appendChild(elem);
		elem.setAttribute("class", "overlay");
		console.log("HERE");
	},
	
	showLoginWindow : function() {
		var win = document.createElement("div");
	}
}
