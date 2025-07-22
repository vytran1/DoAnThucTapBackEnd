package com.thuctap.importing_form;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.importing_form.ImportingFormDetail;
import com.thuctap.common.importing_form.ImportingFormDetailId;

public interface ImportingFormDetailRepository extends JpaRepository<ImportingFormDetail,ImportingFormDetailId> {

	@Query("""
			    SELECT d.costPrice
			    FROM ImportingFormDetail d
			    WHERE d.productVariant.sku = :sku
			    ORDER BY d.importingForm.createdAt DESC
			""")
	List<BigDecimal> findRecentCostPricesBySku(@Param("sku") String sku, Pageable pageable);
	
}
