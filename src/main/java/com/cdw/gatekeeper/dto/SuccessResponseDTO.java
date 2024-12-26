package com.cdw.gatekeeper.dto;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * DTO for success responses
 */
@Data
public class SuccessResponseDTO {
    private HttpStatus httpStatus;
    private Object body;
}
