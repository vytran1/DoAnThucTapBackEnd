package com.thuctap.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setting")
public class SettingController {

	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("")
	public ResponseEntity<?> getAllSettingVariables(){
		
		return ResponseEntity.ok(settingService.getALL());
		
	}
	
	
}
