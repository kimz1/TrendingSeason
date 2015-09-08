var loginContainer, winContainer, win;

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
	loginContainer = document.getElementById("login_container");
	winContainer = document.getElementById("win_container");
	win = document.getElementById("win_login");
	winContainer.addEventListener("click", login.doClose ,true);
}
var login = {
	
	showLogin : function() {
		loginContainer.setAttribute("class", "login_container_visible");
		winContainer.setAttribute("class", "win_container_visible");
	},
		
	doClose : function(event) {
			loginContainer.setAttribute("class", "login_container_hidden");
			winContainer.setAttribute("class", "win_container_hidden");
			event.stopPropagation();
	}
};
