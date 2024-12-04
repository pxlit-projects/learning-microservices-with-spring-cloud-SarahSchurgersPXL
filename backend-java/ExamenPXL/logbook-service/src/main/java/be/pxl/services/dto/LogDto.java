package be.pxl.services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link be.pxl.services.domain.Log}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDto  implements Serializable {

    private Long productId;

    private String message;

    private LocalDateTime timestamp;

    private String user;
}