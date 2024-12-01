package be.pxl.services.controller;

import be.pxl.services.domain.Product;
import be.pxl.services.domain.ProductDto;
import be.pxl.services.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private IProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productdto) {
        productService.addProduct(productdto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        productService.updateProduct(id, productDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}