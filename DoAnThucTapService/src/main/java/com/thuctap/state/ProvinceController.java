package com.thuctap.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
	@Autowired
	private ProvinceService provinceService;
	
	@GetMapping
	public ResponseEntity<List<ProvinceDTO>> getAllProvinces() {
		List<ProvinceDTO> provinceList = provinceService.getAllProvinces();
		return ResponseEntity.ok(provinceList);
	}
}
