package com.thuctap.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.thuctap.common.customer.Customer;

public class CustomerAccountUserDetail implements UserDetails {
	
	private Customer customer;
	
	
	public CustomerAccountUserDetail(Customer customer) {
		super();
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return !customer.getIsDeleted();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !customer.getIsDeleted();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return !customer.getIsDeleted();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return !customer.getIsDeleted();
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	

}
