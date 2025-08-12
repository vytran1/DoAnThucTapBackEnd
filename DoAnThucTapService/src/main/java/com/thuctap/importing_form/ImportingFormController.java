package com.thuctap.importing_form;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.exceptions.ImportingFormNotFoundException;
import com.thuctap.importing_form.dto.ImportingFormOverviewDTO;
import com.thuctap.importing_form.dto.ImportingFormPageAggregator;
import com.thuctap.utility.QuoteData;

@RestController
@RequestMapping("/api/importing_forms")
public class ImportingFormController {
	
	@Autowired
	private ImportingFormService importingFormService;
	
	
	@GetMapping("/search")
	public ResponseEntity<ImportingFormPageAggregator> search(
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "sortField", defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(value = "startDate", required = false) OffsetDateTime startDate,
			@RequestParam(value = "endDate", required = false) OffsetDateTime endDate
			){
		
		 LocalDateTime start = startDate != null ? startDate.toLocalDateTime() : null;
		 LocalDateTime end = endDate != null ? endDate.toLocalDateTime() : null;
		 
		 ImportingFormPageAggregator result = importingFormService.search(start, end, pageNum, pageSize, sortField, sortDir);
		
		 return ResponseEntity.ok(result);
	}
	
	@GetMapping("/{id}/overview")
	public ResponseEntity<?> getOverview(@PathVariable("id") Integer formId){
		
		ImportingFormOverviewDTO result;
		try {
			result = importingFormService.getOverview(formId);
			return ResponseEntity.ok(result);
		} catch (ImportingFormNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	} 
	
	@GetMapping("/{id}/invoice_information")
	public ResponseEntity<?> getInvoiceDetail(@PathVariable("id") Integer formId){
		
		QuoteData invoiceData = importingFormService.getInvoiceInformation(formId);
		
		return ResponseEntity.ok(invoiceData);
		
		
	}
	
	
}
