package com.thuctap.utility;

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
	
}
