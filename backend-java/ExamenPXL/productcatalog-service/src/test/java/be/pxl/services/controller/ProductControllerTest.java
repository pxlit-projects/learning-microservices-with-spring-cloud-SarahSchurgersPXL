package be.pxl.services.controller;

import be.pxl.services.ProductCatalogServiceApplication;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.dto.ProductDto;
import be.pxl.services.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProductCatalogServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class ProductControllerTest {

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
    private ProductRepository repository;

    @BeforeAll
    public static void setUp() {
        sqlContainer.start();
    }

    @Test
    public void testAddProduct() throws Exception {
        ProductDto productDto = new ProductDto("Test", "Test Description", 10.0, Category.TUIN, null);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated());

        assertEquals(1, repository.findAllByName("Test").size());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product = new Product(1L, "Test Product", "Test Description", 10.0, null, null);
        repository.save(product);

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].price").value(10.0));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product(1L, "Test Product", "Test Description", 10.0, Category.TUIN, null);
        repository.save(product);

        ProductDto productDto = new ProductDto("Updated Product", "Updated Description", 20.0, Category.TUIN, null);

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk());

        Product updatedProduct = repository.findById(1L).get();
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(20.0, updatedProduct.getPrice());
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product(1L, "Test Product", "Test Description", 10.0, null, null);
        repository.save(product);

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product(2L, "Test Product", "Test Description", 10.0, null, null);
        repository.save(product);

        mockMvc.perform(delete("/product/2"))
                .andExpect(status().isOk());

        assertEquals(0, repository.countAllById(2L));
    }
}