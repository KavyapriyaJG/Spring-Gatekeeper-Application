package com.cdw.gatekeeper.services.gatekeeperservices;

import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.VisitorPass;

import java.util.List;


/**
 * Interface for Gatekeeper Operations
 */
public interface GatekeeperService {
    List<VisitorPass> getAllVisitors(String date);

    String verifyVisitor(String id, VerificationRequestDTO verificationRequestDTO);
}
