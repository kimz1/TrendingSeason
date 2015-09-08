var loginContainer, loginWin;

if(window.onload) {
	var tempOnload = window.onload;
	var newOnload = function() {
		tempOnload();
		init();
	};
	window.onload = newOnload;
} else {
	window.onload = init;
}

function init() {
	loginContainer = document.getElementById("nav_bar:login:login_container");
	loginWin = document.getElementById("nav_bar:login:win_login");
	loginContainer = document.getElementById("nav_bar:login:login_container");
	loginWin = document.getElementById("nav_bar:login:win_login");
	loginContainer.addEventListener("click", login.doClose ,true);
}
var login = {
	
	showLogin : function() {
		loginContainer.setAttribute("class", "login_container_shown");
		loginWin.setAttribute("class", "win_login_shown");
	},
		
	doClose : function(event) {
			if(!event.stopPropagation()) console.log("stopPropagation() SHOULD WORK / " + event.stopPropagation());
			if(!event.preventDefault()) console.log("preventDefault() SHOULD WORK / " + event.preventDefault());
			loginContainer.setAttribute("class", "login_container_hidden");
			loginWin.setAttribute("class", "win_login_hidden");
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
};
