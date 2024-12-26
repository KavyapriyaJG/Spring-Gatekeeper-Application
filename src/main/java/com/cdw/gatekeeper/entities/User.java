package com.cdw.gatekeeper.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User entity that encapsulates the necessary data of an actor in gatekeeper application
 */
@Data
@Document(collection = "user")
public class User {
    @Id
    private String id;

    @NonNull
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    @NonNull
    private String emailAddress;

    @NonNull
    private Role role;
    private Status status;
    @NonNull
    private String password;

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public User() {
    }
}
