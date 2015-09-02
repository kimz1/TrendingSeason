package com.seasonalpatterns.helper;

import java.lang.reflect.InvocationTargetException;

import com.seasonalpatterns.comm.Commodity;
import com.seasonalpatterns.exchange.Exchange;

public class CommFactory {

	/*
	 * className = fully specified class name of the commodity subclass 
	 * year = year in format [yyyy] 
	 * month = CommMonth object of a month [F, G, H, J, K, M, N, Q, U, V, X, Z] 
	 * exchange = name of the exchange according to the specification
	 */

	public static Commodity createCommodity(String className, int year,
			CommMonth month, String exchange) {

		Commodity comm = null;

		try {
			Class<?> c = Class.forName(className);
			
			Class<?>[] cArg = new Class[3];
			cArg[0] = int.class;
			cArg[1] = CommMonth.class;
			cArg[2] = Exchange.class;

			comm = (Commodity) c.getDeclaredConstructor(cArg).newInstance(
					year, month, Exchange.getExchange(exchange));

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
		return comm;
	}

	public static Commodity createCommodity(String className, String year,
			String month, String exchange) {

		Commodity comm = null;

		try {
			Class<?> c = Class.forName(className);

			Class<?>[] cArg = new Class[3];
			cArg[0] = int.class;
			cArg[1] = CommMonth.class;
			cArg[2] = Exchange.class;

			comm = (Commodity) c.getDeclaredConstructor(cArg).newInstance(
					Integer.valueOf(year), CommMonth.valueOf(month), Exchange.getExchange(exchange));

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException 
				| InvocationTargetException | NoSuchMethodException 
				| SecurityException e) {
			e.printStackTrace();
		}

		return comm;
	}
}
