package com.thuctap.account;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.account.dto.ChangePasswordRequest;
import com.thuctap.account.dto.PersonalInformation;
import com.thuctap.common.exceptions.PasswordNotMatchException;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
		
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/info")
	public ResponseEntity<PersonalInformation> getInformationOfCurrentLoggedUser(){
		
		
		PersonalInformation result = accountService.getInformationOfCurrentLoggedUser();
		
		return ResponseEntity.ok(result);
		
	}
	
	@GetMapping("/image")
	public ResponseEntity<String> getImageOfCurrentLoggedUser(){
		
		String fileName = accountService.getImageOfCurrentLoggedUser();
		
		return ResponseEntity.status(HttpStatus.OK).body(fileName);
		
	}
	
	
	
	@PostMapping("/change/password")
	public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordRequest request){
		
		try {
			accountService.changePassword(request);
			return ResponseEntity.ok().build();
		} catch (PasswordNotMatchException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	
	@PostMapping("/change/image")
	public ResponseEntity<String> updateImage(@RequestParam("file") MultipartFile file){
		
		try {
			String newFileName = accountService.changeImage(file);
			return ResponseEntity.ok(newFileName);
		} catch (IOException e) {
			
			return ResponseEntity.internalServerError().build();
		}
		
	}
	
	
}
