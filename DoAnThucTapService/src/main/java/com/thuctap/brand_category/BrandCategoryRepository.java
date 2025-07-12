package com.thuctap.brand_category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.category.dto.CategoryDropDownList;
import com.thuctap.common.brandcategory.BrandCategory;

public interface BrandCategoryRepository extends JpaRepository<BrandCategory,Integer> {

	
	@Query("SELECT new com.thuctap.category.dto.CategoryDropDownList(c.id, c.name) " +
		       " FROM BrandCategory bc " +
		       " JOIN bc.brand b " +
		       " JOIN bc.category c " +
		       " WHERE b.id = :brandId")
	List<CategoryDropDownList> findCategoriesByBrandId(Integer brandId);
	
	
	@Query("SELECT bc FROM BrandCategory bc WHERE bc.brand.id = ?1 AND bc.category.id = ?2")
	public BrandCategory findByBrandIdAndCategoryId(Integer brandId, Integer categoryId);
	
	
}
