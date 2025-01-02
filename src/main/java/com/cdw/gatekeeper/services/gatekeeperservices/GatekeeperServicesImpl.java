package com.cdw.gatekeeper.services.gatekeeperservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.repositories.VisitorPassRepository;
import com.cdw.gatekeeper.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Class that holds Gatekeeper Operations
 */
@Service
public class GatekeeperServicesImpl implements  GatekeeperService{
    private final VisitorPassRepository visitorPassRepository;

    @Autowired
    public GatekeeperServicesImpl(VisitorPassRepository visitorPassRepository) {
        this.visitorPassRepository = visitorPassRepository;
    }

    /**
     * Fetches list of visitor optionally filtered by date
     * @param dateInput
     * @return
     */
    @Override
    public List<VisitorPass> getAllVisitors(String dateInput) {
        try {
            if(dateInput!= null) {
                LocalDate date = Utilities.convertStringToLocalDate(dateInput);
                return visitorPassRepository.findByVisitingTime(date);
            }
            return visitorPassRepository.findAll();
        } catch(Exception e) {
            throw new GatekeeperBusinessException(HttpStatus.CONFLICT, GKExceptionConstants.GK011);
        }
    }

    /**
     * Modifies the status of the visitor
     * @param visitorId
     * @param verificationRequestDTO
     * @return
     */
    @Transactional
    @Override
    public String verifyVisitor(String visitorId, VerificationRequestDTO verificationRequestDTO) {
        String response;
        VisitorPass visitorPass = visitorPassRepository.findById(visitorId)
                .orElseThrow(() -> new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK003));

        if(visitorPass.getStatus() == Status.BLACKLISTED) {
            throw new GatekeeperBusinessException(HttpStatus.BAD_REQUEST, GKExceptionConstants.GK014);
        }

        switch (verificationRequestDTO.getStatus().toUpperCase()) {
            case "APPROVED" -> {
                visitorPass.setStatus(Status.APPROVED);
                response = "Approved Successfully !";
            }
            case "REJECTED" -> {
                visitorPass.setStatus(Status.REJECTED);
                response = "Rejected Successfully !";
            }
            case "BLACKLISTED" -> {
                visitorPass.setStatus(Status.BLACKLISTED);
                response = "Blacklisted Successfully !";
            }
            default -> throw new GatekeeperBusinessException(HttpStatus.BAD_REQUEST, GKExceptionConstants.GK009);
        }

        visitorPassRepository.save(visitorPass);

        return response;
    }
}
