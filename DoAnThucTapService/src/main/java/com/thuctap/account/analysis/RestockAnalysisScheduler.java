package com.thuctap.account.analysis;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thuctap.stocking.dto.StockingAnalystSuggestionAggregator;

import ch.qos.logback.core.util.Duration;

@Component
@EnableScheduling
public class RestockAnalysisScheduler {
	 private final String WEEK_KEY = "restock:suggestions:weekly";
	
	 @Autowired
	 private AnalysisService analysisService;

	 @Autowired
	 private RedisTemplate<String, Object> redisTemplate;
	 
//	 @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
//	    public void onStartup() {
//	        runWeekAnalysis();
//	    }
	 
	 @Scheduled(fixedDelay = 604800000, initialDelay = 5000) 
	    public void runWeekAnalysis() {
	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime oneWeekAgo = now.minusDays(7);

	        var result = analysisService.suggestItemsToRestock(oneWeekAgo, now, AnalysisPeriodType.WEEK);
	        redisTemplate.opsForValue().set(WEEK_KEY, result, java.time.Duration.ofDays(7));

	        System.out.println("✅ Weekly restock analysis stored.");
	    }
}
