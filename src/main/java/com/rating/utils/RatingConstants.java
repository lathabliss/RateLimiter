/**
 * 
 */
package com.rating.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Constants 
 */
public class RatingConstants {
	
	

	public static final int RATE_LIMIT_PER_HOUR = 10;
	public static final String DEFAULT_USER = "DEFAULT";
	public static final Object COUNT_CACHE_LOCK = new Object();

	
	/*
	 * using cache to store the count of hits at 0'th second of the hour
	 */
	public static Map<String, TreeMap<Long, Integer>> USERID_HOUR_TIMESTAMP_COUNT = new HashMap<>();

}
