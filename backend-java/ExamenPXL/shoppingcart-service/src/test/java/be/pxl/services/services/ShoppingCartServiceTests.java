package be.pxl.services.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.domain.ShoppingCartItem;
import be.pxl.services.dto.ShoppingCartDto;
import be.pxl.services.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ShoppingCartServiceTests {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testAddProductToCart() {
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);
        when(shoppingCartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));

        ShoppingCart result = shoppingCartService.addProductToCart(cart.getId(), 1L, 2);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(1L, result.getItems().get(0).getProductId());
        assertEquals(2, result.getItems().get(0).getQuantity());
        verify(shoppingCartRepository, times(1)).save(cart);
    }

    @Test
    public void testAddProductToCartIncreaseQuantity() {
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);
        when(shoppingCartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));

        ShoppingCart result = shoppingCartService.addProductToCart(cart.getId(), 1L, 2);
        ShoppingCart result2 = shoppingCartService.addProductToCart(cart.getId(), 1L, 2);

        assertNotNull(result2);
        assertEquals(1, result2.getItems().size());
        assertEquals(1L, result2.getItems().get(0).getProductId());
        assertEquals(4, result2.getItems().get(0).getQuantity());
        verify(shoppingCartRepository, times(2)).save(cart);
    }

    @Test
    public void testAddProductToCart_CartNotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            shoppingCartService.addProductToCart(1L, 1L, 2);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    public void testRemoveProductFromCart() {
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProductId(1L);
        cart.getItems().add(item);
        Long cartId = cart.getId();
        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart result = shoppingCartService.removeProductFromCart(cartId, 1L);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        verify(shoppingCartRepository, times(1)).save(cart);
    }

    @Test
    public void testRemoveProductFromCart_CartNotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            shoppingCartService.removeProductFromCart(1L, 1L);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    public void testCreateShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart result = shoppingCartService.createShoppingCart();

        assertNotNull(result);
        verify(shoppingCartRepository, times(1)).save(cart);
    }

    @Test
    public void testGetShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProductId(1L);
        cart.getItems().add(item);
        Product product = new Product();
        product.setId(1L);
        when(shoppingCartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(productClient.getProductById(1L)).thenReturn(product);

        ShoppingCartDto result = shoppingCartService.getShoppingCart(cart.getId());

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(1L, result.getItems().get(0).getProduct().getId());
    }

    @Test
    public void testGetShoppingCart_CartNotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            shoppingCartService.getShoppingCart(1L);
        });

        assertEquals("Cart not found", exception.getMessage());
    }
}