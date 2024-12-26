package com.cdw.gatekeeper.services.residentservices;

import com.cdw.gatekeeper.dto.VisitorDTO;
import com.cdw.gatekeeper.entities.VisitorPass;

import java.util.List;

/**
 * Interface for Resident Operations
 */
public interface ResidentService {
    String scheduleVisitor(VisitorDTO visitorDTO);
    String removeVisitor(String visitorId);

    List<VisitorPass> getAllVisitorsByResidentIdAndDate(String residentId, String date);

    String blacklistVisitor(String visitorId);
}
