package com.seasonalpatterns.exchange;

public class CBOT extends Exchange {
	
	private final static CBOT INSTANCE = new CBOT();

	private CBOT() {
		this.setMarketName("CBOT");
		this.setTicker("C");
	}
	
	public static CBOT getInstance() {
		return INSTANCE;
	}
}
