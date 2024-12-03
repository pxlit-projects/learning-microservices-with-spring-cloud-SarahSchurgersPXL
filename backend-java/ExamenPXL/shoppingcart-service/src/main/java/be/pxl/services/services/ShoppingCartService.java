package be.pxl.services.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService implements IShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    ProductClient productClient;

    @Override
    public ShoppingCart addProductToCart(Long cartId, Long productId, int quantity) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            ShoppingCart cart = optionalCart.get();
            Product product = productClient.getProductById(productId);
            if(product == null) {
                throw new RuntimeException("Product not found");
            }
            cart.addProduct(product, quantity);
            return shoppingCartRepository.save(cart);
        }
        else {
            throw new RuntimeException("Cart not found");
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
