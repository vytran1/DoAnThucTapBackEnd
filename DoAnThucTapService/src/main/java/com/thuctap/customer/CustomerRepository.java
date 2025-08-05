package com.thuctap.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
	
	
	public Optional<Customer> findByEmail(String email);
	
}
