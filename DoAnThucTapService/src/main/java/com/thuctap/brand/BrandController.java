package com.thuctap.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.brand.dto.BrandDTOForDropDown;

@RestController()
@RequestMapping("/api/brands")
public class BrandController {
	
	
	
	@Autowired
	private BrandService brandService;
	
	
	@GetMapping("/list/drop-down")
	public ResponseEntity<List<BrandDTOForDropDown>> getAllBrandForDropdownList(){
		
		List<BrandDTOForDropDown> result = brandService.getAllBrandForDropdownList();
		
		return ResponseEntity.ok(result);
		
		
	}
	
	
}
