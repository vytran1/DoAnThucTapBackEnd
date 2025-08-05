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

import com.thuctap.common.customer.Customer;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.inventory_roles.InventoryRole;
import com.thuctap.security.CustomerAccountUserDetail;
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
		String userType = claims.get("type", String.class);
		
		
		if ("CUSTOMER".equalsIgnoreCase(userType)) {
	        return extractCustomerUserDetails(claims);
	    } else if ("EMPLOYEE".equalsIgnoreCase(userType)) {
	        return extractEmployeeUserDetails(claims);
	    }
		
		return null;
		
		
	}
	
	private UserDetails extractCustomerUserDetails(Claims claims) {
	    String subject = claims.getSubject(); // format: id,email,fullName
	    String[] parts = subject.split(",");


	    Integer customerId = Integer.valueOf(parts[0]);
	    String email = parts[1];
	    String fullName = parts[2];

	    Customer customer = new Customer();
	    customer.setId(customerId);
	    customer.setEmail(email);
	    customer.setFullName(fullName);

	    return new CustomerAccountUserDetail(customer);
	}
	
	private UserDetails extractEmployeeUserDetails(Claims claims) {
	    String subject = claims.getSubject(); // format: id,email,inventoryCode,inventoryId
	    String[] parts = subject.split(",");

	    Integer employeeId = Integer.valueOf(parts[0]);
	    String email = parts[1];
	    String inventoryCode = parts[2];
	    Integer inventoryId = Integer.valueOf(parts[3]);

	    String roleName = claims.get("role", String.class);

	    InventoryRole inventoryRole = new InventoryRole();
	    inventoryRole.setName(roleName);

	    Inventory inventory = new Inventory();
	    inventory.setInventoryCode(inventoryCode);
	    inventory.setId(inventoryId);

	    InventoryEmployee employee = new InventoryEmployee();
	    employee.setId(employeeId);
	    employee.setEmail(email);
	    employee.setInventory(inventory);
	    employee.setInventoryRole(inventoryRole);

	    return new CustomerUserDetails(employee);
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
