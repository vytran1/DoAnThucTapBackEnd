package com.thuctap.product_attribute;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.product_attribute.ProductAttribute;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Integer> {

}
