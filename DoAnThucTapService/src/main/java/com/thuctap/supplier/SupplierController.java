package com.thuctap.supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.supplier.dto.SupplierDropdownListDTO;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

	
	@Autowired
	private SupplierService supplierService;
	
	@GetMapping("/get/drop-down")
	public ResponseEntity<List<SupplierDropdownListDTO>> getAllSupplierForDropdownList(){
		return ResponseEntity.ok(supplierService.getAllSupplierForDropdownList()) ;
	}
	
	
	
}
