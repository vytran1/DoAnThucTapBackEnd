package com.thuctap.reports;

import com.thuctap.reports.dto.ReportAggregator;
import com.thuctap.reports.dto.ReportRequest;

public interface DirectorRevenueReportService {


	ReportAggregator getRevenueReportForDirector(ReportRequest request, Integer inventoryId);

}
