package com.thuctap.state;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.common.district.District;
import com.thuctap.common.state.Province;
import com.thuctap.district.DistrictDTO;
import com.thuctap.district.DistrictRepository;

@Service
public class LocationService {

    @Autowired
    private ProvinceRepository provinceRepository;
    
    @Autowired
    private DistrictRepository districtRepository;

    public List<ProvinceDTO> getAllProvinces() {
        return provinceRepository.findAll().stream()
            .map(this::convertToProvinceDto)
            .collect(Collectors.toList());
    }

    public List<DistrictDTO> getDistrictsByProvince(Integer provinceId) {
        return districtRepository.findByProvinceId(provinceId).stream()
            .map(this::convertToDistrictDto)
            .collect(Collectors.toList());
    }

    private ProvinceDTO convertToProvinceDto(Province province) {
        ProvinceDTO dto = new ProvinceDTO();
        dto.setId(province.getId());
        dto.setName(province.getName());
        return dto;
    }

    private DistrictDTO convertToDistrictDto(District district) {
        DistrictDTO dto = new DistrictDTO();
        dto.setId(district.getId());
        dto.setName(district.getName());
        dto.setProvinceId(district.getProvince().getId());
        dto.setProvinceName(district.getProvince().getName());
        return dto;
    }
}