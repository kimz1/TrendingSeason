package com.seasonalpatterns.downloader;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import com.seasonalpatterns.comm.Commodity;
import com.seasonalpatterns.exchange.Exchange;

public interface DataProvider {

	public InputStreamReader getData(Commodity comm, Date lastDate);
	public Map<String, String> getTickerMap();
	public String getTicker(Commodity comm);
	public Map<Exchange, String> getExchangeMap(); // returns the map of data provider's exchange codes
}
