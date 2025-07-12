package com.thuctap.product_variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.product_variant.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {


	@Query("SELECT COUNT(p) > 0 FROM ProductVariant p WHERE p.sku LIKE CONCAT(:baseSku, '_%')")
	boolean existsByBaseSku(@Param("baseSku") String baseSku);
	
}
