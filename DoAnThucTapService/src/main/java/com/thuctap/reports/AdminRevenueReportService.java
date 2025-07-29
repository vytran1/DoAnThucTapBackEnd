package com.thuctap.reports;

import com.thuctap.reports.dto.ReportAggregator;
import com.thuctap.reports.dto.ReportRequest;

public interface AdminRevenueReportService {
    ReportAggregator getRevenueReportForAdmin(ReportRequest request);

}
