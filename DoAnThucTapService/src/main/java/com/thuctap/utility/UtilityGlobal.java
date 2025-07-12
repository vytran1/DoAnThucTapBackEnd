package com.thuctap.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.security.CustomerUserDetails;

public class UtilityGlobal {
	
	
	public static Integer getIdOfCurrentLoggedUser() 
	{
		CustomerUserDetails customerUserDetails = (CustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		InventoryEmployee employee = customerUserDetails.getInventoryEmployee();
		Integer id = employee.getId();
		return id;
	}
	
	
	public static Pageable setUpPageRequest(Integer pageNum, Integer pageSize, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1,pageSize,sort);
		
		return pageable;
	}
	
}
