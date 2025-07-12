package com.thuctap.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.brand.dto.BrandDTOForDropDown;
import com.thuctap.common.brand.Brand;

public interface BrandRepository extends JpaRepository<Brand,Integer> {
	
	
	@Query("SELECT new com.thuctap.brand.dto.BrandDTOForDropDown(b.id,b.name) FROM Brand b WHERE b.isDelete = false")
	public List<BrandDTOForDropDown> findAllForDropdownList();
	
}
