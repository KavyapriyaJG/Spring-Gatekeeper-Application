package com.cdw.gatekeeper.mappers;

import com.cdw.gatekeeper.dto.VisitorDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.utils.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * Class that holds VisitorDTO mapper functions
 */
@Component
@RequiredArgsConstructor
public class VisitorPassDTOMapper {

    /**
     * Maps the visitorDTO on to the visitorPass entity
     * @param visitorDTO
     * @return
     */
    public VisitorPass mapVisitorDTOtoVisitorPass(VisitorDTO visitorDTO) {
        VisitorPass visitorPass = new VisitorPass();

        visitorPass.setResidentId(visitorDTO.getResidentId());

        // Generate a unique entry pass based on the entry timestamp
        String entryPass = "ENTRY-" + System.currentTimeMillis();
        visitorPass.setEntryPass(entryPass);
        visitorPass.setStatus(Status.PENDING);
        visitorPass.setVisitingTime(Utilities.convertStringToLocalDate(visitorDTO.getVisitingTime()));

        visitorPass.setCreatedDateTime(LocalDateTime.now());
        visitorPass.setUpdatedDateTime(LocalDateTime.now());

        return visitorPass;
    }
}
