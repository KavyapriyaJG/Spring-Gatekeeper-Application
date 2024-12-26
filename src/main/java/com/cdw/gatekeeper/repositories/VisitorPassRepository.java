package com.cdw.gatekeeper.repositories;

import com.cdw.gatekeeper.entities.VisitorPass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for VisitorPass document
 */
@Repository
public interface VisitorPassRepository extends MongoRepository<VisitorPass, String> {
    List<VisitorPass> findByVisitingTime(LocalDate date);

    List<VisitorPass> findByResidentId(String residentId);

    List<VisitorPass> findByResidentIdAndVisitingTime(String residentId, LocalDate visitingTime);

    VisitorPass findByEntryPass(String entryPass);
}
