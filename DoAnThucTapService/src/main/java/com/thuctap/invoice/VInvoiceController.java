package com.thuctap.invoice;

import org.springframework.beans.factory.annotation.Autowired;
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
			service.save(dto);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		
	}
	
	
}
