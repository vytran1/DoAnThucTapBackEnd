package com.thuctap.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.thuctap.common.customer.Customer;
import com.thuctap.security.CustomerAccountUserDetail;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class CustomerJwtTokenFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerJwtTokenFilter.class);
	
	@Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!hasAuthorizationBearerToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getBearerToken(request);
        LOGGER.info("Customer JWT Token: " + token);

        try {
            Claims claims = jwtUtility.validateAccessToken(token);
            UserDetails userDetails = getUserDetails(claims);

            setAuthenticationContext(userDetails, request);
            filterChain.doFilter(request, response);
            clearAuthenticationContext();
        } catch (JwtValidationException e) {
            LOGGER.error(e.getMessage(), e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
    
    
    private boolean hasAuthorizationBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        LOGGER.info("Authorization Header " + header);
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer ");
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
    
    private UserDetails getUserDetails(Claims claims) {
        String subject = claims.getSubject(); 
        String[] array = subject.split(",");
        Integer id = Integer.valueOf(array[0]);
        String email = array[1];
        String fullName = array[2];

        Customer customer = new Customer();
        customer.setId(id);
        customer.setEmail(email);
        customer.setFullName(fullName); 

        LOGGER.info("Parsed Customer JWT: " + id + " | " + email + " | " + fullName);

        return new CustomerAccountUserDetail(customer);
    }
    

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    private void clearAuthenticationContext() {
        SecurityContextHolder.clearContext();
    }
    
    
    
    

}
