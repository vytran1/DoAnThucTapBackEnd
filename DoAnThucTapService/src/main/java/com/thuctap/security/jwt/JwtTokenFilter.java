package com.thuctap.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.inventory_roles.InventoryRole;
import com.thuctap.security.CustomerUserDetails;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
	
	@Autowired
	private JwtUtility jwtUtility;
	
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver handlerExceptionResolver;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		if(!hasAuthorizationBearerToken(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = getBearerToken(request);
		
		LOGGER.info("Bearer Token " + token);
		
		try {
			
			Claims claims = jwtUtility.validateAccessToken(token);
			UserDetails userDetails = getUserDetails(claims);
			
			setAuthenticationContext(userDetails,request);
			
			filterChain.doFilter(request, response);
			
			clearAuthenticationContext();
			
		} catch (JwtValidationException e) {
			
			LOGGER.error(e.getMessage(),e);
			handlerExceptionResolver.resolveException(request, response,null,e);
		
		}
		
	}

	private void clearAuthenticationContext() {
		SecurityContextHolder.clearContext();
		
	}

	private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
		var authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private UserDetails getUserDetails(Claims claims) {
		
		String subject = (String) claims.get(Claims.SUBJECT);
		String[] array = subject.split(",");
		
		Integer employeeId = Integer.valueOf(array[0]);
		String email = array[1];
		
		
		InventoryEmployee inventoryEmployee = new InventoryEmployee();
		inventoryEmployee.setId(employeeId);
		inventoryEmployee.setEmail(email);
		
		String roleName = (String) claims.get("role");
		InventoryRole inventoryRole = new InventoryRole();
		inventoryRole.setName(roleName);
		
		String inventoryCode = array[2];
		Inventory inventory = new Inventory();
		inventory.setInventoryCode(inventoryCode);
		
		
		inventoryEmployee.setInventoryRole(inventoryRole);
		inventoryEmployee.setInventory(inventory);
		
		LOGGER.info("Information parsed From JWT" + 
		employeeId + " " + " " + 
				email + " " + 
		roleName + " " + 
				inventoryCode);
		
		
		return new CustomerUserDetails(inventoryEmployee);
	}

	private boolean hasAuthorizationBearerToken(HttpServletRequest request) {
		
		String header = request.getHeader("Authorization");
		
		LOGGER.info("Authorization Header " + header);
		
		if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}
		
		return true;
	}
	
	
	private String getBearerToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		
		String[] array = header.split(" ");
		
		if(array.length == 2) 
		{
			return array[1];
		}
		
		return null;
	}
	
	
	
	

}
