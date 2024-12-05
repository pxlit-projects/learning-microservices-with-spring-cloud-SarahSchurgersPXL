package be.pxl.services.controller;

import be.pxl.services.domain.Product;
import be.pxl.services.dto.ProductDto;
import be.pxl.services.dto.ProductResponseDto;
import be.pxl.services.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody @Valid ProductDto productdto) {
        logger.info("received request to add product");
        productService.addProduct(productdto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto productDto) {
        logger.info("received request to update product with id: " + id);
        productService.updateProduct(id, productDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        logger.info("received request to get all products");
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        logger.info("received request to get product with id: " + id);
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("received request to delete product with id: " + id);
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
