package com.cdw.gatekeeper.repositories;

import com.cdw.gatekeeper.entities.VisitorPass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for VisitorPass document
 */
@Repository
public interface VisitorPassRepository extends MongoRepository<VisitorPass, String> {
    List<VisitorPass> findByVisitingTime(LocalDate date);

    List<VisitorPass> findByResidentId(String residentId);

    List<VisitorPass> findByResidentIdAndVisitingTime(String residentId, LocalDate visitingTime);

    Optional<VisitorPass> findByEntryPass(String entryPass);
}
