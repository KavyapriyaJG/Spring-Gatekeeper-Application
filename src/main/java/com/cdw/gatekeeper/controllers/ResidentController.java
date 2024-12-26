package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.VisitorDTO;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.services.residentservices.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController for handling Resident operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/resident")
public class ResidentController {

    private final ResidentService residentService;

    /**
     * Fetches list of visitors of a mentioned visitor optionally filtered by date
     * @param residentId
     * @param date
     * @return
     */
    @GetMapping("/{id}/visitors")
    public ResponseEntity<SuccessResponseDTO> getAllVisitors(@PathVariable(name = "id") String residentId, @RequestParam(value= "date", required = false) String date) {
        List<VisitorPass> visitorPasses = residentService.getAllVisitorsByResidentIdAndDate(residentId, date);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setHttpStatus(HttpStatus.OK);
        successResponseDTO.setBody(visitorPasses);

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Schedules an appointment (entryPass) with the resident on the mentioned date
     * @param visitorDTO
     * @return
     */
    @PostMapping("/visitors")
    public ResponseEntity<SuccessResponseDTO> scheduleVisitor(@RequestBody VisitorDTO visitorDTO) {
        residentService.scheduleVisitor(visitorDTO);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setHttpStatus(HttpStatus.OK);
        successResponseDTO.setBody("Scheduled successfully !");

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Removes an appointment (entryPass) with the resident
     * @param id
     * @return
     */
    @DeleteMapping("/visitors/{id}")
    public ResponseEntity<SuccessResponseDTO> removeVisitor(@PathVariable(name = "id") String id) {
        residentService.removeVisitor(id);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setHttpStatus(HttpStatus.OK);
        successResponseDTO.setBody("Schedule cancelled successfully !");

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Blacklists a visitor
     * @param visitorId
     * @return
     */
    @PatchMapping("/visitors/{id}/blacklist")
    public ResponseEntity<SuccessResponseDTO> blacklistVisitor(@PathVariable(name = "id") String visitorId) {
        String verificationStatus = residentService.blacklistVisitor(visitorId);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setHttpStatus(HttpStatus.OK);
        successResponseDTO.setBody(verificationStatus);

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
