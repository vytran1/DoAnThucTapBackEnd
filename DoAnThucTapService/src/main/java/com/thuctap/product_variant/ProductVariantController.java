package com.thuctap.product_variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.product_variant.dto.CheckExistOfSkuCodeRequest;
import com.thuctap.product_variant.dto.CheckExistOfSkuCodeResponse;

@RestController
@RequestMapping("/api/variants")
public class ProductVariantController {
	
	@Autowired
	private ProductVariantService service;
	
	
	@GetMapping("/exist/skuCode/{code}")
	public ResponseEntity<CheckExistOfSkuCodeResponse> checkExistOfSkuCode(@PathVariable("code") String code){
		
		CheckExistOfSkuCodeResponse response = service.checkExistOfSkuCode(code);
		
		return ResponseEntity.ok(response);
		
	}
	
}
