package com.thuctap.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.brand_category.BrandCategoryRepository;
import com.thuctap.category.dto.CategoryDropDownList;

@Service
public class CategoryService {
	
	@Autowired
	private BrandCategoryRepository repository;
	
	
	public List<CategoryDropDownList> getAllCategoriesBelongToABrand(Integer brandId){
		return repository.findCategoriesByBrandId(brandId);
	}
	
	
}
