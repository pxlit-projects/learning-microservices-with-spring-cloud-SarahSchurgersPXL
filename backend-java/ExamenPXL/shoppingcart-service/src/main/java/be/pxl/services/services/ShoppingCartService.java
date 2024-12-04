package be.pxl.services.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.dto.ShoppingCartDto;
import be.pxl.services.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService implements IShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    ProductClient productClient;

    @Override
    public ShoppingCart addProductToCart(Long cartId, Long productId, int quantity) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            ShoppingCart cart = optionalCart.get();
            Product product = productClient.getProductById(productId);
            if(product == null) {
                logger.info("product with id " + productId + " not found");
                throw new RuntimeException("Product not found");
            }
            cart.addProduct(product, quantity);
            logger.info("product with id " + productId + " added to cart with id " + cartId);
            return shoppingCartRepository.save(cart);
        }
        else {
            logger.info("cart with id " + cartId + " not found");
            throw new RuntimeException("Cart not found");
        }
    }

    @Override
    public ShoppingCart removeProductFromCart(Long cartId, Long productId) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            ShoppingCart cart = optionalCart.get();
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            logger.info("product with id " + productId + " removed from cart with id " + cartId);
            return shoppingCartRepository.save(cart);
        } else {
            logger.info("cart with id " + cartId + " not found");
            throw new RuntimeException("Cart not found");
        }
    }

    @Override
    public ShoppingCartDto getShoppingCart(Long cartId) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            logger.info("cart with id " + cartId + " found");
            ShoppingCartDto cart = new ShoppingCartDto(optionalCart.get());
            return cart;
        } else {
            logger.info("cart with id " + cartId + " not found");
            throw new RuntimeException("Cart not found");
        }
    }
}
