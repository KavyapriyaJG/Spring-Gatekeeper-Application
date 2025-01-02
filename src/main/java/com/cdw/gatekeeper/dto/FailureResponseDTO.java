package com.cdw.gatekeeper.dto;

import lombok.Data;

/**
 * DTO for failure responses
 */
@Data
public class FailureResponseDTO {
    private boolean success;
    private String message;
}
