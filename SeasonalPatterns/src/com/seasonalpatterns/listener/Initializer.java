package com.seasonalpatterns.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.seasonalpatterns.dbmanager.DBManager;

@WebListener
public class Initializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		DBManager.getInstance();
		System.out.println("DBManager initialized...");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		System.out.println("ContextInitializer destroyed...");
	}
}
