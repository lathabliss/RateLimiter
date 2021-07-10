/**
 * 
 */
package com.rating.services;

import static com.rating.utils.RatingConstants.COUNT_CACHE_LOCK;
import static com.rating.utils.RatingConstants.USERID_HOUR_TIMESTAMP_COUNT;

import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.rating.utils.DateTimeUtils;


@Component
public class LimiterCacheService {

	
	
	/**
	 * Periodically run the service at start the each hour
	 * 
	 * Cleans the cache data older than one hour
	 * Ts ->  time Stamp
	 */
	public void clearHistoricalEntries() {

		long currTs = System.currentTimeMillis();
		
		
		synchronized (COUNT_CACHE_LOCK) {
			
			if (USERID_HOUR_TIMESTAMP_COUNT == null || USERID_HOUR_TIMESTAMP_COUNT.isEmpty()) {
				return;
			}

			
			for (String userId : USERID_HOUR_TIMESTAMP_COUNT.keySet()) {
				
				TreeMap<Long, Integer> timeSeriesCountMap = getUpdatedHourCountMap(
						USERID_HOUR_TIMESTAMP_COUNT.get(userId), currTs);
				USERID_HOUR_TIMESTAMP_COUNT.put(userId, timeSeriesCountMap);
			}
			
		}

	}

	
	
	/**
	 * Clean the cache data older than one hour<br>
	 * 
	 * eg - 2 pm is current time then all data before 1 pm will be cleared
	 * 	  - data from 1 PM will be present in the resultant map
	 * 
	 * @param inputMap
	 * @param currTs
	 */
	private TreeMap<Long, Integer> getUpdatedHourCountMap(TreeMap<Long, Integer> inputMap, long currTs) {

		TreeMap<Long, Integer> resultMap = new TreeMap<>();
		if(inputMap == null || inputMap.isEmpty()) {
			return resultMap;
		}
		
		
		Long prevHrLimitTs = currTs - DateTimeUtils.MILLIS_IN_HOUR;
		prevHrLimitTs = inputMap.ceilingKey(prevHrLimitTs);
		
		while (prevHrLimitTs != null) {
			resultMap.put(prevHrLimitTs, inputMap.get(prevHrLimitTs));		
			prevHrLimitTs = inputMap.higherKey(prevHrLimitTs);
		}

		return resultMap;
	}
}
