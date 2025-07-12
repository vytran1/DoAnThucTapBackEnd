package com.thuctap.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.product.Product;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
