package com.cdw.gatekeeper.dto;

import lombok.Data;

/**
 * DTO for creating an entryPass
 */
@Data
public class VisitorDTO {
    private String residentId;
    private String visitingTime;
}
