/**
 * 
 */
package com.rating.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rating.services.LimiterCacheService;
import com.rating.services.RateLimiter;
import com.rating.utils.RatingConstants;

@RestController
public class RatingController {
	
	@Autowired
	private RateLimiter ratelimiter;

	@Autowired
	private LimiterCacheService cacheService;

	
	
	@RequestMapping("/getRateLimit")
	public String getRateLimiter() {
		return ratelimiter.checkRateLimit(RatingConstants.DEFAULT_USER);
	}

	
	@RequestMapping("/getRateLimitWithUserId")
	public String getRateLimitForUser(@RequestParam String id) {
		return ratelimiter.checkRateLimit(id);
	}

	
	
	//@Scheduled(fixedRate=5 * 60*1000)
	@Scheduled(cron = "0 0 */1 * * *")
	public void run() {
		cacheService.clearHistoricalEntries();
	}

}
