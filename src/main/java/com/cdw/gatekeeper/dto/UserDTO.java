package com.cdw.gatekeeper.dto;

import lombok.Data;

/**
 * DTO for creating a user
 */
@Data
public class UserDTO {
    String firstName;
    String lastName;
    String dob;
    String gender;
    String emailAddress;
    String role;
    String password;
}
