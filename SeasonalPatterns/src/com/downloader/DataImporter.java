package com.downloader;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.comm.Commodity;
import com.dbmanager.DataDAO;
import com.helper.NoDataProviderFoundException;

public class DataImporter {
	
	/*
	 * For each Commodity object there is a DataImporter object to handle 
	 * importing price from a data source.
	 */

	private DataProvider provider = null;
	private DataDAO manager = null;
	
	public DataImporter() {
		manager = DataDAO.getInstance();
	}
	
	public int importToDB(InputStreamReader isr, Commodity comm) {
		
		int updatedRows = 0;
		
		updatedRows = manager.updateDB(isr, comm);
		return updatedRows;
	}
	
	public void setDataProvider(DataProvider provider) {
		this.provider = provider;
	}
			
	public InputStreamReader getData(Commodity comm) throws NoDataProviderFoundException {
		
		Date lastDate = null;
		try {
			lastDate = getLastCommDate(comm);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(provider == null) throw new NoDataProviderFoundException();
		InputStreamReader isr = provider.getData(comm, lastDate);
		
		return isr;
	}
	
	public DataProvider getDataProvider() {
		return provider;
	}
	
	public Date getLastCommDate(Commodity comm) throws SQLException {
		
		// Checks for a date of the last data.
		return manager.getLastDate(comm);
	}
}
