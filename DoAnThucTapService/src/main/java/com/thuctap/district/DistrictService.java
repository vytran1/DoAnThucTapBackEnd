package com.thuctap.district;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.common.district.District;

@Service
public class DistrictService {
	@Autowired
	private DistrictRepository districtRepository;
	
	public DistrictDTO convertDistrictDTO(District district)  {
		DistrictDTO dto = new DistrictDTO();
		
		dto.setId(district.getId());
		dto.setName(district.getName());
		dto.setProvinceId(district.getProvince().getId());
		dto.setProvinceName(district.getProvince().getName());
		
		return dto;
	}
	
	public List<DistrictDTO> getAllDistrict() { 
		List<District> districts = districtRepository.findAll();
		
		return districts.stream() 
				.map(this::convertDistrictDTO)
				.collect(Collectors.toList());
	}
	
//	public DistrictDTO getDistrictById(Integer id) {
//		District district = districtRepository.findById(id).orElse(null);
//		
//		if(district != null) {
//			return convertDistrictDTO(district);
//		}
//		
//		return null;
//	}
	
}
