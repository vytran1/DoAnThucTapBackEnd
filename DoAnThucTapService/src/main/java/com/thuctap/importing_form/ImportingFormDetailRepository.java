package com.thuctap.importing_form;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.importing_form.ImportingFormDetail;
import com.thuctap.common.importing_form.ImportingFormDetailId;
import com.thuctap.importing_form.dto.ImportingFormDetailOverviewDTO;

public interface ImportingFormDetailRepository extends JpaRepository<ImportingFormDetail,ImportingFormDetailId> {

	@Query("""
			    SELECT d.costPrice
			    FROM ImportingFormDetail d
			    WHERE d.productVariant.sku = :sku
			    ORDER BY d.importingForm.createdAt DESC
			""")
	List<BigDecimal> findRecentCostPricesBySku(@Param("sku") String sku, Pageable pageable);
	
	
	@Query("""
				SELECT new com.thuctap.importing_form.dto.ImportingFormDetailOverviewDTO(
						d.productVariant.sku,
			     		d.quantity,
			     		d.costPrice,
			     		d.costPrice * d.quantity
					)
				FROM ImportingFormDetail d
				WHERE d.importingForm.id = ?1	
			""")
	List<ImportingFormDetailOverviewDTO> findDetailsByImportingFormId(Integer id);
	
	
	
}
