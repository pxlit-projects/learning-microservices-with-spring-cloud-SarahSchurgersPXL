package be.pxl.services.services;

import be.pxl.services.client.LogClient;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ProductDto;
import be.pxl.services.dto.LogDto;
import be.pxl.services.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    @Autowired
    LogClient logClient;

    @Override
    public void addProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .category(productDto.getCategory())
                .labels(productDto.getLabels())
                .build();
        productRepository.save(product);
        LogDto logDtp = new LogDto(product.getId(), "Product added", LocalDateTime.now(), "admin");
        logClient.addLog(logDtp);

    }

    @Override
    public void updateProduct(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(productDto.getCategory());
            product.setLabels(productDto.getLabels());
            productRepository.save(product);
            LogDto logDtp = new LogDto(product.getId(), "Product updated", LocalDateTime.now(), "admin");
            logClient.addLog(logDtp);

        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new RuntimeException("Product not found");
        }
    }
}
