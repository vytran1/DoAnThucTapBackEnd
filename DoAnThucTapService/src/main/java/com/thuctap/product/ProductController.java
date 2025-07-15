package com.thuctap.product;

import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.common.exceptions.ProductNotFoundException;
import com.thuctap.product.dto.ProductDetailForVariantAggregateDTO;
import com.thuctap.product.dto.ProductFindAllDTOList;
import com.thuctap.product.dto.ProductImageAggregatorDTO;
import com.thuctap.product.dto.ProductOverviewDTO;
import com.thuctap.product.dto.ProductSaveInformationDTO;
import com.thuctap.product_image.dto.ProductImageDTO;
import com.thuctap.product_variant.dto.ProductVariantInventoryDTO;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("")
	public ResponseEntity<?> saveProduct(@RequestBody ProductSaveInformationDTO dto){
		
		
		System.out.println(dto);
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
	
	
	@GetMapping("/{id}/overview")
	public ResponseEntity<?> getProductDetailsById(@PathVariable("id") Integer id){
		
		try {
			ProductOverviewDTO result = productService.getProductById(id);
			return ResponseEntity.ok(result);
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			Map<String,String> map = Map.of("message",e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		}
	}
	
	
	@GetMapping("/{id}/inventory")
	public ResponseEntity<?> getProductVariantDetailsWithStockingInformation(@PathVariable("id") Integer id){
		
		List<ProductVariantInventoryDTO> result = productService.getVariantWithStockingInformation(id);
		
		return ResponseEntity.ok(result);
		
	}
	
	
	
	@PutMapping("/{id}/change/image")
	public ResponseEntity<?> changeMainImage(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file){
		
		String newFileName = productService.changeMainImage(id, file);	
		Map<String,String> map = Map.of("image",newFileName);
		
		return ResponseEntity.ok(map);
	} 
	
	
	@PostMapping("/{id}/add/subImage")
	public ResponseEntity<ProductImageDTO> addSubImage(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file){
		
		ProductImageDTO result = productService.addSubImage(id, file);
		
		return ResponseEntity.ok(result);
		
	}
	
	
	@DeleteMapping("/delete/subImage/{subImageId}")
	public ResponseEntity<?> deleteSubImage(@PathVariable("subImageId") Integer subImageId){
		productService.deleteSubImage(subImageId);
		return ResponseEntity.ok().build();
	}
	
	
	@GetMapping("/{id}/images")
	public ResponseEntity<ProductImageAggregatorDTO> getAllImageInformation(@PathVariable("id") Integer id){
		
		ProductImageAggregatorDTO result = productService.getAllImageInformation(id);
		
		return ResponseEntity.ok(result);
		
	}
	
	
	
	@GetMapping("/{id}/variants")
	public ResponseEntity<?> getProductVariantsDetailsById(@PathVariable("id") Integer id){
		
		ProductDetailForVariantAggregateDTO result = productService.getVariantDetailByProductId(id);
		
		return ResponseEntity.ok(result);
		
	}
	
}
