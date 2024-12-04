package be.pxl.services.dto;

import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.domain.ShoppingCartItem;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link be.pxl.services.domain.ShoppingCart}
 */
@Value
public class ShoppingCartDto implements Serializable {
    Long id;
    List<ShoppingCartItem> items;


    public ShoppingCartDto(ShoppingCart shoppingCart) {
        this.id = shoppingCart.getId();
        this.items = shoppingCart.getItems();
    }
}