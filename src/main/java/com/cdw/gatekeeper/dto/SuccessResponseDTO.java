package com.cdw.gatekeeper.dto;
import lombok.Data;

/**
 * DTO for success responses
 */
@Data
public class SuccessResponseDTO {
    private boolean success;
    private Object body;
}
