package com.predizetest.ecommercebackend.repository;

import com.predizetest.ecommercebackend.model.ShoppingCart;
import com.predizetest.ecommercebackend.model.ShoppingCartProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProducts, Long> {
    List<ShoppingCartProducts> findByCarrinhoId(Long carrinho);
}
