package com.thuctap.reports;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thuctap.reports.dto.ReportRequest;

@Component
public class HalfYearReportStrategy implements ReportTimeRangeStrategy {

	@Override
	public boolean supports(ReportType type) {
		return type == ReportType.HALF_YEAR;
	}

	@Override
	public LocalDateTime getStartDate(ReportRequest request) {
		return LocalDateTime.now().minusMonths(6);
	}

	@Override
	public LocalDateTime getEndDate(ReportRequest request) {
		 return LocalDateTime.now();
	}
	
	
	
	
}
