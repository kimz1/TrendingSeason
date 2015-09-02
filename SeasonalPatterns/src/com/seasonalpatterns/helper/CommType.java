// Declares the Type of a Commodity and its specifications.

package com.seasonalpatterns.helper;

public enum CommType {
	
	C ("Corn_CBOT", "FUTURE_C", "Grain/Oilseed", "CBOT", "OFDP", new CommMonth[] {CommMonth.H, CommMonth.K,
			CommMonth.N, CommMonth.U, CommMonth.Z}, 0.25 , 12.5, 10, 1413411594812L),
	O ("Oats", "FUTURE_O", "Grain/Oilseed", "CBOT", "OFDP", new CommMonth[] {CommMonth.H, CommMonth.K,
			CommMonth.N, CommMonth.U, CommMonth.Z}, 0.25 , 12.5, 10, 1413412785412L),
	W ("Wheat", "FUTURE_W", "Grain/Oilseed", "CBOT", "OFDP", new CommMonth[] {CommMonth.H, CommMonth.K,
			CommMonth.N, CommMonth.U, CommMonth.Z}, 0.25 , 12.5, 10, 1413411720000L);
	
	
	private String description = null;
	private String quandlTicker = null;
	private String category = null;
	private String exchange = null;
	private String quandlExchange = null;
	private CommMonth[] tradingMonths = null;
	private double tickSize = 0;
	private double tickPrice = 0;
	private int firstNoticeDay = 0;
	private long updateTime = 0;
	
	private CommType(String description, String quandlTicker, 
			String category, String exchange, String quandlExchange,
			CommMonth[] tradingMonths, double tickSize, double tickPrice, 
			int firstNoticeDay, long updateTime) {
		
		this.description = description;
		this.quandlTicker = quandlTicker;
		this.category = category;
		this.exchange = exchange;
		this.quandlExchange = quandlExchange;
		this.tradingMonths = tradingMonths;
		this.tickSize = tickSize;
		this.tickPrice = tickPrice;
		this.firstNoticeDay = firstNoticeDay;
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return this.name();
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getQuandlTicker() {
		return this.quandlTicker;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String getExchange() {
		return this.exchange;
	}
	
	public String getQuandlExchange() {
		return this.quandlExchange;
	}
	
	public CommMonth[] getTradingMonths() {
		return this.tradingMonths;
	}
	
	public double getTickSize() {
		return this.tickSize;
	}
	
	public double getTickPrice() {
		return this.tickPrice;
	}
	
	public int getFirstNoticeDay() {
		return this.firstNoticeDay;
	}
	
	public long getUpdateTime() {
		return this.updateTime;
	}
}
