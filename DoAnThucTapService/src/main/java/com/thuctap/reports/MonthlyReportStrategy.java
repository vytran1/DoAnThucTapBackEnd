package com.thuctap.reports;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thuctap.reports.dto.ReportRequest;

@Component
public class MonthlyReportStrategy implements ReportTimeRangeStrategy {
	
	 @Override
	    public boolean supports(ReportType type) {
	        return type == ReportType.MONTH;
	    }

	    @Override
	    public LocalDateTime getStartDate(ReportRequest request) {
	        LocalDateTime now = LocalDateTime.now();
	        return now.withDayOfMonth(1); // First day of current month
	    }

	    @Override
	    public LocalDateTime getEndDate(ReportRequest request) {
	        LocalDateTime now = LocalDateTime.now();
	        int lastDay = now.toLocalDate().lengthOfMonth();
	        return now.withDayOfMonth(lastDay).withHour(23).withMinute(59).withSecond(59); // Last day of current month
	    }
	
}
