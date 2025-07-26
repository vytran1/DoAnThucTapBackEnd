package com.thuctap.product_variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.exceptions.VariantNotFoundException;
import com.thuctap.product_variant.dto.CheckExistOfSkuCodeRequest;
import com.thuctap.product_variant.dto.CheckExistOfSkuCodeResponse;
import com.thuctap.product_variant.dto.ProductVariantDetailDTO;
import com.thuctap.product_variant.dto.ProductVariantForTransactionAggregator;
import com.thuctap.product_variant.dto.ProductVariantWithStockAggregator;

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
	
	@PutMapping("/update")
	public ResponseEntity<?> updateVariant(@RequestBody ProductVariantDetailDTO dto){
		
		try {
			ProductVariantDetailDTO newDTO = service.updateVariant(dto);
			return ResponseEntity.ok(newDTO);
		} catch (VariantNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<ProductVariantForTransactionAggregator> searchForTransaction(
				@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
				@RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
				@RequestParam(value = "sortField", defaultValue = "id") String sortField,
				@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
				@RequestParam(value = "name", defaultValue = "", required = false) String variantName,
				@RequestParam(value = "categoryId",required = false) Integer categoryId
			){
		
		ProductVariantForTransactionAggregator result = service.findAllVariantForTransactions(pageNum
				,pageSize, 
				sortField, 
				sortDir, 
				variantName, 
				categoryId);
		return ResponseEntity.ok(result);
		
	}
	
	
	@GetMapping("/search/sale")
	public ResponseEntity<ProductVariantWithStockAggregator> searchForSale(
				@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
				@RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
				@RequestParam(value = "sortField", defaultValue = "id") String sortField,
				@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
				@RequestParam(value = "name", defaultValue = "", required = false) String variantName,
				@RequestParam(value = "categoryId",required = false) Integer categoryId
			){
		
		ProductVariantWithStockAggregator result = service.findAllVariantWithStock(pageNum,pageSize,sortField,sortDir, variantName,categoryId);
		
		return ResponseEntity.ok(result);
		
	}
	
	
	
}
