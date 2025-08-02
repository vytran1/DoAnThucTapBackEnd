package com.thuctap.reports;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.importing_form.ImportingFormRepository;
import com.thuctap.invoice.InvoiceRepository;
import com.thuctap.reports.dto.ReportAggregator;
import com.thuctap.reports.dto.ReportItemDTO;
import com.thuctap.reports.dto.ReportRequest;
import com.thuctap.stocking.StockingService;
import com.thuctap.stocking.dto.StockingReportAggregator;

@Service
public class ReportService implements AdminRevenueReportService,DirectorRevenueReportService {
	
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private ReportTimeRangeStrategyResolver strategyResolver;
	
	@Autowired
	private ImportingFormRepository importingFormRepository;
	
	
	 @Autowired
	 private StockingService stockService;
	
	

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
	
	
	
	
	
	
	
	
	
	
	@Override
	public ReportAggregator getImportingFormForDirector(ReportRequest request) {
		 ReportTimeRangeStrategy strategy = strategyResolver.resolve(request.getType());
		 LocalDateTime start = strategy.getStartDate(request);
	     LocalDateTime end = strategy.getEndDate(request);
	     
	     System.out.println("Inventory In Start Date" + start.toString());
	     System.out.println("Inventory In End Date" + end.toString());
	     System.out.println("Inventory In Importing Form" + request.getInventoryId());
	     
	     List<ReportItemDTO> items = importingFormRepository.getImportingReportByDate(start, end,request.getInventoryId());
	     BigDecimal totalValues = calculateTotalValues(items);
	     
	     
	     return new ReportAggregator(totalValues, items);
	}
	
	
	

	@Override
	public ReportAggregator getImportingFormForAdmin(ReportRequest request) {
		 ReportTimeRangeStrategy strategy = strategyResolver.resolve(request.getType());
		 LocalDateTime start = strategy.getStartDate(request);
	     LocalDateTime end = strategy.getEndDate(request);
	     
	     List<ReportItemDTO> items = importingFormRepository.getImportingReportByDate(start, end,request.getInventoryId());
	     BigDecimal totalValues = calculateTotalValues(items);
	     
	     
	     return new ReportAggregator(totalValues, items);
	}

	@Override
	public StockingReportAggregator getStockingReportOfInventoryForDirector(Integer inventoryId, int pageNum,
			int pageSize) {
		
		
		return this.getStockingReportOfInventory(inventoryId, pageNum, pageSize);
	}

	@Override
	public StockingReportAggregator getStockingReportOfInventoryForAdmin(Integer inventoryId, int pageNum,
			int pageSize) {
		
		
		
		return this.getStockingReportOfInventory(inventoryId, pageNum, pageSize);
	}

	private StockingReportAggregator getStockingReportOfInventory(Integer inventoryId,
			int pageNum,
			int pageSize
			) {
		
		
		return stockService.getStockingReportOfInventory(inventoryId,pageNum,pageSize);
	}
	
	
	
	private BigDecimal calculateTotalValues(List<ReportItemDTO> items) {
		
		BigDecimal result = BigDecimal.ZERO;
		
		
		for(ReportItemDTO item : items) {
			result =result.add(item.getValue());
		}
		
		return result;
	}
	
	
	
	
}
