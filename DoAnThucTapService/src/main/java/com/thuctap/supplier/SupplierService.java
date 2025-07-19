package com.thuctap.supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.supplier.dto.SupplierDropdownListDTO;

@Service
public class SupplierService {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	
	public List<SupplierDropdownListDTO> getAllSupplierForDropdownList(){
		return supplierRepository.findAllSupplierForDropDownList();
	}
	
}
