package com.thuctap.customer.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.common.customer.Customer;
import com.thuctap.customer.CustomerRepository;
import com.thuctap.utility.UtilityGlobal;

@Service
public class CustomerPersonalInformationService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	public Customer getPersonalInformationOfCurrentLoggedCustomer() {
		
		Integer id = UtilityGlobal.getIfOfCurrentLoggedCustomer();
		return customerRepository.findById(id).get();
		
	}
	
}
