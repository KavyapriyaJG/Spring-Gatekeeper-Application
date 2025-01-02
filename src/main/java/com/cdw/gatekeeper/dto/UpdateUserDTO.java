package com.cdw.gatekeeper.dto;

import lombok.Data;

/**
 * DTO for updating a user
 */
@Data
public class UpdateUserDTO {
    String firstName;
    String lastName;
    String dob;
    String gender;
    String emailAddress;
}
