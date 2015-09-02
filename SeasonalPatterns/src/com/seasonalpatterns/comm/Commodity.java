// The Commodity class defines all properties for a particular commodity 
// (declares its type, MONTH and YEAR).

package com.seasonalpatterns.comm;

import java.util.Arrays;
import java.util.Calendar;

import com.seasonalpatterns.downloader.DataImporter;
import com.seasonalpatterns.downloader.QuandlAPI;
import com.seasonalpatterns.exchange.Exchange;
import com.seasonalpatterns.helper.CommMonth;
import com.seasonalpatterns.helper.NoDataProviderFoundException;

public abstract class Commodity implements Comparable<Commodity> {

	private Integer year = null;
	private CommMonth month = null;
	private String name = null;
	private Long updateTime = null;
	private Long lastUpdateTime = 0L;
	private Long nextUpdateTime = 0L;
	private String description = null;
	private String category = null;
	private CommMonth[] tradingMonths = null;
	private Double tickSize = null;
	private Double tickPrice = null;
	private Integer FND = null;
	private DataImporter dataImporter = null;
	private Exchange exchange = null;

	public int getYear() {
		return this.year.intValue();
	}

	public CommMonth getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}
	
	public long getUpdateTime() {
		return this.updateTime.longValue();
	}
	
	public long getLastUpdateTime() {
		
		return this.lastUpdateTime.longValue();
	}

	public Long getNextUpdateTime() {
		return nextUpdateTime;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public CommMonth[] getTradingMonths() {
		return tradingMonths;
	}

	public double getTickSize() {
		return tickSize.doubleValue();
	}

	public double getTickPrice() {
		return tickPrice.doubleValue();
	}

	public int getFND() {
		return FND.intValue();
	}

	public DataImporter getDataImporter() {
		return dataImporter;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public long getLastPossibleUpdate() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, this.getYear());
		cal.set(Calendar.MONTH, this.getMonth().toInt());
		cal.set(Calendar.DATE, this.getFND());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		long result = cal.getTimeInMillis();
		return result;
	}

	public void setMonth(CommMonth month) {
		this.month = this.month == null ? month : this.month;
	}

	public void setYear(int year) {
		this.year = this.year == null ? year : this.year;
	}
	
	public void setName(String name) {
		this.name = this.name == null ? name : this.name;
	}

	// All setters (except setUpdateTime) can be initialized only once.

	public void setDescription(String description) {
		this.description = this.description == null ? description
				: this.description;
	}

	public void setCategory(String category) {
		this.category = this.category == null ? category : this.category;
	}
	
	public void setTradingMonths(CommMonth[] tradingMonths) {
		this.tradingMonths = this.tradingMonths == null ? tradingMonths
				: this.tradingMonths;
	}

	public void setTickSize(double tickSize) {
		this.tickSize = this.tickSize == null ? tickSize : this.tickSize;
	}

	public void setTickPrice(double tickPrice) {
		this.tickPrice = this.tickPrice == null ? tickPrice : this.tickPrice;
	}

	public void setFND(int FND) {
		this.FND = this.FND == null ? FND : this.FND;
	}

	public void setDataImporter(Commodity comm) {
		this.dataImporter = new DataImporter();
	}

	public void setExchange(Exchange ex) {
		this.exchange = this.exchange == null ? ex : this.exchange;
	}

	public void setUpdateTime(long millis) {
		
		this.updateTime = millis;
	}

	public long setLastUpdateTime() {

		Calendar now = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(this.getUpdateTime());
		cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
		cal.roll(Calendar.DATE, true);
		long result = cal.getTimeInMillis();
		
		this.lastUpdateTime = result;
		return result;
	}

	public void setNextUpdateTime() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.lastUpdateTime);
		cal.roll(Calendar.DATE, true);
		this.nextUpdateTime = cal.getTimeInMillis();
	}

	@Override
	public int compareTo(Commodity comm) {

		// if commodities are equal, then return 0;
		if (this.equals(comm)) {
			return 0;

			// ticker comparison
		} else if (this.getExchange().getMarketName().compareTo(comm.getExchange().getMarketName()) > 0) {
			return 1;
		} else if (this.getExchange().getMarketName().compareTo(comm.getExchange().getMarketName()) < 0) {
			return -1;

			// year comparison
		} else if (this.getYear() > comm.getYear()) {
			return 1;
		} else if (this.getYear() < comm.getYear()) {
			return -1;

			// month comparison
		} else if (this.getMonth().compareTo(comm.getMonth()) > 0) {
			return 1;
		} else
			return -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((exchange == null) ? 0 : exchange.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + Arrays.hashCode(tradingMonths);
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commodity other = (Commodity) obj;
		if (exchange == null) {
			if (other.exchange != null)
				return false;
		} else if (!exchange.equals(other.exchange))
			return false;
		if (month != other.month)
			return false;
		if (!Arrays.equals(tradingMonths, other.tradingMonths))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	public boolean updateCommodity() {

		boolean updated = false;

		dataImporter.setDataProvider(new QuandlAPI()); // data provider must be set in order to manage downloading
		
		int updates = 0;
		try {
			updates = dataImporter.importToDB(dataImporter.getData(this), this);
		} catch (NoDataProviderFoundException e) {
			e.printStackTrace();
		}
		if (updates > 0) {
			updated = true;
		}
		return updated;
	}
	
	public String toString() {
		
		String result = this.getClass().getSimpleName() + this.getYear() + this.getMonth() + "_" + this.getExchange();
		return result;
	}
}
