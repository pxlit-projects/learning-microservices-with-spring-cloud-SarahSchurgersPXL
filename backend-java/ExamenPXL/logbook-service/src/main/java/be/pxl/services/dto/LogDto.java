package be.pxl.services.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link be.pxl.services.domain.Log}
 */
@Value
public class LogDto implements Serializable {
    Long productId;
    String message;
    LocalDateTime timestamp;
    String user;
}