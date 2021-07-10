/**
 * 
 */
package com.rating.services;

import static com.rating.utils.RatingConstants.COUNT_CACHE_LOCK;
import static com.rating.utils.RatingConstants.RATE_LIMIT_PER_HOUR;
import static com.rating.utils.RatingConstants.USERID_HOUR_TIMESTAMP_COUNT;

import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.rating.utils.DateTimeUtils;

@Component
public class RateLimiter {


	/**
	 * Service to check the rate limit of the given userId
	 * 
	 * @param userId - String
	 * @return [error message or info message]
	 */
	public String checkRateLimit(String userId) {
		
		
		long currTs = System.currentTimeMillis();
		long hourStartTs = DateTimeUtils.getHourStartTimeStamp(currTs);
		long nextHourStarts = DateTimeUtils.getNextHourStartTimestamp(hourStartTs);
		long timeIntervalLeft = (nextHourStarts - currTs) / DateTimeUtils.MILLIS_IN_MIUTE;

		Integer count = 0;
		synchronized (COUNT_CACHE_LOCK) {
			
			USERID_HOUR_TIMESTAMP_COUNT.computeIfAbsent(userId, k -> new TreeMap<>());
			USERID_HOUR_TIMESTAMP_COUNT.get(userId).putIfAbsent(hourStartTs, 0);

			count = USERID_HOUR_TIMESTAMP_COUNT.get(userId).get(hourStartTs);
			if (count >= RATE_LIMIT_PER_HOUR) {
				return callErrorMessage(timeIntervalLeft);
			}

			count++;
			USERID_HOUR_TIMESTAMP_COUNT.get(userId).put(hourStartTs, count);
		}

		
		int residualCount = (RATE_LIMIT_PER_HOUR - count);		
		return (residualCount > 0) ? callResponseMessage(residualCount, timeIntervalLeft)
				: callWaitMessage(timeIntervalLeft);		
	}
	
	

	/**
	 * Error message reg number of requests left out
	 * 
	 * @param timeIntervalLeft
	 */
	private String callErrorMessage(long timeIntervalLeft) {
		return "Have already reached maximum limit, please try after " + (++timeIntervalLeft) + " minutes.";
	}
	
	
	
	/**
	 * Response message reg number of requests left out
	 * 
	 * @param residualCount
	 * @param timeIntervalLeft
	 */
	private String callResponseMessage(int residualCount, long timeIntervalLeft) {
		return "Can make " + residualCount + " more request in next " + ( ++timeIntervalLeft) + " minutes.";
	}

	
	
	/**
	 * Number of minutes left over<br>
	 * to make the next request
	 * 
	 * @param residualCount
	 * @param timeIntervalLeft
	 */
	private String callWaitMessage(long timeIntervalLeft) {
		return "Wait for " + timeIntervalLeft + " minutes, to make the next request.";
	}

}
