package be.pxl.services.dto;

import lombok.Value;

import java.io.Serializable;
@Value
public class AddProductDto implements Serializable {
    private Long productId;
    private int quantity;


}
