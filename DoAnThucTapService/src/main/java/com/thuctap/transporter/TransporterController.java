package com.thuctap.transporter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.transporter.dto.TransporterDropdownListDTO;

@RestController
@RequestMapping("/api/transporters")
public class TransporterController {
	
	@Autowired
	private TransporterService transporterService;
	
	
	@GetMapping("/dropdown")
	public ResponseEntity<?> getTransporterForDropdownList(){
		List<TransporterDropdownListDTO> result = transporterService.getTransporterForDropdownList();
		return ResponseEntity.ok(result);
	}
	
	
	
	
	
}
