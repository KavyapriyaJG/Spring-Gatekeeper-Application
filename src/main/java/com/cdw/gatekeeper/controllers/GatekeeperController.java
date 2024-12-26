package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.services.gatekeeperservices.GatekeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * RestController for handling Gatekeeper operations
 */
@RestController
@RequestMapping("/gatekeeper")
public class GatekeeperController {

    private final GatekeeperService gatekeeperService;

    @Autowired
    public GatekeeperController(GatekeeperService gatekeeperService) {
        this.gatekeeperService = gatekeeperService;
    }

    /**
     * Fetches list of visitors optionally filtered by date
     * @param date
     * @return
     */
    @GetMapping("/visitors")
    public ResponseEntity<SuccessResponseDTO> listAllVisitors(@RequestParam(value= "date", required = false) String date) {

        List<VisitorPass> visitorPasses = gatekeeperService.getAllVisitors(date);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setHttpStatus(HttpStatus.OK);
        successResponseDTO.setBody(visitorPasses);

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Modifies a user's status
     * @param id
     * @param verificationRequestDTO
     * @return
     */
    @PutMapping("/visitors/{id}/status")
    public ResponseEntity<SuccessResponseDTO> verify(@PathVariable String id, @RequestBody VerificationRequestDTO verificationRequestDTO) {
        String verificationStatus = gatekeeperService.verifyVisitor(id, verificationRequestDTO);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setHttpStatus(HttpStatus.OK);
        successResponseDTO.setBody(verificationStatus);

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
