package com.thuctap.transporter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.transporter.dto.TransporterDropdownListDTO;
import com.thuctap.utility.UtilityGlobal;

@Service
public class TransporterService {

	@Autowired
	private TransporterRepository transporterRepository;
	
	
	public List<TransporterDropdownListDTO> getTransporterForDropdownList(){
		return transporterRepository.getTransporterForDropdownList();
	}
	
}
