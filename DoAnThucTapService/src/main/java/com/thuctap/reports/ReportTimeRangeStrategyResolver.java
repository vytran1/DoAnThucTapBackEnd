package com.thuctap.reports;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ReportTimeRangeStrategyResolver {

	private final List<ReportTimeRangeStrategy> strategies;
	
	public ReportTimeRangeStrategyResolver(List<ReportTimeRangeStrategy> strategies) {
        this.strategies = strategies;
    }
	
	public ReportTimeRangeStrategy resolve(ReportType type) {
        return strategies.stream()
                .filter(s -> s.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported report type"));
    }
	
}
