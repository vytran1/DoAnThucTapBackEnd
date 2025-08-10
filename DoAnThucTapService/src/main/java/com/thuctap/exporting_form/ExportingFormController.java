package com.thuctap.exporting_form;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.exceptions.ExportingFormNotFoundException;
import com.thuctap.common.exceptions.TransporterNotFoundException;
import com.thuctap.common.exporting_form.QuotePriceData;
import com.thuctap.exporting_form.dto.CreateExportingFormRequest;
import com.thuctap.exporting_form.dto.ExportingFormOverviewDTO;
import com.thuctap.exporting_form.dto.ExportingFormStatusDTO;
import com.thuctap.exporting_form.dto.QuotePriceInformationAggregator;

@RestController
@RequestMapping("/api/exporting_form")
public class ExportingFormController {
		
	private static final Logger log = LoggerFactory.getLogger(ExportingFormController.class); 
	
	
	@Autowired
	private ExportingFormService exportingFormService;
	
	
	
	@PostMapping("")
	public ResponseEntity<?> saveExportingForm(@RequestBody CreateExportingFormRequest request){
		exportingFormService.saveExportingForm(request);
		return ResponseEntity.ok().build();
	}
	
	
	
	@GetMapping("/{formId}/overview")
	public ResponseEntity<?> getDetailsInformationOfExportingForm(@PathVariable("formId") Integer formId){
		try {
			ExportingFormOverviewDTO result = exportingFormService.getExportingFormOverview(formId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (ExportingFormNotFoundException e) {
			log.error("Error",e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/{formId}/status")
	public ResponseEntity<?> getStatusesInformationOfExportingForm(@PathVariable("formId") Integer formId){
		try {
			List<ExportingFormStatusDTO> result = exportingFormService.getExportingFormStatus(formId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (ExportingFormNotFoundException e) {
			log.error("Error",e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	
	@PostMapping("/transporter/{formId}/{transporter}/status/review_accept")
	public ResponseEntity<?> updateQuotePriceInformation(
			@PathVariable("formId") Integer formId,
			@PathVariable("transporter") String transporter,
			@RequestHeader("X-SECRET-KEY") String secretKey,
			@RequestBody QuotePriceData quotePriceData
			){
		
		
			ExportingFormStatusDTO result;
			try {
				result = exportingFormService.updateStatusAcceptAndTransferByTransporter(formId, transporter, secretKey, quotePriceData);
				return ResponseEntity.ok(result);
			} catch (IllegalStateException e) {
				log.error(e.getMessage(), e);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			} catch (ExportingFormNotFoundException  | TransporterNotFoundException e) {
				log.error(e.getMessage(), e);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			} 
	}
	
	@GetMapping("/{formId}/quote_price")
	public ResponseEntity<?> getQuoteShippingPriceInformation(@PathVariable("formId") Integer formId){
		
		try {
			QuotePriceInformationAggregator result = exportingFormService.getQuotePriceInformation(formId);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	
}
