package com.thuctap.security.jwt;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thuctap.common.inventory_employees.InventoryEmployee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtility {
	
	
	private static final String SECRET_KEY_ALGORITHMS = "HmacSHA512";
	
	@Value("${app.security.jwt.issuer}")
	private String issueName;
	
	@Value("${app.security.jwt.secret}")
	private String secretKey;
	
	@Value("${app.security.jwt.access-token.expiration}")
	private int accessTokenExpiration;
	
	
	public String generateAccessToken(InventoryEmployee employee) {
		if(employee == null || employee.getEmail().equals(null)) {
			throw new IllegalArgumentException("employee object is null");
		}
		
		long expirationTime = System.currentTimeMillis() + accessTokenExpiration * 60000;
		
		String subject = String.format("%s,%s,%s,%s",employee.getId(),
														employee.getEmail(),
														employee.getInventory().getInventoryCode(),
														employee.getInventory().getId()	
				);
		
		
		
		return Jwts.builder()
				.subject(subject)
				.issuer(issueName)
				.issuedAt(new Date())
				.expiration(new Date(expirationTime))
				.claim("role",employee.getInventoryRole().getName())
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),Jwts.SIG.HS512)
				.compact();
				
				
	}
	
	public Claims validateAccessToken(String token) throws JwtValidationException{
		try {
			SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(),SECRET_KEY_ALGORITHMS);
			
			return Jwts.parser()
					.verifyWith(keySpec)
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch(ExpiredJwtException e) {
			throw new JwtValidationException("Access Token expired",e);
		} catch(IllegalArgumentException e) {
			throw new JwtValidationException("Access Token is illegal",e);
		} catch(MalformedJwtException e) {
			throw new JwtValidationException("Access Token is not well formed",e);
		} catch(UnsupportedJwtException e) {
			throw new JwtValidationException("Access Token is not supported",e);
		}
	}
	
	
	public String getIssueName() {
		return issueName;
	}
	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public int getAccessTokenExpiration() {
		return accessTokenExpiration;
	}
	public void setAccessTokenExpiration(int accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}
	
	
	
	
	
	
}
