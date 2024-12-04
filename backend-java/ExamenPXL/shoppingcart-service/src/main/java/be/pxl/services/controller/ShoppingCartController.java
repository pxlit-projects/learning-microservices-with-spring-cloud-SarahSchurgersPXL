package be.pxl.services.controller;

import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.dto.ShoppingCartDto;
import be.pxl.services.services.IShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingcart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final IShoppingCartService shoppingCartService;
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @PostMapping("/{cartId}/addProduct/{productId}")
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        ShoppingCart updatedCart = shoppingCartService.addProductToCart(cartId, productId, quantity);
        logger.info("received request to add product with id " + productId + " to cart with id " + cartId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/removeProduct/{productId}")
    public ResponseEntity<ShoppingCart> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        ShoppingCart updatedCart = shoppingCartService.removeProductFromCart(cartId, productId);
        logger.info("received request to remove product with id " + productId + " from cart with id " + cartId);
        return new ResponseEntity<>(updatedCart ,HttpStatus.OK);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCartDto> getShoppingCart(@PathVariable Long cartId) {
        ShoppingCartDto cart = shoppingCartService.getShoppingCart(cartId);
        logger.info("received request to get cart with id " + cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
