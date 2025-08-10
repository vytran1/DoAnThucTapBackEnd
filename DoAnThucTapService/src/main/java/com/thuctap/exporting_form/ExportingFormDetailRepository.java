package com.thuctap.exporting_form;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.exporting_form.ExportingFormDetail;
import com.thuctap.common.exporting_form.ExportingFormDetailId;
import com.thuctap.exporting_form.dto.ExportingFormDetailOverviewDTO;

public interface ExportingFormDetailRepository extends JpaRepository<ExportingFormDetail,ExportingFormDetailId> {
	
	
	@Query("""
			   SELECT new com.thuctap.exporting_form.dto.ExportingFormDetailOverviewDTO(
			   	  efd.productVariant.sku,
			   	  efd.quantity
			   )
			   FROM ExportingFormDetail efd
			   WHERE efd.exportingForm.id = ?1
			""")
	public List<ExportingFormDetailOverviewDTO> getAllDetailsById(Integer formId);

	@Query("""
			SELECT SUM(efd.quantity)
			FROM ExportingFormDetail efd
			WHERE efd.exportingForm.id = ?1
			""")
	Long getTotalQuantityByFormId(Integer formId);
	
}
