package com.thuctap.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.thuctap.common.inventory_employees.InventoryEmployee;

public class CustomerUserDetails implements UserDetails {
	
	
	private InventoryEmployee inventoryEmployee;
	
	
	
	public CustomerUserDetails(InventoryEmployee inventoryEmployee) {
		super();
		this.inventoryEmployee = inventoryEmployee;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(inventoryEmployee.getInventoryRole().getName()));
		
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return  inventoryEmployee.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return inventoryEmployee.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return !inventoryEmployee.getIsDelete();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !inventoryEmployee.getIsDelete();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return !inventoryEmployee.getIsDelete();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return !inventoryEmployee.getIsDelete();
	}

	public InventoryEmployee getInventoryEmployee() {
		return inventoryEmployee;
	}

	public void setInventoryEmployee(InventoryEmployee inventoryEmployee) {
		this.inventoryEmployee = inventoryEmployee;
	}
	
	
	

}
