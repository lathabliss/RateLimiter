/**
 * 
 */
package com.rating.utils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Utils for date and time
 */
public class DateTimeUtils {

	public static final long MILLIS_IN_DAY = TimeUnit.DAYS.toMillis(1);// 86400000
	public static final long MILLIS_IN_HOUR = TimeUnit.HOURS.toMillis(1);// 3600000
	public static final long MILLIS_IN_MIUTE = TimeUnit.MINUTES.toMillis(1); // 60000
	public static final long MILLIS_IN_SECOND = TimeUnit.SECONDS.toMillis(1); // 1000

	

	/**
	 * Return Hour start time stamp for the given millis
	 * 
	 * @param referenceTs
	 */
	public static long getHourStartTimeStamp(long referenceTs) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(referenceTs);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	
	/**
	 * Return Next Hour start time stamp for the given millis
	 * 
	 * @param referenceTs
	 */
	public static long getNextHourStartTimestamp(long referenceTs) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(referenceTs);
		calendar.set(Calendar.MINUTE, 60);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	

}
