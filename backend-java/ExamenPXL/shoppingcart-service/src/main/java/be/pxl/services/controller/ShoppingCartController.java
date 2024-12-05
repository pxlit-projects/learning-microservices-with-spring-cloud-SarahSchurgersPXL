package be.pxl.services.controller;

import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.dto.AddProductDto;
import be.pxl.services.dto.DeleteProductDto;
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

    @PostMapping("/{cartId}")
    public ResponseEntity<Void> addProductToCart(@PathVariable int cartId, @RequestBody AddProductDto addProductDto) {
        shoppingCartService.addProductToCart((long) cartId, addProductDto.getProductId(), addProductDto.getQuantity());
        logger.info("received request to add product with id " + addProductDto.getProductId() + " to cart with id " + cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ShoppingCartDto> removeProductFromCart(@PathVariable int cartId, @RequestBody DeleteProductDto productDto) {
        shoppingCartService.removeProductFromCart((long) cartId, productDto.getProductId());
        ShoppingCartDto cart = shoppingCartService.getShoppingCart((long)cartId);
        logger.info("received request to remove product with id " + productDto.getProductId() + " from cart with id " + cartId);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCartDto> getShoppingCart(@PathVariable Long cartId) {
        ShoppingCartDto cart = shoppingCartService.getShoppingCart(cartId);
        logger.info("received request to get cart with id " + cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ShoppingCartDto> createNewCart() {
        ShoppingCart newCart = shoppingCartService.createShoppingCart();
        ShoppingCartDto cartDto = new ShoppingCartDto(newCart);
        logger.info("received request to create a new cart");
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

}
