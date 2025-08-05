package com.thuctap.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.thuctap.common.customer.Customer;
import com.thuctap.customer.CustomerRepository;

public class CustomerAccountUserDetailService implements UserDetailsService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Customer> customerOPT = customerRepository.findByEmail(username);
		
		if(!customerOPT.isPresent()) {
			throw new UsernameNotFoundException("No User With The Given Username");
		}
		
		return new CustomerAccountUserDetail(customerOPT.get());
	}
	
	
	

}
