package com.cdw.gatekeeper.entities;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * VisitorPass entity encapsulates the data in Visitor Entry Pass
 */
@Data
@Document(collection = "visitorPass")
public class VisitorPass {
    @Id
    private String id;

    @NonNull
    private String residentId;
    private String entryPass;
    private Status status;

    @NonNull
    private LocalDate visitingTime;

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public VisitorPass() {
    }
}
