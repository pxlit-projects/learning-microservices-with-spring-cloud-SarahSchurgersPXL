package be.pxl.services.dto;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link be.pxl.services.domain.Product}
 */
@Value
public class ProductResponseDto implements Serializable {
    Long id;
    String name;
    String description;
    double price;
    Category category;
    List<String> labels;

    public static List<ProductResponseDto> from(List<Product> products) {
        return products.stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getLabels()
                ))
                .collect(Collectors.toList());
    }

    public static ProductResponseDto from(Product product) {
        return product == null ? null : new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getLabels()
        );
    }

}