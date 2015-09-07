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
		var loginContainer = document.getElementById("nav_bar:login:login_container");
		var win = document.getElementById("nav_bar:login:win_login");
		loginContainer.setAttribute("class", "login_container_shown");
		win.setAttribute("class", "win_login_shown");
		
		loginContainer.addEventListener("click", stopEvent ,false);
		function stopEvent(ev) {
			loginContainer.setAttribute("class", "login_container_hidden");
			win.setAttribute("class", "win_login_hidden");
			ev.stopPropagation();
		}
	}
	/*showLogin : function() {
		login.fadeBackground();
		login.showLoginWindow();
	},

	fadeBackground : function() {
		var elem = document.createElement("div");
		var body = document.querySelector("body");
		body.insertBefore(elem, document.getElementById("nav_bar"));
		elem.setAttribute("class", "overlay");
		elem.addEventListener("click", function() {
			body.removeChild(this);
			var win = document.getElementById("nav_bar:login:win_login").setAttribute("class", "win_login_hidden");
		}, false);
		console.log("HERE");
	},
	
	showLoginWindow : function() {
		var win = document.getElementById("nav_bar:login:win_login");
		win.setAttribute("class", "win_login_shown");
	}*/
}
