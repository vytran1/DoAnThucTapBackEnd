package com.thuctap.reports;

import java.time.LocalDateTime;

import com.thuctap.reports.dto.ReportRequest;

public interface ReportTimeRangeStrategy {
	boolean supports(ReportType type);
	LocalDateTime getStartDate(ReportRequest request);
	LocalDateTime getEndDate(ReportRequest request);
}
