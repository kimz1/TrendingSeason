package com.seasonalpatterns.downloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.seasonalpatterns.comm.Commodity;
import com.seasonalpatterns.exchange.CBOT;
import com.seasonalpatterns.exchange.Exchange;

public class QuandlAPI implements DataProvider{

	private static final String AUTH_CODE = "rZojuWxYcHY8jw5xLspm";
	private final String BASE = "https://www.quandl.com/api/v1/datasets";
	private StringBuilder sb = new StringBuilder();
	private final Map<String, String> tickerMap = new HashMap<>();
	private final Map<Exchange, String> exchangeMap = new HashMap<>();
	
	public QuandlAPI() {
		
		this.setTickerMap(); // creates a map of commodity names and Quandl tickers;
		this.setExchangeMap();
	}

	public InputStreamReader getData(Commodity comm, Date lastDate) {

		System.out.println("Retrieving data in QuandlAPI...");
		InputStreamReader in = null;
		try {
			URL url = new URL(this.buildURL(comm, lastDate));
			System.out.println(url.toString());
			in = new InputStreamReader(url.openStream());
			System.out.println("Data retrieved");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	public Map<String, String> getTickerMap() {
		return this.tickerMap;
	}
	
	private void setTickerMap() {
		this.tickerMap.put("Corn", "FUTURE_C");
	}
	
	public String getTicker(Commodity comm) {
		return this.tickerMap.get(comm.getName());
	}

	public String buildURL(Commodity comm, Date lastDate) {
		
		// Increase lastDate by 1 day;
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastDate);
		cal.roll(Calendar.DATE, true);

		sb.append(BASE);
		sb.append("/");
		sb.append(getExchange(comm.getExchange()));
		sb.append("/");
		sb.append(getTicker(comm));
		sb.append(comm.getMonth());
		sb.append(comm.getYear());
		sb.append(".csv?trim_start=");
		sb.append(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		sb.append("&trim_end=");
		sb.append(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		sb.append("&exclude_headers=true");
		sb.append("&auth_token=");
		sb.append(AUTH_CODE);
		sb.append("&column=4");
		return sb.toString();
	}

	public Map<Exchange, String> getExchangeMap() {
		return exchangeMap;
	}

	private void setExchangeMap() {
		this.exchangeMap.put(Exchange.getExchange("CBOT"), "OFDP");
	}
	
	public String getExchange(Exchange exch) {
		return this.exchangeMap.get(exch);
	}
}