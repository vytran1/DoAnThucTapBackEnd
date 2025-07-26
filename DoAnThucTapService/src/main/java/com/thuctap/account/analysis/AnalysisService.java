package com.thuctap.account.analysis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.thuctap.invoice.InvoiceDetailRepository;
import com.thuctap.invoice.dto.ProductSaleSummaryDTO;
import com.thuctap.stocking.StockingRepository;
import com.thuctap.stocking.dto.StockingAnalystSuggestion;
import com.thuctap.stocking.dto.StockingAnalystSuggestionAggregator;
import com.thuctap.stocking.dto.StockingSummaryDTO;

import ch.qos.logback.core.util.Duration;

@Service
public class AnalysisService {
	
	private final String WEEK_KEY = "restock:suggestions:weekly";
	
	@Value("${stocking.analysis.buffer-threshold}")
	private int bufferThreshold;
	
	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;
	
	@Autowired
	private StockingRepository stockingRepository;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
	public StockingAnalystSuggestionAggregator suggestItemsToRestock(LocalDateTime startDate, LocalDateTime endDate, AnalysisPeriodType type){
		
		List<StockingSummaryDTO> stockingsSummary = stockingRepository.getTotalStockingGroupBySku();
		
		List<ProductSaleSummaryDTO>  sales = invoiceDetailRepository.getSalesSummaryGroupBySku(startDate, endDate);
		
		Map<String, ProductSaleSummaryDTO> saleMap = sales.stream()
			    .collect(Collectors.toMap(ProductSaleSummaryDTO::getSku, s -> s));
		
		List<StockingAnalystSuggestion> suggestions = analysis(stockingsSummary, saleMap, type, startDate, endDate);
		
		StockingAnalystSuggestionAggregator result = new StockingAnalystSuggestionAggregator();
		result.setStartDate(startDate);
		result.setEndDate(endDate);
		result.setSuggestions(suggestions);
		
		return result;
	}
	
	public StockingAnalystSuggestionAggregator getStockingAnalysisWeekly() {
		ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
		Object cached = valueOps.get(WEEK_KEY);

		if (cached == null) {
			throw new RuntimeException("Weekly restock suggestion data not found in Redis.");
		}

		if (cached instanceof StockingAnalystSuggestionAggregator suggestion) {
			return suggestion;
		} else {
			throw new RuntimeException("Cached object is not of expected type.");
		}
	}
	
	
	
	private List<StockingAnalystSuggestion> analysis(List<StockingSummaryDTO> stockingsSummary,Map<String, 
				ProductSaleSummaryDTO> saleMap,
				AnalysisPeriodType type,
				LocalDateTime startDate,
				LocalDateTime endDate
			){
				List<StockingAnalystSuggestion> suggestions = new ArrayList<>();

				for (StockingSummaryDTO stocks : stockingsSummary) {

					String sku = stocks.getSku();
					String productName = stocks.getName();
					Long currentQuantity = stocks.getTotalQuantity();
					ProductSaleSummaryDTO sale = saleMap.get(sku);
					Long totalSaledQuantity = sale.getTotalQuantity();

					StockingAnalystSuggestion suggestion = new StockingAnalystSuggestion();
					suggestion.setName(productName);
					suggestion.setSku(sku);
					suggestion.setTotalCurrentStocking(currentQuantity);
					suggestion.setTotalSaledQuantity(totalSaledQuantity);

					Long predictedTotalQuantityForNextPeriod = predictNextPeriodSales(totalSaledQuantity, startDate,
							endDate, type, suggestion);
					boolean needToImport = (currentQuantity < predictedTotalQuantityForNextPeriod - 10);

					suggestion.setPredictQuantityForNextPeriod(predictedTotalQuantityForNextPeriod);
					suggestion.setNeedToBeImported(needToImport);

					suggestions.add(suggestion);
				}
				
				return suggestions;
	}
	
	
	private long predictNextPeriodSales(long totalQuantity, 
			 LocalDateTime startDate, 
			 LocalDateTime endDate, 
			 AnalysisPeriodType type,
			 StockingAnalystSuggestion suggestion
			 ) {
	        long daysBetween = java.time.Duration.between(startDate, endDate).toDays();
	        if (daysBetween == 0) return 0; // tránh chia 0

	        double avgPerDay = (double) totalQuantity / daysBetween;
	        
	        suggestion.setAverageSaledQuantity(avgPerDay);

	        return switch (type) {
	            case WEEK -> Math.round(avgPerDay * 7);
	            case MONTH -> Math.round(avgPerDay * 30);
	            case YEAR -> Math.round(avgPerDay * 365);
	        };
	}
	
	
	
	
}
