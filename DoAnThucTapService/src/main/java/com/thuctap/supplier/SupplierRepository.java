package com.thuctap.supplier;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.supplier.Supplier;
import com.thuctap.supplier.dto.SupplierDropdownListDTO;

public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
	
	
	@Query("""
			SELECT new com.thuctap.supplier.dto.SupplierDropdownListDTO(s.id,s.supplierCode,s.name)
			FROM Supplier s
			""")
	public List<SupplierDropdownListDTO> findAllSupplierForDropDownList();
	
	
	@Query("""
			SELECT s FROM Supplier s WHERE s.supplierCode = ?1
			""")
	public Optional<Supplier> findBySupplierCode(String code);
	
}
