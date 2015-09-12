//var loginContainer, winContainer, win;

$(document).ready(function(){
	$("html").addClass("js");
	
	var loginBg = $(".login_background");
	var loginForm = $(".login_form");
	$("#loginBtn").click(function(){
		loginBg.fadeIn(400);
		loginForm.show();
	});
	loginBg.click(function(){
		loginBg.hide();
		loginForm.hide();
	});
});