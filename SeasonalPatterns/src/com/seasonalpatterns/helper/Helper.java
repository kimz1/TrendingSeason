package com.seasonalpatterns.helper;

import java.util.Calendar;

import com.seasonalpatterns.comm.Commodity;

public class Helper {
	
	public static long getCurrentUpdateTime(Commodity comm) {

		Calendar now = Calendar.getInstance();
		Calendar past = Calendar.getInstance();

		long updateTime = comm.getUpdateTime();

		past.setTimeInMillis(updateTime);
		past.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
				now.get(Calendar.DATE));

		return past.getTimeInMillis();
	}
}
