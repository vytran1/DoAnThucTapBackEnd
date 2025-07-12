package com.thuctap.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.brand.dto.BrandDTOForDropDown;

@Service
public class BrandService {
	
	@Autowired
	private BrandRepository brandRepository;
	
	
	public List<BrandDTOForDropDown> getAllBrandForDropdownList(){
		return brandRepository.findAllForDropdownList();
	}
	
	
}
