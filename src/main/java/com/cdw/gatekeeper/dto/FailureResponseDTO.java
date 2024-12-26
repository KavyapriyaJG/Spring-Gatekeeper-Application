package com.cdw.gatekeeper.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * DTO for failure responses
 */
@Data
public class FailureResponseDTO {
    private HttpStatus httpStatus;
    private String message;
}
