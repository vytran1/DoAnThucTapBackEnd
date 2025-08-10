package com.thuctap.exporting_form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.exporting_form.ExportingForm;
import com.thuctap.exporting_form.dto.ExportingFormOverviewDTO;

public interface ExportingFormRepository extends JpaRepository<ExportingForm,Integer> {
	
	
	
	@Query("""
			
			SELECT new com.thuctap.exporting_form.dto.ExportingFormOverviewDTO(
				ef.exportingFormCode,
				moveFromI.inventoryCode,
				moveToI.inventoryCode,
				CONCAT(createEmp.firstName,' ',createEmp.lastName),
				CONCAT(receiveEmp.firstName,' ',receiveEmp.lastName),
				t.transporterCode
			)
			FROM ExportingForm ef
			JOIN ef.moveFromInventory moveFromI
			JOIN ef.moveToInventory moveToI
			JOIN ef.createEmployee createEmp
			LEFT JOIN ef.receiveEmployee receiveEmp
			JOIN ef.transporter t
			WHERE ef.id = ?1
			
			""")
	public ExportingFormOverviewDTO getExportingFormOverview(Integer formId);
	
}
