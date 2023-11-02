package com.predizetest.ecommercebackend.service;

import com.predizetest.ecommercebackend.exception.InvalidQuantityException;
import com.predizetest.ecommercebackend.exception.ProductNotFoundException;
import com.predizetest.ecommercebackend.exception.ShoppingCartNotFoundException;
import com.predizetest.ecommercebackend.model.Product;
import com.predizetest.ecommercebackend.model.ShoppingCart;
import com.predizetest.ecommercebackend.model.ShoppingCartProducts;
import com.predizetest.ecommercebackend.repository.ShoppingCartProductRepository;
import com.predizetest.ecommercebackend.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final ProductService productService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               ShoppingCartProductRepository shoppingCartProductRepository,
                               ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.productService = productService;
    }

    public ShoppingCart criarCarrinhoDeCompras() {
        Sort sort = Sort.by("id").descending();
        Optional<ShoppingCart> findFirst = shoppingCartRepository.findFirstBy(sort);
        if(findFirst.isEmpty()){
            ShoppingCart carrinho = new ShoppingCart();
            carrinho.setDataCriacao(LocalDateTime.now());
            return shoppingCartRepository.save(carrinho);
        }

        return findFirst.get();

    }

    public ShoppingCartProducts adicionarAoCarrinho(Long carrinhoId, Long produtoId) throws InvalidQuantityException {
        if (carrinhoId == null) {
            throw new ShoppingCartNotFoundException("Carrinho de compras não encontado.");
        }

        Product produto = productService.getProductById(produtoId);

        if (produto == null) {
            throw new ProductNotFoundException("Produto não encontrado.");
        }

        ShoppingCartProducts productEntry = new ShoppingCartProducts(carrinhoId, produto, 1);

        List<ShoppingCartProducts> list = shoppingCartProductRepository.findByCarrinhoId(carrinhoId);
        Optional<ShoppingCartProducts> checkProduct = list.stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst();

        if(checkProduct.isPresent()) {
            ShoppingCartProducts cartProduct = checkProduct.get();
            productEntry.setId(cartProduct.getId());
            productEntry.setQuantidade_produto_adicionada(cartProduct.getQuantidade_produto_adicionada() + 1);
        }

        return shoppingCartProductRepository.save(productEntry);
    }

    public List<ShoppingCartProducts> getShoppingCartProducts(Long carrinhoId) {
        return shoppingCartProductRepository.findByCarrinhoId(carrinhoId);
    }

    public double calcularValorTotal(ShoppingCart carrinho) {
        List<ShoppingCartProducts> produtosNoCarrinho = shoppingCartProductRepository.findByCarrinhoId(carrinho.getId());
        double valorTotal = 0.0;

        for (ShoppingCartProducts item : produtosNoCarrinho) {
            Product produto = item.getProduto();
            int quantidade = item.getQuantidade_produto_adicionada();
            valorTotal += produto.getPreco() * quantidade;
        }

        return valorTotal;
    }

    public ShoppingCart getShoppingCartById(Long carrinhoId) {
        Optional<ShoppingCart> carrinho = shoppingCartRepository.findById(carrinhoId);
        return carrinho.orElse(null);
    }

    public ShoppingCartProducts updateProductQuantity(Long productEntryId, int newQuantity) throws InvalidQuantityException {
        ShoppingCartProducts productEntry = shoppingCartProductRepository.findById(productEntryId).orElse(null);

        if (productEntry == null) {
            return null;
        }

        if (newQuantity <= 0 || newQuantity > productEntry.getProduto().getQuantidade()) {
            throw new InvalidQuantityException("Quantidade inválida ou excede o estoque disponível.");
        }

        productEntry.setQuantidade_produto_adicionada(newQuantity);

        return shoppingCartProductRepository.save(productEntry);
    }

    public void removeProductFromCart(Long carrinhoId, Long productEntryId)
            throws InvalidQuantityException, ProductNotFoundException, ShoppingCartNotFoundException {
        ShoppingCart carrinho = shoppingCartRepository.findById(carrinhoId).orElse(null);
        if (carrinho == null) {
            throw new ShoppingCartNotFoundException("Carrinho de compras não encontrado");
        }

        ShoppingCartProducts productEntry = shoppingCartProductRepository.findById(productEntryId).orElse(null);
        if (productEntry == null) {
            throw new ProductNotFoundException("Entrada de produto não encontrada");
        }

        shoppingCartProductRepository.delete(productEntry);

        shoppingCartRepository.save(carrinho);
    }

    public ShoppingCart saveShoppingCart(ShoppingCart carrinho) {
        return shoppingCartRepository.save(carrinho);
    }

}
