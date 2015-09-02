package com.seasonalpatterns;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seasonalpatterns.helper.CommTracker;

@WebServlet("/tester")
public class Tester extends HttpServlet{

	private static final long serialVersionUID = 2585416182968418214L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

		PrintWriter out = res.getWriter();
		res.setContentType("text/html");
		
		CommTracker tracker = CommTracker.getInstance();
		tracker.run();
		
		out.println("<!DOCTYPE html>");
		out.println("<html><head><title>Title</title></head><body>");
		out.println("<h1>Working...</h1></body></html>");
		
		out.close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		doGet(req, res);
	}
}