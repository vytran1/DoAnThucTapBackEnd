package com.thuctap.product_variant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.product_variant.dto.ProductVariantDetailDTO;
import com.thuctap.product_variant.dto.ProductVariantInventoryDTO;
import com.thuctap.product_variant.dto.ProductVariantWithStockDTO;

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
	
	public Page<ProductVariant> findAll(Specification<ProductVariant> spec,Pageable pageable);
	
	
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
	
	
	@Query("SELECT pv FROM ProductVariant pv WHERE pv.sku = ?1")
	public Optional<ProductVariant> findProductVariantIdBySkuCode(String sku);
	
	
	@Query("""
			SELECT new com.thuctap.product_variant.dto.ProductVariantWithStockDTO(
					 pv.id,
					 p.id,
					 pv.nameOverride,
			 		 pv.sku,
			 		 p.image,
                     COALESCE(SUM(s.quantity), 0),
                     pv.price
				)
			FROM ProductVariant pv
			JOIN pv.product p 
			JOIN p.brandCategory bc
			JOIN bc.category c
			LEFT JOIN Stocking s ON s.productVariant.id = pv.id AND s.inventory.id = :inventoryId
			WHERE pv.isDelete = false
				AND (:categoryId IS NULL OR c.id = :categoryId)
				AND (:name IS NULL OR LOWER(pv.nameOverride) LIKE LOWER(CONCAT('%', :name, '%')))
			
			GROUP BY pv.id, pv.sku, pv.nameOverride, pv.price		
			""")
	public Page<ProductVariantWithStockDTO> findForSaleOfPoint(
			@Param("inventoryId") Integer inventoryId, 
			@Param("name")String name, 
			@Param("categoryId")Integer categoryId, 
			Pageable pageable);
	
	
}
