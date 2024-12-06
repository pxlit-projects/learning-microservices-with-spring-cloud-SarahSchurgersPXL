package be.pxl.services.services;

import be.pxl.services.domain.Product;
import be.pxl.services.dto.ProductDto;
import be.pxl.services.dto.LogDto;
import be.pxl.services.dto.ProductResponseDto;
import be.pxl.services.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Transactional
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
        rabbitTemplate.convertAndSend("LogbookQueue", logDtp);
        logger.info("added product: " + product.getName() + " with id: " + product.getId());
    }
    @Transactional
    @Override
    public void updateProduct(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            boolean isUpdated = false;

            if (!product.getName().equals(productDto.getName()) && productDto.getName() != null) {
                product.setName(productDto.getName());
                isUpdated = true;
            }
            if (!product.getDescription().equals(productDto.getDescription()) && productDto.getDescription() != null) {
                product.setDescription(productDto.getDescription());
                isUpdated = true;
            }
            if (product.getPrice() != productDto.getPrice() && productDto.getPrice() != 0) {
                product.setPrice(productDto.getPrice());
                isUpdated = true;
            }
            if (!product.getCategory().equals(productDto.getCategory()) && productDto.getCategory() != null) {
                product.setCategory(productDto.getCategory());
                isUpdated = true;
            }
            if (productDto.getLabels() != null && !product.getLabels().equals(productDto.getLabels())) {
                product.setLabels(productDto.getLabels());
                isUpdated = true;
            }

            if (isUpdated) {
                productRepository.save(product);
                LogDto logDtp = new LogDto(product.getId(), "Product updated", LocalDateTime.now(), "admin");
                rabbitTemplate.convertAndSend("LogbookQueue", logDtp);
                logger.info("updated product: " + product.getName() + " with id: " + product.getId());
            } else {
                logger.info("no changes detected for product with id: " + product.getId());
            }
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        logger.info("retrieved all products");
        List<Product> products = productRepository.findAll();
        return ProductResponseDto.from(products);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            logger.info("retrieved product with id: " + id);
            return ProductResponseDto.from(optionalProduct.get());
        } else {
            logger.info("product with id: " + id + " not found");
            throw new RuntimeException("Product not found");
        }
    }
    @Transactional
    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
            LogDto logDtp = new LogDto(id, "Product deleted", LocalDateTime.now(), "admin");
            rabbitTemplate.convertAndSend("LogbookQueue", logDtp);
            logger.info("deleted product with id: " + id);
        } else {
            logger.info("product with id: " + id + " not found");
            throw new RuntimeException("Product not found");
        }
    }
}
