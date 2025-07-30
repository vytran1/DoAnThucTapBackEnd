package com.thuctap.district;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.district.District;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByProvinceId(Integer provinceId);

}
