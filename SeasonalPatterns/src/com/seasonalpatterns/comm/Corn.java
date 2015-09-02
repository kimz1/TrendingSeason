package com.seasonalpatterns.comm;

import com.seasonalpatterns.exchange.Exchange;
import com.seasonalpatterns.helper.CommMonth;

public class Corn extends Commodity {
	
	public Corn(int year, CommMonth month, Exchange exchange) {

		this.setYear(year);
		this.setMonth(month);
		this.setExchange(exchange);
		this.setName("Corn");

		this.setUpdateTime(1413411594812L);
		this.setDescription("This is a description of the Corn commodity...");
		this.setCategory("Grain/Oilseed");
		this.setTradingMonths(new CommMonth[] { CommMonth.H, CommMonth.K, CommMonth.N,
				CommMonth.U, CommMonth.Z });
		this.setTickSize(0.25);
		this.setTickPrice(12.5);
		this.setFND(10);
		this.setDataImporter(this);
	}	
}