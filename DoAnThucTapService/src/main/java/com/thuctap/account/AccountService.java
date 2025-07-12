package com.thuctap.account;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.account.dto.ChangePasswordRequest;
import com.thuctap.account.dto.PersonalInformation;
import com.thuctap.common.exceptions.PasswordNotMatchException;
import com.thuctap.inventory_employee.InventoryEmployeeRepository;
import com.thuctap.utility.UtilityGlobal;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
	
	
	
	@Autowired
	private InventoryEmployeeRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public PersonalInformation getInformationOfCurrentLoggedUser() {
		
		Integer employeeId = UtilityGlobal.getIdOfCurrentLoggedUser();
		
		return repository.findInformationOfCurrentLoggedEmployee(employeeId);
	}
	
	
	public String getImageOfCurrentLoggedUser() {
		Integer employeeId = UtilityGlobal.getIdOfCurrentLoggedUser();
		
		return repository.findImageById(employeeId);
	}
	
	
	
	@Transactional
	public void changePassword(ChangePasswordRequest request) throws PasswordNotMatchException {
		
		Integer id = UtilityGlobal.getIdOfCurrentLoggedUser();
		
		String passwordFromDatabase = repository.findPasswordByEmployeeId(id);
		
		comparePassword(passwordFromDatabase,request.getOldPassword());
		
		String encodedPassword = passwordEncoder.encode(request.getNewPassword());
		
		repository.updatePassword(id, encodedPassword);
		
		
	}
	
	public void comparePassword(String passwordFromDatabase, String passwordFromUser) throws PasswordNotMatchException {
		
		boolean isMatch = passwordEncoder.matches(passwordFromUser, passwordFromDatabase);
		
		if(!isMatch) {
			throw new PasswordNotMatchException("Password is not match with that in the database");
		}
		
	}
	
	@Transactional
	public String changeImage(MultipartFile file) throws IOException {
		Integer id = UtilityGlobal.getIdOfCurrentLoggedUser();
		Path folderPath = Paths.get("C:/DoAnThucTapImages/InventoryEmployee",String.valueOf(id));
		
		Files.createDirectories(folderPath);
		
		deleteExistingFile(folderPath);
		
		String newFileName = setupNewFileName(file,id);
		
		Path filePath = folderPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        repository.updateImage(id, newFileName);
        
        return newFileName;
	}

	
	
	

	private String setupNewFileName(MultipartFile file,Integer employeeId) {
		
		 String originalName = Paths.get(file.getOriginalFilename()).getFileName().toString();
	     String extension = originalName.substring(originalName.lastIndexOf("."));
	     
	     String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
	     String newFileName = employeeId + "_" + timestamp + extension;
	     
	     return newFileName;
	}



	private void deleteExistingFile(Path folderPath) throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
            for (Path path : stream) {
                Files.deleteIfExists(path);
            }
        }
	}
	
	
	
	
	
}
