package com.predizetest.ecommercebackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carrinho_produtos")
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long carrinhoId;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Product produto;

    @NotNull
    private int quantidade_produto_adicionada;

    public ShoppingCartProducts(Long carrinhoId, Product produto, int quantidade) {
        this.carrinhoId = carrinhoId;
        this.produto = produto;
        this.quantidade_produto_adicionada = quantidade;
    }

}

