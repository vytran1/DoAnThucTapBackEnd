package com.thuctap.inventory_employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.account.dto.PersonalInformation;
import com.thuctap.common.inventory_employees.InventoryEmployee;

public interface InventoryEmployeeRepository extends JpaRepository<InventoryEmployee,Integer> {
	
	public Optional<InventoryEmployee> findByEmail(String email);
	
	
	
	@Query("SELECT new com.thuctap.account.dto.PersonalInformation(ie.email,ie.firstName,ie.lastName) "
			+ " FROM InventoryEmployee ie "
			+ " WHERE ie.id = ?1")
	public PersonalInformation findInformationOfCurrentLoggedEmployee(Integer id);
	
	
	
	@Query("SELECT ie.password FROM InventoryEmployee ie WHERE ie.id = ?1")
	public String findPasswordByEmployeeId(Integer id);
	
	
	
	@Query("UPDATE InventoryEmployee SET password = ?2 WHERE id = ?1")
	@Modifying
	public void updatePassword(Integer id, String newPassword);
	
	
	@Query("UPDATE InventoryEmployee SET personalImage = ?2 WHERE id = ?1")
	@Modifying
	public void updateImage(Integer id, String imagePath);
	
	
	@Query("SELECT ie.personalImage FROM InventoryEmployee ie WHERE ie.id = ?1")
	public String findImageById(Integer id);
	
}
