package com.seasonalpatterns;

import java.util.Date;

import com.seasonalpatterns.comm.Commodity;
import com.seasonalpatterns.downloader.QuandlAPI;
import com.seasonalpatterns.helper.CommFactory;
import com.seasonalpatterns.helper.CommMonth;

public class MainTester {

	public static void main(String[] args) {
		
		Commodity comm = CommFactory.createCommodity("com.seasonalpatterns.comm.Corn", 2015, CommMonth.H, "CBOT");
		comm.getDataImporter().setDataProvider(new QuandlAPI());
		QuandlAPI q = (QuandlAPI)comm.getDataImporter().getDataProvider();
		System.out.println(q.buildURL(comm, new Date(comm.getLastPossibleUpdate())));
		System.out.println("Printing in MainTester: " + comm);
	}
}
