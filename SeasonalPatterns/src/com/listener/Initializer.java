package com.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dbmanager.DataDAO;

@WebListener
public class Initializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		DataDAO.getInstance();
		System.out.println("DBManager initialized...");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		System.out.println("ContextInitializer destroyed...");
	}
}
