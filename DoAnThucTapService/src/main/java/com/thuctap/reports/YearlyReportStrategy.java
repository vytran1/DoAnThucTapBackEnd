package com.thuctap.reports;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thuctap.reports.dto.ReportRequest;

@Component
public class YearlyReportStrategy implements ReportTimeRangeStrategy {

	@Override
	public boolean supports(ReportType type) {
		return type == ReportType.YEAR;
	}

	@Override
	public LocalDateTime getStartDate(ReportRequest request) {
		return LocalDateTime.now().minusYears(1);
	}

	@Override
	public LocalDateTime getEndDate(ReportRequest request) {
		return LocalDateTime.now();
	}

	
	
}
