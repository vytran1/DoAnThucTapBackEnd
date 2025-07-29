package com.thuctap.invoice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.invoice.Invoice;
import com.thuctap.reports.dto.ReportItemDTO;
import com.thuctap.stocking.dto.StockingSummaryDTO;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
	
	
	
	
	
	@Query("""
			SELECT new com.thuctap.reports.dto.ReportItemDTO(
					DATE(i.createdAt),
					SUM(d.unitPrice * d.quantity) + SUM(i.tax)
				)
			FROM Invoice i
			JOIN InvoiceDetail d ON d.invoice = i
			WHERE (:inventoryId IS NULL OR i.inventory.id = :inventoryId)
				AND i.createdAt BETWEEN :startDate AND :endDate
			GROUP BY DATE(i.createdAt)
			ORDER BY DATE(i.createdAt)
			""")
	public List<ReportItemDTO> getRevenueReportByDate(LocalDateTime startDate, LocalDateTime endDate, Integer inventoryId);
	
	
}
