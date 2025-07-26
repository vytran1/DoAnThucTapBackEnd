package com.thuctap.account.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.stocking.dto.StockingAnalystSuggestionAggregator;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
	
	@Autowired
	private AnalysisService analysisService;
	
	@GetMapping("/weekly")
	public ResponseEntity<?> getStockingAnalysisWeekly(){
		
		StockingAnalystSuggestionAggregator stockingAggregator  = analysisService.getStockingAnalysisWeekly();
		return ResponseEntity.ok(stockingAggregator);
		
		
		
	}
	
	
}
