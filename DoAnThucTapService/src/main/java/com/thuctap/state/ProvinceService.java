package com.thuctap.state;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.common.state.Province;

@Service
public class ProvinceService {
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	public ProvinceDTO convertToDTO(Province province) {
		ProvinceDTO dto = new ProvinceDTO();
		dto.setId(province.getId());
		dto.setName(province.getName());
		return dto;	
	}
	
	public List<ProvinceDTO> getAllProvinces() {
		List<Province> provinceList = provinceRepository.findAll();
		
		return provinceList.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}
}
