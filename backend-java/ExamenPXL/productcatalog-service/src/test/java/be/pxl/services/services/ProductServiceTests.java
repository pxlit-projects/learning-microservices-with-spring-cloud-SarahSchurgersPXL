package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.dto.LogDto;
import be.pxl.services.dto.ProductDto;
import be.pxl.services.dto.ProductResponseDto;
import be.pxl.services.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProductService productService;

    @Captor
    private ArgumentCaptor<LogDto> logDtoArgumentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addProduct() {
        List<String> labels = new ArrayList<>();
        ProductDto productDto = new ProductDto("product", "description", 10.0, Category.TUIN, labels);
        Product product = Product.builder()
                .name("product")
                .description("description")
                .price(10.0)
                .category(Category.TUIN)
                .labels(labels)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.addProduct(productDto);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("LogbookQueue"), any(LogDto.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        List<String> labels = new ArrayList<>();
        labels.add("label");
        Product product = Product.builder()
                .id(1L)
                .name("product")
                .description("description")
                .price(10.0)
                .category(Category.TUIN)
                .labels(labels)
                .build();
        List<String> labels2 = new ArrayList<>();
        labels2.add("label2");
        ProductDto productDto = new ProductDto("updated product", "updated description", 20.0, Category.VERZORGING, labels2);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProduct(1L, productDto);

        verify(productRepository, times(1)).save(product);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("LogbookQueue"), any(LogDto.class));
        assertEquals("updated product", product.getName());
        assertEquals("updated description", product.getDescription());
        assertEquals(20.0, product.getPrice());
        assertEquals(Category.VERZORGING, product.getCategory());
        assertEquals(labels2, product.getLabels());
    }

    @Test
    public void testUpdateProductNoChanges() {
        List<String> labels = new ArrayList<>();
        labels.add("label");
        Product product = Product.builder()
                .id(1L)
                .name("product")
                .description("description")
                .price(10.0)
                .category(Category.TUIN)
                .labels(labels)
                .build();

        ProductDto productDto = new ProductDto("product", "description", 10.0, Category.TUIN, labels);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProduct(1L, productDto);

        verify(productRepository, never()).save(product);
        verify(rabbitTemplate, never()).convertAndSend(eq("LogbookQueue"), any(LogDto.class));
    }

    @Test
    public void testUpdateProductNotFound() {
        ProductDto productDto = new ProductDto("updated product", "updated description", 20.0, Category.TUIN, null);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1L, productDto);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product(1L, "product1", "description1", 10.0, Category.TUIN, null);
        Product product2 = new Product(2L, "product2", "description2", 20.0, Category.TUIN, null);
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponseDto> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("product1", result.get(0).getName());
        assertEquals("product2", result.get(1).getName());
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1L, "product", "description", 10.0, Category.TUIN, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("product", result.getName());
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product(1L, "product", "description", 10.0, Category.TUIN, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("LogbookQueue"), any(LogDto.class));
    }

    @Test
    public void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Product not found", exception.getMessage());
    }
}