package com.thuctap.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.thuctap.security.jwt.JwtTokenFilter;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity(debug = true)
@Order(1)
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtTokenFilter jwtTokenFilter;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
		daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		return daoAuthenticationProvider;
	}

	@Bean(name = "employeeAuthenticationManager")
	@Primary
	public AuthenticationManager employeeAuthenticationManager() {
		return new ProviderManager(daoAuthenticationProvider());
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.securityMatcher("/api/**").cors().configurationSource(corsConfigurationSource()).and()
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/**", "/api/forgot_password", "/api/reset_password").permitAll()
						.requestMatchers("/employee-images/**").permitAll().requestMatchers("/product-images/**")
						.permitAll().requestMatchers("/api/setting/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/orders/*/quote/supplier/*").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/orders/*/quote/reject/supplier/*").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/orders/*/supplier/*/confirm/payed").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/orders/*/supplier/*/status/shipping").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/orders/*/supplier/*/status/arriving").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/exporting_form/transporter/**").permitAll()
						.anyRequest().authenticated())
				.csrf(csrf -> csrf.disable())
				.exceptionHandling(exh -> exh.authenticationEntryPoint((request, response, exception) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
				}).accessDeniedHandler((request, response, exception) -> {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					response.setContentType("application/json");
					response.getWriter().write(
							"{\\\"error\\\": \\\"You do not have sufficient permissions to access this resource.\\\"}");
				})

				).addFilterBefore(jwtTokenFilter, AuthorizationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Set the frontend origin
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*", "Authorization"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
