package be.pxl.services.dto;


import lombok.*;


import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDtoMessage implements Serializable {

    private Long productId;

    private String message;

    private LocalDateTime timestamp;

    private String user;
}
