package com.cdw.gatekeeper.dto;


import lombok.Data;

/**
 * DTO for updating status of a user
 */
@Data
public class VerificationRequestDTO {
    private String status;
}
