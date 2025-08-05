package com.thuctap.customer.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.customer.Customer;
import com.thuctap.customer.CustomerRepository;

@RestController
@RequestMapping("/sale/customer/account")
public class CustomerPersonalInformationController {

	@Autowired
	private CustomerPersonalInformationService customerPersonalInformationService;
	
	
	
	
	@GetMapping("/info")
	public ResponseEntity<?> getPersonalInformationOfCurrentLoggedCustomer(){
		
		Customer customer = customerPersonalInformationService.getPersonalInformationOfCurrentLoggedCustomer();
		
		
		return ResponseEntity.status(HttpStatus.OK).body(customer);
		
	}
	
	
	
	
	
}
