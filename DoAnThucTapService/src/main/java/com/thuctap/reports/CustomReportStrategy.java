package com.thuctap.reports;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thuctap.reports.dto.ReportRequest;

@Component
public class CustomReportStrategy implements ReportTimeRangeStrategy {

	@Override
	public boolean supports(ReportType type) {
		return type == ReportType.CUSTOM;
	}

	@Override
	public LocalDateTime getStartDate(ReportRequest request) {
		return request.getStartDate().atStartOfDay();
	}

	@Override
	public LocalDateTime getEndDate(ReportRequest request) {
		return request.getEndDate().atTime(23, 59, 59);
	}
	
	
	
	
}
