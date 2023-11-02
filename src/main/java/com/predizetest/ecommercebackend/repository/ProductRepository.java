package com.predizetest.ecommercebackend.repository;

import com.predizetest.ecommercebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
