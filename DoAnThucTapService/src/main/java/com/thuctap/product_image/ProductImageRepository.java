package com.thuctap.product_image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.product_image.ProductImage;
import com.thuctap.product_image.dto.ProductImageDTO;

public interface ProductImageRepository extends JpaRepository<ProductImage,Integer> {
	
	
	
	
	@Query("SELECT new com.thuctap.product_image.dto.ProductImageDTO(pi.id,pi.name) FROM ProductImage pi WHERE pi.product.id = ?1")
	public List<ProductImageDTO> findAllSubImagesByProductId(Integer id);
	
}
