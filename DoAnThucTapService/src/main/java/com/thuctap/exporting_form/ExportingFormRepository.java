package com.thuctap.exporting_form;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.exporting_form.ExportingForm;
import com.thuctap.exporting_form.dto.ExportingFormOverviewDTO;
import com.thuctap.exporting_form.dto.ExportingFormPageDTO;

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
	
	
	@Query("""
			SELECT new com.thuctap.exporting_form.dto.ExportingFormPageDTO(
					ef.id,
					ef.exportingFormCode,
					mfi.inventoryCode,
					mti.inventoryCode,
					SUM(efd.quantity),
					st.name
				)
			FROM ExportingForm ef
			JOIN ef.moveFromInventory mfi
			JOIN ef.moveToInventory mti
			JOIN ExportingFormDetail efd 
				ON efd.exportingForm = ef 
			LEFT JOIN ExportingFormStatus stRel
			    ON stRel.exportingForm = ef
			    AND stRel.createdAt = (
			         SELECT MAX(st2.createdAt)
			         FROM ExportingFormStatus st2
			         WHERE st2.exportingForm = ef
			    )
			LEFT JOIN ExportingStatus st
			    ON stRel.status = st 
			WHERE (
			         (:startDate IS NULL OR :endDate IS NULL) 
			         OR (ef.createdAt BETWEEN :startDate AND :endDate)
			      )
			AND   (mfi.id = :currentInventoryId OR mti.id = :currentInventoryId)
			GROUP BY ef.id, ef.exportingFormCode, mfi.inventoryCode, mti.inventoryCode, st.name	  
			""")
	public Page<ExportingFormPageDTO> search(
			@Param("startDate") LocalDateTime startDate,
		    @Param("endDate") LocalDateTime   endDate,
		    @Param("currentInventoryId") Integer currentInventoryId,
		    Pageable pageable
			);
	
	
	
}
