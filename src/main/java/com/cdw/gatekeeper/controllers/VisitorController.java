package com.cdw.gatekeeper.controllers;


import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.services.visitorservices.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RestController for handling Visitor operations
 */
@RestController
@RequestMapping("/visitor")
public class VisitorController {

    private final VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * Checks the status of an entryPass by its entryPassKey string
     * @param entryPass
     * @return
     */
    @GetMapping("/{entryPass}")
    public ResponseEntity<SuccessResponseDTO> checkStatus(@PathVariable("entryPass") String entryPass) {

        String status = visitorService.checkStatus(entryPass);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setHttpStatus(HttpStatus.OK);
        successResponseDTO.setBody(status);

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
