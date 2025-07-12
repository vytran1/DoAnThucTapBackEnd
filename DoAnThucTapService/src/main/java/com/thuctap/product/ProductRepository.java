package com.thuctap.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.product.Product;
import com.thuctap.product.dto.ProductFindAllDTO;

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

}
