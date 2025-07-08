package com.thuctap.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.inventory_employee.InventoryEmployeeRepository;


@Service
public class CustomerUserDetailsService implements UserDetailsService {
	
	@Autowired
	private InventoryEmployeeRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<InventoryEmployee> employeeOTP = repository.findByEmail(username);
		
		if(!employeeOTP.isPresent()) {
			throw new UsernameNotFoundException("No User With The Given Username");
		}
		return new CustomerUserDetails(employeeOTP.get());
	}
	
	
	

}
