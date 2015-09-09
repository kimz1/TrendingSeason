//var loginContainer, winContainer, win;

$(document).ready(function(){
	$("#login").click(function(){
		$("#login_container").attr("class", "login_container_visible");
		$("#win_login").attr("class", "win_login_visible");
	});
	$("#login_container").click(function(event){
		event.stopPropagation();
		$(this).attr("class", "login_container_hidden");
		$("#win_login").attr("class", "win_container_hidden");
	});
});