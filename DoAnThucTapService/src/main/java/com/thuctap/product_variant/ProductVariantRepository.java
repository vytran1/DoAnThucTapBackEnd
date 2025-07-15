package com.thuctap.product_variant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.product_variant.dto.ProductVariantDetailDTO;
import com.thuctap.product_variant.dto.ProductVariantInventoryDTO;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {


	@Query("SELECT COUNT(p) > 0 FROM ProductVariant p WHERE p.sku LIKE CONCAT(:baseSku, '_%')")
	boolean existsByBaseSku(@Param("baseSku") String baseSku);
	
	
	
	@Query("""
			SELECT new com.thuctap.product_variant.dto.ProductVariantDetailDTO(pv.nameOverride,pv.sku,pv.price,pv.isDefault) 
			FROM ProductVariant pv 
			JOIN pv.product p
			WHERE p.id = ?1
			""")
	public List<ProductVariantDetailDTO> findAllVariantByProductId(Integer id);
	
	
	@Query("SELECT pv FROM ProductVariant pv WHERE pv.sku = ?1")
	public Optional<ProductVariant> findBySkuCode(String sku);
	
	
	
	
	@Query("""
			SELECT new com.thuctap.product_variant.dto.ProductVariantInventoryDTO(
				pv.nameOverride, 
				pv.sku, 
				pv.price, 
				COALESCE(SUM(s.quantity), 0)
				)
			FROM ProductVariant pv
			LEFT JOIN Stocking s ON s.productVariant.sku = pv.sku 
			WHERE pv.product.id = ?1
			GROUP BY pv.nameOverride, pv.sku, pv.price
			ORDER BY pv.id
			""")
	public List<ProductVariantInventoryDTO> findProductVariantWithInventoryDetails(Integer productId);
	
	
	
	
}
