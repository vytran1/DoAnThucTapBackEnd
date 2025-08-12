package com.thuctap.exporting_form;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.exceptions.ExportingFormNotFoundException;
import com.thuctap.common.exceptions.TransporterNotFoundException;
import com.thuctap.common.exporting_form.QuotePriceData;
import com.thuctap.exporting_form.dto.CreateExportingFormRequest;
import com.thuctap.exporting_form.dto.ExportingFormOverviewDTO;
import com.thuctap.exporting_form.dto.ExportingFormPageAggregator;
import com.thuctap.exporting_form.dto.ExportingFormStatusDTO;
import com.thuctap.exporting_form.dto.QuotePriceInformationAggregator;
import com.thuctap.importing_form.dto.ImportingFormPageAggregator;

@RestController
@RequestMapping("/api/exporting_form")
public class ExportingFormController {
		
	private static final Logger log = LoggerFactory.getLogger(ExportingFormController.class); 
	
	
	@Autowired
	private ExportingFormService exportingFormService;
	
	
	@GetMapping("/search")
	public ResponseEntity<ExportingFormPageAggregator> search(
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "sortField", defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(value = "startDate", required = false) OffsetDateTime startDate,
			@RequestParam(value = "endDate", required = false) OffsetDateTime endDate
			){
		
		 LocalDateTime start = startDate != null ? startDate.toLocalDateTime() : null;
		 LocalDateTime end = endDate != null ? endDate.toLocalDateTime() : null;
		 
		 ExportingFormPageAggregator result = exportingFormService.search(start, end, pageNum, pageSize, sortField, sortDir);
		
		 return ResponseEntity.ok(result);
	}
	
	
	
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
	
	@PostMapping("/transporter/{formId}/{transporter}/status/reject")
	public ResponseEntity<?> rejectQuotationByTransporter(
			@PathVariable("formId") Integer formId,
			@PathVariable("transporter") String transporter,
			@RequestHeader("X-SECRET-KEY") String secretKey,
			@RequestBody QuotePriceData quotePriceData
			){
		
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updateReviewAndRejectedStatusByTransporter(formId, transporter, secretKey, quotePriceData);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException | TransporterNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}  catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/{formId}/status/paying")
	public ResponseEntity<?> updatePayingStatusByInventory(@PathVariable("formId") Integer formId){
		
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updatePayingStatus(formId);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}  catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/transporter/{formId}/{transporter}/status/payed")
	public ResponseEntity<?> updatePayedStatusByTransporter(
			@PathVariable("formId") Integer formId,
			@PathVariable("transporter") String transporter,
			@RequestHeader("X-SECRET-KEY") String secretKey
			){
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updatePayedStatus(formId, transporter, secretKey);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException | TransporterNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	
	@PostMapping("/{formId}/status/accept_price")
	public ResponseEntity<?> updateAcceptPriceStatus(@PathVariable("formId") Integer formId){
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updateAcceptPriceStatusByInventory(formId);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/{formId}/status/reject_price")
	public ResponseEntity<?> updateRejectPriceStatus(@PathVariable("formId") Integer formId){
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updateRejectPriceStatusByInventory(formId);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/{formId}/status/giving_product")
	public ResponseEntity<?> updateGivingProductStatus(@PathVariable("formId") Integer formId){
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updateGivingProductStatus(formId);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/transporter/{formId}/{transporter}/status/shipping")
	public ResponseEntity<?> updateShippingStatusByTransporter(
			@PathVariable("formId") Integer formId,
			@PathVariable("transporter") String transporter,
			@RequestHeader("X-SECRET-KEY") String secretKey){
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updateShippingStatusByTransporter(formId, transporter, secretKey);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException | TransporterNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@PostMapping("/transporter/{formId}/{transporter}/status/arriving")
	public ResponseEntity<?> updateArrivingStatusByTransporter(
			@PathVariable("formId") Integer formId,
			@PathVariable("transporter") String transporter,
			@RequestHeader("X-SECRET-KEY") String secretKey){
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updateArrivingStatusByTransporter(formId, transporter, secretKey);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException | TransporterNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/{formId}/status/finish")
	public ResponseEntity<?> updateFinishStatusByInventory(@PathVariable("formId") Integer formId){
		
		try {
			ExportingFormStatusDTO result = exportingFormService.updateFinishStatusByInventory(formId);
			return ResponseEntity.ok(result);
		} catch (ExportingFormNotFoundException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
