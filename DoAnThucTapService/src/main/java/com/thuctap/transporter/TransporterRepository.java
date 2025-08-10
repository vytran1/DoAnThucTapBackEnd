package com.thuctap.transporter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.transporter.Transporter;
import com.thuctap.transporter.dto.TransporterDropdownListDTO;

public interface TransporterRepository extends JpaRepository<Transporter,Integer> {
		
	
	
	@Query("""
			SELECT new com.thuctap.transporter.dto.TransporterDropdownListDTO(
				t.id, CONCAT(t.name,"-",t.transporterCode)
				)
			FROM Transporter t
			""")
	public List<TransporterDropdownListDTO> getTransporterForDropdownList();
	
	
	public Optional<Transporter> findByTransporterCode(String code);
	
}
