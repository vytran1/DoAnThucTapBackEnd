package com.thuctap.exporting_form;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.exporting_form.ExportingFormStatus;
import com.thuctap.common.exporting_form.ExportingFormStatusId;

public interface ExportingFormStatusRepository extends JpaRepository<ExportingFormStatus,ExportingFormStatusId> {
	
	
	@Query("""
		    SELECT s
		    FROM ExportingFormStatus s
		    JOIN FETCH s.exportingForm f
		    JOIN FETCH s.status st
		    LEFT JOIN FETCH s.transporter t
		    LEFT JOIN FETCH s.employee e
		    WHERE f.id = :formId
		    ORDER BY s.createdAt ASC
		""")
	public List<ExportingFormStatus> getAllStatusBelongToOneExportingForm(Integer formId);
	
	@Query("""
		    SELECT st.name
		    FROM ExportingFormStatus s
		    JOIN s.exportingForm f
		    JOIN s.status st
		    WHERE f.id = :formId
		    ORDER BY s.createdAt ASC
		""")
	List<String> getStatusNamesByFormId(Integer formId);
	
	
	@Query("""
		    SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
		    FROM ExportingFormStatus s
		    JOIN s.exportingForm f
		    JOIN s.status st
		    WHERE f.id = :formId
		      AND st.name IN ('REVIEW_DECIDED_TRANSPORT', 'REVIEW_REJECT')
		""")
	boolean hasReviewDecisionOrReject(Integer formId);
	
	@Query("""
		    SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
		    FROM ExportingFormStatus s
		    JOIN s.exportingForm f
		    JOIN s.status st
		    WHERE f.id = :formId
		      AND st.name = 'REVIEW_DECIDED_TRANSPORT'
		""")
	boolean hasReviewDecidedTransport(Integer formId);
	
	
	
}
