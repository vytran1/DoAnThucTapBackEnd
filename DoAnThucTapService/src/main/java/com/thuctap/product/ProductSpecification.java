package com.thuctap.product;

import org.springframework.data.jpa.domain.Specification;

import com.thuctap.common.product_variant.ProductVariant;

public class ProductSpecification {
	
	public static Specification<ProductVariant> hasCategory(Integer categoryId){
		return (root,query,cb) -> {
			if(categoryId == null) return null;
			return cb.equal(
				root.get("product")
					.get("brandCategory")
					.get("category")
					.get("id"),
					categoryId
			);
		};
	}
	
	public static Specification<ProductVariant> hasName(String name){
		return (root,query,cb) -> {
			if(name == null || name.isEmpty()) return cb.conjunction();
			return cb.like(cb.lower(root.get("nameOverride")),"%" + name.toLowerCase() + "%");
		};
	}
	
}
