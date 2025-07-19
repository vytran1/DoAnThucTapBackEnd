package com.thuctap.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.category.dto.CategoryDropDownList;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	
	@Autowired
	private CategoryService categoryService;
	
	
	
	@GetMapping("list/drop-down/{brandId}")
	public ResponseEntity<List<CategoryDropDownList>> getAllCategoriesBelongToBrand(@PathVariable("brandId") Integer brandId){
		
		return ResponseEntity.ok(categoryService.getAllCategoriesBelongToABrand(brandId));
		
	}
	
	
	@GetMapping("list/drop-down")
	public ResponseEntity<List<CategoryDropDownList>> getAllCategoriesForDropDownList(){
		return ResponseEntity.ok(categoryService.getAllCategoriesForDropdownList());
	}
	
}
