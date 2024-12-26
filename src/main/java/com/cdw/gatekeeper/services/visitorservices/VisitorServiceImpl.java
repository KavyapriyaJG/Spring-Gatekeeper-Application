package com.cdw.gatekeeper.services.visitorservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.repositories.VisitorPassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


/**
 * Class that holds Visitor Operations
 */
@Service
public class VisitorServiceImpl implements VisitorService {

    private final VisitorPassRepository visitorPassRepository;

    @Autowired
    public VisitorServiceImpl(VisitorPassRepository visitorPassRepository) {
        this.visitorPassRepository = visitorPassRepository;
    }

    @Override
    public String checkStatus(String entryPass) {
        VisitorPass visitorPass = visitorPassRepository.findByEntryPass(entryPass);

        if(visitorPass == null) {
            throw new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK012);
        }

        return "This visitor pass is in status : " + visitorPass.getStatus().toString();
    }
}
