package com.thuctap.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.district.DistrictDTO;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/api/v1")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces() {
        return ResponseEntity.ok(locationService.getAllProvinces());
    }

    @GetMapping("/provinces/{provinceId}/districts")
    public ResponseEntity<List<DistrictDTO>> getDistrictsByProvince(@PathVariable Integer provinceId) {
        return ResponseEntity.ok(locationService.getDistrictsByProvince(provinceId));
    }
}