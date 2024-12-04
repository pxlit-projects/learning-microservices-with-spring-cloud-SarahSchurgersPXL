package be.pxl.services.dto;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Product}
 */
@Value
public class ProductDto implements Serializable {
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    @Positive
    double price;
    @NotNull
    Category category;
    List<String> labels;
}