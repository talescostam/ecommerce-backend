package com.predizetest.ecommercebackend.controller;

import com.predizetest.ecommercebackend.exception.InvalidQuantityException;
import com.predizetest.ecommercebackend.model.ShoppingCart;
import com.predizetest.ecommercebackend.model.ShoppingCartProductsDTO;
import com.predizetest.ecommercebackend.model.ShoppingCartProducts;
import com.predizetest.ecommercebackend.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/listProducts/{carrinhoId}")
    public ResponseEntity<List<ShoppingCartProducts>> listaProdutosDoCarrinho(@PathVariable Long carrinhoId) {
        return ResponseEntity.ok(shoppingCartService.getShoppingCartProducts(carrinhoId));
    }

    @PostMapping("/create")
    public ResponseEntity<ShoppingCart> criarCarrinhoDeCompras() {
        ShoppingCart carrinho = shoppingCartService.criarCarrinhoDeCompras();
        return ResponseEntity.ok(carrinho);
    }

    @GetMapping("/{carrinhoId}")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long carrinhoId) {
        ShoppingCart carrinho = shoppingCartService.getShoppingCartById(carrinhoId);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/addToCart", consumes = "application/json")
    public ResponseEntity<ShoppingCartProducts> addProductToShoppingCart(
            @RequestBody ShoppingCartProductsDTO shoppingCartProductsDTO) throws InvalidQuantityException {
        ShoppingCartProducts addProductEntry = shoppingCartService.adicionarAoCarrinho(
                shoppingCartProductsDTO.getCarrinhoId(),
                shoppingCartProductsDTO.getProdutoId()
        );

        if (addProductEntry != null) {
            return ResponseEntity.ok(addProductEntry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{productEntryId}/updateQuantity")
    public ResponseEntity<ShoppingCartProducts> addProductQuantity(
            @PathVariable Long productEntryId,
            @RequestParam int quantityToAdd) throws InvalidQuantityException {
        ShoppingCartProducts updatedProductEntry = shoppingCartService.updateProductQuantity(productEntryId, quantityToAdd);

        if (updatedProductEntry != null) {
            return ResponseEntity.ok(updatedProductEntry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{productEntryId}/remove")
    public ResponseEntity<ShoppingCartProducts> removeProductQuantity(
            @PathVariable Long productEntryId,
            @RequestParam int quantityToRemove) throws InvalidQuantityException {
        ShoppingCartProducts updatedProductEntry = shoppingCartService.updateProductQuantity(productEntryId, -quantityToRemove);

        if (updatedProductEntry != null) {
            return ResponseEntity.ok(updatedProductEntry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{carrinhoId}/updateStatus")
    public ResponseEntity<ShoppingCart> updateStatus(@PathVariable Long carrinhoId) {
        ShoppingCart carrinho = shoppingCartService.getShoppingCartById(carrinhoId);

        if (carrinho != null) {
            carrinho.setStatus(1);
            shoppingCartService.saveShoppingCart(carrinho);
            return ResponseEntity.ok(carrinho);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{carrinhoId}/totalValue")
    public ResponseEntity<Double> getValorTotalCarrinho(@PathVariable Long carrinhoId) {
        ShoppingCart carrinho = shoppingCartService.getShoppingCartById(carrinhoId);
        if (carrinho != null) {
            double valorTotal = shoppingCartService.calcularValorTotal(carrinho);
            return ResponseEntity.ok(valorTotal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{carrinhoId}/removeProduct/{productEntryId}")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable Long carrinhoId,
            @PathVariable Long productEntryId) {
        try {
            shoppingCartService.removeProductFromCart(carrinhoId, productEntryId);
            return ResponseEntity.noContent().build();
        } catch (InvalidQuantityException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
