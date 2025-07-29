package com.thuctap.reports;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thuctap.reports.dto.ReportRequest;

@Component
public class WeeklyReportStrategy implements ReportTimeRangeStrategy {

	@Override
	public boolean supports(ReportType type) {
		return type == ReportType.WEEK;
	}

	@Override
	public LocalDateTime getStartDate(ReportRequest request) {
		return LocalDateTime.now().minusDays(7);
	}

	@Override
	public LocalDateTime getEndDate(ReportRequest request) {
		return LocalDateTime.now();
	}

	
	
}
