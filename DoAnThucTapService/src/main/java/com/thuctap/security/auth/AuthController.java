package com.thuctap.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.security.CustomerUserDetails;
import com.thuctap.security.CustomerUserDetailsService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	
	@PostMapping("/token")
	public ResponseEntity<AuthResponse> getToken(@RequestBody AuthRequest request){
		String username = request.getUsername();
		String password = request.getPassword();
		
		try {
			Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
			
			CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
			
			InventoryEmployee employee = userDetails.getInventoryEmployee();
			
			AuthResponse response = tokenService.generateToken(employee);
			
			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		
	}
	
}
