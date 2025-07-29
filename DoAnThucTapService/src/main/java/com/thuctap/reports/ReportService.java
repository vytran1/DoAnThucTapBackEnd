package com.thuctap.reports;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.invoice.InvoiceRepository;
import com.thuctap.reports.dto.ReportAggregator;
import com.thuctap.reports.dto.ReportItemDTO;
import com.thuctap.reports.dto.ReportRequest;

@Service
public class ReportService implements AdminRevenueReportService,DirectorRevenueReportService {
	
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private ReportTimeRangeStrategyResolver strategyResolver;
	

	@Override
	public ReportAggregator getRevenueReportForDirector(ReportRequest request, Integer inventoryId) {
		 ReportTimeRangeStrategy strategy = strategyResolver.resolve(request.getType());
		 LocalDateTime start = strategy.getStartDate(request);
	     LocalDateTime end = strategy.getEndDate(request);
	     
	     
	     List<ReportItemDTO> items = invoiceRepository.getRevenueReportByDate(start, end, inventoryId);
	     BigDecimal totalValues = calculateTotalValues(items);
	     
		
		
	     return new ReportAggregator(totalValues, items);
	}

	@Override
	public ReportAggregator getRevenueReportForAdmin(ReportRequest request) {
		ReportTimeRangeStrategy strategy = strategyResolver.resolve(request.getType());
		Integer inventoryId = request.getInventoryId();
		LocalDateTime start = strategy.getStartDate(request);
	    LocalDateTime end = strategy.getEndDate(request);
		
	    List<ReportItemDTO> items = invoiceRepository.getRevenueReportByDate(start, end, inventoryId);
	    BigDecimal totalValues = calculateTotalValues(items);
		
		
		return new ReportAggregator(totalValues, items);
	}
	
	
	
	private BigDecimal calculateTotalValues(List<ReportItemDTO> items) {
		
		BigDecimal result = BigDecimal.ZERO;
		
		
		for(ReportItemDTO item : items) {
			result =result.add(item.getValue());
		}
		
		return result;
	}
	
	
	
	
}
