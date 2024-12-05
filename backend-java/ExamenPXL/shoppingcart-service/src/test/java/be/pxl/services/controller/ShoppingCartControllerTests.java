package be.pxl.services.controller;

import be.pxl.services.ShoppingCartServiceApplication;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.domain.ShoppingCartItem;
import be.pxl.services.dto.AddProductDto;
import be.pxl.services.dto.DeleteProductDto;
import be.pxl.services.repository.ShoppingCartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShoppingCartServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class ShoppingCartControllerTests {

    private ShoppingCart cart;

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static MySQLContainer sqlContainer = new MySQLContainer("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingCartRepository repository;

    @BeforeAll
    public static void setUp() {
        sqlContainer.start();
    }

    @BeforeEach
    public void startUp(){
        cart = new ShoppingCart();
        repository.save(cart);
    }

    @AfterEach
    public void tearDown(){
        repository.deleteAll();
    }


    @Test
    public void testAddProductToCart() throws Exception {
        ShoppingCart cart = repository.findById(1L).orElseThrow(() -> new RuntimeException("Cart not found"));
        AddProductDto addProductDto = new AddProductDto(1L, 2);
        Long cartId = cart.getId();
        mockMvc.perform(post("/shoppingcart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductDto)))
                .andExpect(status().isOk());

        // Verify product is added to the cart
        ShoppingCart updatedCart = repository.findById(cartId).get();
        assertEquals(1, updatedCart.getItems().size());
        assertEquals(1L, updatedCart.getItems().get(0).getProductId());
        assertEquals(2, updatedCart.getItems().get(0).getQuantity());
    }

    @Test
    public void testRemoveProductFromCart() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setId(1L);

        cart.getItems().add(item);
        repository.save(cart);

        DeleteProductDto deleteProductDto = new DeleteProductDto(1L);
        Long cartId = cart.getId();
        mockMvc.perform(delete("/shoppingcart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteProductDto)))
                .andExpect(status().isOk());

        // Verify product is removed from the cart
        ShoppingCart updatedCart = repository.findById(cartId).get();
        assertEquals(0, updatedCart.getItems().size());
    }

    @Test
    public void testGetShoppingCart() throws Exception {
        ShoppingCart cart = repository.findById(1L).orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(10.0);

        ShoppingCartItem item = new ShoppingCartItem();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setId(1L);
        item.setProduct(product);

        cart.getItems().add(item);
        repository.save(cart);
        Long cartId = cart.getId();
        mockMvc.perform(get("/shoppingcart/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateNewCart() throws Exception {
        repository.deleteAll();

        mockMvc.perform(post("/shoppingcart"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());

        // Check that a cart is created in the repository
        assertEquals(1, repository.findAll().size());
    }
}
