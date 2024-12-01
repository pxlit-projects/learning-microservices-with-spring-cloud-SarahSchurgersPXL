package be.pxl.services.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;


@Value
public class LogDto implements Serializable {
    Long productId;
    String message;
    LocalDateTime timestamp;
    String user;
}