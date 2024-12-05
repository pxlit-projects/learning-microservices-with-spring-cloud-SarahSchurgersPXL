package be.pxl.services.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.domain.ShoppingCartItem;
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
    private final ProductClient productClient;

    @Override
    public ShoppingCart addProductToCart(Long cartId, Long productId, int quantity) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (!optionalCart.isPresent()) {
            logger.info("cart with id " + cartId + " not found");
            throw new RuntimeException("Cart not found");
        }

        ShoppingCart cart = optionalCart.get();
        Optional<ShoppingCartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            ShoppingCartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            logger.info("quantity of product with id " + productId + " increased in cart with id " + cartId);
        } else {
            cart.getItems().add(ShoppingCartItem.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .shoppingCart(cart)
                    .build());
            logger.info("product with id " + productId + " added to cart with id " + cartId);
        }

        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart removeProductFromCart(Long cartId, Long productId) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            ShoppingCart cart = optionalCart.get();
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
            logger.info("product with id " + productId + " removed from cart with id " + cartId);
            return shoppingCartRepository.save(cart);
        } else {
            logger.info("cart with id " + cartId + " not found");
            throw new RuntimeException("Cart not found");
        }
    }

    @Override
    public ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        logger.info("new shoppingcart created");
        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto getShoppingCart(Long cartId) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            logger.info("cart with id " + cartId + " found");
            ShoppingCart cart = optionalCart.get();
            cart.getItems().forEach(item -> {
                Product product = productClient.getProductById(item.getProductId());
                item.setProduct(product);
            });
            return new ShoppingCartDto(cart);
        } else {
            logger.info("cart with id " + cartId + " not found");
            throw new RuntimeException("Cart not found");
        }
    }


}
