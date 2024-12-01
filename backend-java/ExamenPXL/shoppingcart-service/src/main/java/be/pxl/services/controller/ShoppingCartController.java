package be.pxl.services.controller;

import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.services.IShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingcart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final IShoppingCartService shoppingCartService;

    @PostMapping("/{cartId}/addProduct/{productId}")
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        ShoppingCart updatedCart = shoppingCartService.addProductToCart(cartId, productId, quantity);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/removeProduct/{productId}")
    public ResponseEntity<ShoppingCart> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        ShoppingCart updatedCart = shoppingCartService.removeProductFromCart(cartId, productId);
        return new ResponseEntity<>(updatedCart ,HttpStatus.OK);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long cartId) {
        ShoppingCart cart = shoppingCartService.getShoppingCart(cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
