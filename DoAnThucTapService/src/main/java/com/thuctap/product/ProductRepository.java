package com.thuctap.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.product.Product;
import com.thuctap.product.dto.ProductFindAllDTO;
import com.thuctap.product.dto.ProductOverviewDTO;

public interface ProductRepository extends JpaRepository<Product,Integer> {
	
	
	
	@Query("""
			SELECT new com.thuctap.product.dto.ProductFindAllDTO(p.id,p.image,p.name,v.sku,b.name,c.name) FROM Product p
			JOIN p.brandCategory bc
			JOIN bc.brand b
			JOIN bc.category c
			JOIN ProductVariant v ON v.product = p AND v.isDefault = true
			WHERE p.isDelete = false
			""")
	Page<ProductFindAllDTO> findAllWithSkuCode(Pageable pageable);
	
	
	
	@Query("""
			SELECT new com.thuctap.product.dto.ProductOverviewDTO(p.name,v.sku,b.name,c.name,v.price) 
			FROM Product p
			JOIN p.brandCategory bc
			JOIN bc.brand b
			JOIN bc.category c
			JOIN ProductVariant v ON v.product = p AND v.isDefault = true
			WHERE p.id = ?1 AND p.isDelete = false
			""")
	public Optional<ProductOverviewDTO> findProductById(Integer id);
	
	
	@Query("UPDATE Product p SET p.image = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateProductMainImage(Integer id, String imagePath);
	
	
	@Query("SELECT p.image FROM Product p WHERE p.id = ?1")
	public String findMainImageByProductId(Integer id);

}
