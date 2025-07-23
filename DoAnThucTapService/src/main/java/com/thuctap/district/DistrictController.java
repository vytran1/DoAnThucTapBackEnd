package com.thuctap.district;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/districts")
public class DistrictController {
	@Autowired
	private DistrictService districtService;
	
	@GetMapping
	public ResponseEntity<List<DistrictDTO>> getAllDistricts() { 
		List<DistrictDTO> districtList = districtService.getAllDistrict();
		return ResponseEntity.ok(districtList);
	}
	
}
