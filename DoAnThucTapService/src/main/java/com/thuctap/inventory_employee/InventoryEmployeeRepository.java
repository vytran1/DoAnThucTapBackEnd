package com.thuctap.inventory_employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.inventory_employees.InventoryEmployee;

public interface InventoryEmployeeRepository extends JpaRepository<InventoryEmployee,Integer> {
	
	public Optional<InventoryEmployee> findByEmail(String email);
	
}
