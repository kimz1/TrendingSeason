package com.dbmanager;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebFilter(urlPatterns = {"/ts/*"})
public class AuthFilter implements  Filter {

	@Override
	public void destroy() {
		System.out.println("Filter destroyed");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String URI = request.getRequestURI();
		
		if(URI.indexOf("/ts/") < 0 || session.getAttribute("username") != null) {
			chain.doFilter(req, res);
		} else {
			response.sendRedirect(request.getContextPath() + "/loginError.xhtml");
		}
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		System.out.println("Filter initialized");
	}

}
