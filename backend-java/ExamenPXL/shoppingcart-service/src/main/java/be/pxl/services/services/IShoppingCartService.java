package be.pxl.services.services;

import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.dto.ShoppingCartDto;

public interface IShoppingCartService {
    ShoppingCart addProductToCart(Long cartId, Long productId, int quantity);
    ShoppingCart removeProductFromCart(Long cartId, Long productId);

    ShoppingCartDto getShoppingCart(Long cartId);
}

