package com.thuctap.inventory_employee;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.inventory_roles.InventoryRole;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class InventoryEmployeeRepositoryTest {
	
	@Autowired
	private InventoryEmployeeRepository repository;
	
	
	
	@Test
	public void testSaveEmployeesToDatabase_shouldSuccess() {
		
		InventoryEmployee inventoryEmployee = new InventoryEmployee();
		inventoryEmployee.setEmail("n21dcvt128@gmail.com");
		inventoryEmployee.setFirstName("Trần Nguyễn");
		inventoryEmployee.setLastName("Vỹ");
		inventoryEmployee.setPhoneNumber("0979847481");
		inventoryEmployee.setInventoryRole(new InventoryRole(2));
		inventoryEmployee.setInventory(new Inventory(2));
		inventoryEmployee.setPassword("$10$UGp1w7ULeJPRTtsCalA0dum2iMxVsQHBc/ug6VrQwjhcxBZA.Oqre");
		
		InventoryEmployee savedInventoryEmployee = repository.save(inventoryEmployee);
		Assertions.assertThat(savedInventoryEmployee).isNotNull();
	}
	
	@Test
	public void testGetEmployeeById_shouldSuccess() {
		
		Integer employeeId = 2;
		
		Optional<InventoryEmployee> employeeOTP = repository.findById(employeeId);
		
		assertThat(employeeOTP).isNotNull();
		
		if(!Objects.isNull(employeeOTP)) {
			
			InventoryEmployee employee = employeeOTP.get();
			
			System.out.println(employee.getFirstName() + employee.getLastName());
			System.out.println(employee.getIsDelete());
			System.out.println(employee.getInventory().getInventoryCode());
			System.out.println(employee.getInventoryRole().getName());
			
			
			
		}
		
		
	}
	
	
	
	
}
