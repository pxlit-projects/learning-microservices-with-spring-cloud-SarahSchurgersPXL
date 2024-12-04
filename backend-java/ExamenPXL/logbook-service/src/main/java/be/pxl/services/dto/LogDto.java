package be.pxl.services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link be.pxl.services.domain.Log}
 */
@Value
public class LogDto implements Serializable {
    @NotBlank
    Long productId;
    @NotBlank
    String message;
    @NotEmpty
    LocalDateTime timestamp;
    @NotBlank
    String user;
}