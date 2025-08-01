package com.thuctap.importing_form;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.importing_form.ImportingForm;
import com.thuctap.importing_form.dto.ImportingFormOverviewDTO;
import com.thuctap.importing_form.dto.ImportingFormPageDTO;
import com.thuctap.reports.dto.ReportItemDTO;

public interface ImportingFormRepository extends JpaRepository<ImportingForm,Integer> {
	
	
	
	@Query("""
			SELECT new com.thuctap.importing_form.dto.ImportingFormPageDTO(
					if.id,
					if.importingFormCode,
					CONCAT(e.firstName,' ',e.lastName),
					if.createdAt,
					SUM(ifd.quantity),
					SUM(ifd.quantity * ifd.costPrice)
				)
			FROM ImportingForm if
			LEFT JOIN if.employee e
			LEFT JOIN ImportingFormDetail ifd ON ifd.importingForm.id = if.id
			WHERE (?1 IS NULL OR if.createdAt >= ?1) 
				AND (?2 IS NULL OR if.createdAt <= ?2)
			GROUP BY if.id, if.importingFormCode, if.createdAt, e.firstName, e.lastName  	
			""")
	public Page<ImportingFormPageDTO> search(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
	
	
	@Query("""
			SELECT new com.thuctap.importing_form.dto.ImportingFormOverviewDTO(
					if.importingFormCode,
					CONCAT(e.firstName,' ',e.lastName),
					if.createdAt
				)
			FROM ImportingForm if
			LEFT JOIN if.employee e
			WHERE if.id = ?1
			""")
	public ImportingFormOverviewDTO findImportingFormOverviewById(Integer id);
	
	
	@Query("""
			SELECT o.id 
			FROM ImportingForm if
			JOIN if.order o 
			WHERE if.id = ?1 
			""")
	public Integer findOrderIdByImportingFormId(Integer formId);
	
	@Query("""
		    SELECT new com.thuctap.reports.dto.ReportItemDTO(
		        DATE(f.createdAt),
		        SUM(d.quantity * d.costPrice)
		    )
		    FROM ImportingForm f
		    JOIN f.inventory inv
		    JOIN ImportingFormDetail d ON d.importingForm = f
		    WHERE (:inventoryId IS NULL OR f.inventory.id = :inventoryId)
		      AND f.createdAt BETWEEN :startDate AND :endDate
		    GROUP BY DATE(f.createdAt)
		    ORDER BY DATE(f.createdAt)
		""")
		List<ReportItemDTO> getImportingReportByDate(
		    @Param("startDate") LocalDateTime startDate,
		    @Param("endDate") LocalDateTime endDate,
		    @Param("inventoryId") Integer inventoryId
		);
	
	
}
