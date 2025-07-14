package com.thuctap.product_attribute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.product_attribute.ProductAttribute;
import com.thuctap.product_attribute.dto.ProductAttributeForProductDetailDTO;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Integer> {
		
	
	
	
	@Query("""
			SELECT new com.thuctap.product_attribute.dto.ProductAttributeForProductDetailDTO(pa.name)
			FROM ProductAttribute pa
			JOIN pa.product p
			WHERE p.id = ?1
			ORDER BY pa.position ASC
			""")
	public List<ProductAttributeForProductDetailDTO> findAllAttributeByProductId(Integer id);
	
}
