package be.pxl.services.services;

import be.pxl.services.domain.Product;
import be.pxl.services.dto.ProductDto;
import be.pxl.services.dto.ProductResponseDto;

import java.util.List;

public interface IProductService {
    void addProduct(ProductDto dto);
    void updateProduct(Long id, ProductDto dto);
    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    void deleteProduct(Long id);
}
