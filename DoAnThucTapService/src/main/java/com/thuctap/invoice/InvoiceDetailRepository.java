package com.thuctap.invoice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.invoice.InvoiceDetail;
import com.thuctap.common.invoice.InvoiceDetailId;
import com.thuctap.invoice.dto.ProductSaleSummaryDTO;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail,InvoiceDetailId> {
	
	@Query("""
			    SELECT new com.thuctap.invoice.dto.ProductSaleSummaryDTO(
			        d.id.sku, SUM(d.quantity)
			    )
			    FROM InvoiceDetail d
			    WHERE d.invoice.createdAt BETWEEN :start AND :end
			    GROUP BY d.id.sku
			""")
	List<ProductSaleSummaryDTO> getSalesSummaryGroupBySku(@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
	
}
