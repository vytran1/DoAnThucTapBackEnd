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
	
	
	
//	@Test
	public void testSaveEmployeesToDatabase_shouldSuccess() {
		
		InventoryEmployee inventoryEmployee = new InventoryEmployee();
		inventoryEmployee.setEmail("vy.tn171003@gmail.com");
		inventoryEmployee.setFirstName("Trần Nguyễn");
		inventoryEmployee.setLastName("Vỹ");
		inventoryEmployee.setPhoneNumber("0979847481");
		inventoryEmployee.setInventoryRole(new InventoryRole(1));
		inventoryEmployee.setInventory(new Inventory(1));
		inventoryEmployee.setPassword("$2a$12$poT6EdckHKPYLj8UbQfZQOM6DomBxkaQqY7rdGbkUWHi.7Ily2m4u");
		
		InventoryEmployee savedInventoryEmployee = repository.save(inventoryEmployee);
		Assertions.assertThat(savedInventoryEmployee).isNotNull();
	}
	
	@Test
	public void testGetEmployeeById_shouldSuccess() {
		
		Integer employeeId = 1;
		
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
