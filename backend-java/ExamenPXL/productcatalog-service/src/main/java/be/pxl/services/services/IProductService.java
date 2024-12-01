package be.pxl.services.services;

import be.pxl.services.domain.Product;
import be.pxl.services.domain.ProductDto;

import java.util.List;

public interface IProductService {
    void addProduct(ProductDto dto);
    void updateProduct(Long id, ProductDto dto);
    List<Product> getAllProducts();

    Product getProductById(Long id);
}
