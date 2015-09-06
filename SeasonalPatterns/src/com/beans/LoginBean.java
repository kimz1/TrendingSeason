package com.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dbmanager.LoginDAO;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{

	private static final long serialVersionUID = 8713407889545715310L;
	private String username, password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String login(String username, String password) {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		
		boolean isValid = LoginDAO.validate(username, password);
		
		if(isValid) {
			// if success, store the username in the session.
			
			session.setAttribute("username", username);
			session.setMaxInactiveInterval(0);
			return "success";
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid username or password!"));
			session.invalidate();
			return "failure";
		}
	}
	
	public String logout() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
		if(session != null) session.invalidate();
		return "failure";
	}
	
	public String getRequestURI() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String URI = ctx.getExternalContext().getRequestContextPath();
		System.out.println("getRequestContextPath = " + URI);
		
		HttpServletRequest rq = (HttpServletRequest) ctx.getExternalContext().getRequest();
		String rqURI = rq.getRequestURI();
		
		System.out.println("getRequestUri = " + rqURI);
		return "index.xhtml";
	}
}