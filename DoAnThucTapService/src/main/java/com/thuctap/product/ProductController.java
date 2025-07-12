package com.thuctap.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.product.dto.ProductSaveInformationDTO;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("")
	public ResponseEntity<?> saveProduct(@RequestBody ProductSaveInformationDTO dto){
		
		productService.saveProduct(dto);
		
		return ResponseEntity.ok().build();
		
	}
	
}
