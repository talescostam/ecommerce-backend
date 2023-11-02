package com.predizetest.ecommercebackend.repository;

import com.predizetest.ecommercebackend.model.ShoppingCart;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    public Optional<ShoppingCart> findFirstBy(Sort sort);
}

