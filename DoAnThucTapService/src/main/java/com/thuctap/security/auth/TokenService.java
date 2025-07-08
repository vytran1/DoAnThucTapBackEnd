package com.thuctap.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.security.jwt.JwtUtility;

@Service
public class TokenService {
	
	@Autowired
	private JwtUtility jwtUtility;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public AuthResponse generateToken(InventoryEmployee employee) {
		
		String accessToken = jwtUtility.generateAccessToken(employee);
		
		AuthResponse response = new AuthResponse();
		response.setAccessToken(accessToken);
		
		return response;
	}
	
}
