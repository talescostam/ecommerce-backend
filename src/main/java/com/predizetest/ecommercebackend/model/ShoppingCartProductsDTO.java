package com.predizetest.ecommercebackend.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShoppingCartProductsDTO {
    private Long carrinhoId;
    private Long produtoId;

    public ShoppingCartProductsDTO(Long carrinhoId, Long produtoId) {
        this.carrinhoId = carrinhoId;
        this.produtoId = produtoId;
    }

    public Long getCarrinhoId() {
        return carrinhoId;
    }

    public void setCarrinhoId(Long carrinhoId) {
        this.carrinhoId = carrinhoId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}

