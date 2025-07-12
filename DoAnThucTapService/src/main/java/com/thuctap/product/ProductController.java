package com.thuctap.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.product.dto.ProductFindAllDTOList;
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
	
	
	
	@GetMapping("")
	public ResponseEntity<ProductFindAllDTOList> getAllByPage(
			@RequestParam(defaultValue = "1")	Integer pageNum,
			@RequestParam(defaultValue = "5")	Integer pageSize,
			@RequestParam(defaultValue = "id")	String  sortField,
			@RequestParam(defaultValue = "asc")	String  sortDir
			)
	{
		
		
		ProductFindAllDTOList result = productService.getByPage(pageNum, pageSize, sortField, sortDir);
		return ResponseEntity.ok(result);
		
	}
	
}
