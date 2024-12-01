package be.pxl.services.services;

import be.pxl.services.domain.ShoppingCart;

public interface IShoppingCartService {
    ShoppingCart addProductToCart(Long cartId, Long productId, int quantity);
    ShoppingCart removeProductFromCart(Long cartId, Long productId);

    ShoppingCart getShoppingCart(Long cartId);
}

