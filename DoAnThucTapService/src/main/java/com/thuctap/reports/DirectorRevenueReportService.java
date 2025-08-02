package com.thuctap.reports;

import com.thuctap.reports.dto.ReportAggregator;
import com.thuctap.reports.dto.ReportRequest;
import com.thuctap.stocking.dto.StockingReportAggregator;

public interface DirectorRevenueReportService {


	ReportAggregator getRevenueReportForDirector(ReportRequest request, Integer inventoryId);
	 ReportAggregator getImportingFormForDirector(ReportRequest request);
	StockingReportAggregator getStockingReportOfInventoryForDirector(Integer inventoryId,
			int pageNum,
			int pageSize);
}
