package com.cdw.gatekeeper.services.residentservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.VisitorDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.mappers.VisitorPassDTOMapper;
import com.cdw.gatekeeper.repositories.UserRepository;
import com.cdw.gatekeeper.repositories.VisitorPassRepository;
import com.cdw.gatekeeper.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * Class that holds Resident Operations
 */
@Service
public class ResidentServiceImpl implements ResidentService {

    private final UserRepository userRepository;
    private final VisitorPassRepository visitorPassRepository;
    private final VisitorPassDTOMapper visitorPassDTOMapper;

    @Autowired
    public ResidentServiceImpl(UserRepository userRepository, VisitorPassRepository visitorPassRepository, VisitorPassDTOMapper visitorPassDTOMapper) {
        this.userRepository = userRepository;
        this.visitorPassRepository = visitorPassRepository;
        this.visitorPassDTOMapper = visitorPassDTOMapper;
    }

    /**
     * Schedules an appointment (entryPass) for a visitor
     * @param visitorDTO
     * @return
     */
    @Transactional
    @Override
    public String scheduleVisitor(VisitorDTO visitorDTO) {
        userRepository.findById(visitorDTO.getResidentId())
                .orElseThrow(() -> new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK003));

        VisitorPass visitorPass = visitorPassDTOMapper.mapVisitorDTOtoVisitorPass(visitorDTO);
        visitorPassRepository.save(visitorPass);

        return "Visitor scheduled successfully !";
    }

    /**
     * Removes an appointment (entryPass) for a visitor
     * @param visitorId
     * @return
     */
    @Transactional
    @Override
    public String removeVisitor(String visitorId) {
        try {
            visitorPassRepository.deleteById(visitorId);
            return "Visitor removed successfully !";

        } catch (Exception e) {
            throw new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK003);
        }
    }

    /**
     * Fetches visitors of a mentioned resident filtered optionally by date
     * @param residentId
     * @param dateInput
     * @return
     */
    @Override
    public List<VisitorPass> getAllVisitorsByResidentIdAndDate(String residentId, String dateInput) {
        List<VisitorPass> visitorPasses;

        if(dateInput != null) {
            LocalDate date = Utilities.convertStringToLocalDate(dateInput);
            visitorPasses = visitorPassRepository.findByResidentIdAndVisitingTime(residentId, date);
        } else {
            visitorPasses = visitorPassRepository.findByResidentId(residentId);
        }

        if(visitorPasses == null || visitorPasses.isEmpty()) {
            throw new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK016);
        }
        return visitorPasses;
    }

    /**
     * Blacklists a visitor
     * @param visitorId
     * @return
     */
    @Transactional
    @Override
    public String blacklistVisitor(String visitorId) {
        try {
            VisitorPass visitorPass = visitorPassRepository.findById(visitorId).orElseThrow(()-> new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK016));

            if(visitorPass.getStatus().equals(Status.BLACKLISTED)) {
                return "Visitor already Blacklisted !";
            }

            visitorPass.setStatus(Status.BLACKLISTED);
            visitorPassRepository.save(visitorPass);
            return "Visitor Blacklisted successfully !";
        } catch (Exception e) {
            throw e;
        }
    }
}