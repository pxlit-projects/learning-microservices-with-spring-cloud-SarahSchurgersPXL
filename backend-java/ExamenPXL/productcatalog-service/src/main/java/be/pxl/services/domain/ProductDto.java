package be.pxl.services.domain;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Product}
 */
@Value
public class ProductDto implements Serializable {
    String name;
    String description;
    double price;
    Category category;
    List<String> labels;
}