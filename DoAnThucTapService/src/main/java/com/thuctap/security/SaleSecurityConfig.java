package com.thuctap.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.thuctap.security.jwt.CustomerJwtTokenFilter;
import com.thuctap.security.jwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@Order(2)
public class SaleSecurityConfig {
	
	@Autowired
	private JwtTokenFilter customerJwtTokenFilter;

	@Bean
	public UserDetailsService customerAccounUsertDetailsService() {
		return new CustomerAccountUserDetailService();
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public DaoAuthenticationProvider customerAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customerAccounUsertDetailsService());
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}
	
	@Bean(name = "customerAuthenticationManager")
	public AuthenticationManager customerAuthenticationManager() {
	    return new ProviderManager(customerAuthenticationProvider());
	}
	
	
	@Bean
	public SecurityFilterChain customerSecurityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .securityMatcher("/sale/**") 
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/sale/customer/auth/**").permitAll()
	                .anyRequest().authenticated()
	            )
	            .csrf(csrf -> csrf.disable())
	            .addFilterBefore(customerJwtTokenFilter,AuthorizationFilter.class);

	        return http.build();
	}

}
