package com.thuctap.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.invoice.dto.VInvoiceDTO;

@RestController
@RequestMapping("/api/invoices")
public class VInvoiceController {
	
	@Autowired
	private VInvoiceService service;
	
	
	@PostMapping("")
	public ResponseEntity<?> saveInvoice(@RequestBody VInvoiceDTO dto){
		try {			
			byte[] pdfBytes = service.save(dto);
			
			HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            
            headers.setContentDisposition(
                    ContentDisposition.inline()
                        .filename("invoice.pdf")
                        .build()
            );
            
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(pdfBytes);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		
	}
	
	
}
