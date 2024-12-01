package be.pxl.services.services;

import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService implements IShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart addProductToCart(Long cartId, Long productId, int quantity) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            ShoppingCart cart = optionalCart.get();
            // Add product to cart
            return shoppingCartRepository.save(cart);
        }
        else {
            throw new RuntimeException("Cart or Product not found");
        }

    }

    @Override
    public ShoppingCart removeProductFromCart(Long cartId, Long productId) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);

        if (optionalCart.isPresent()) {
            ShoppingCart cart = optionalCart.get();
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            return shoppingCartRepository.save(cart);
        } else {
            throw new RuntimeException("Cart not found");
        }
    }

    @Override
    public ShoppingCart getShoppingCart(Long cartId) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            throw new RuntimeException("Cart not found");
        }
    }
}
