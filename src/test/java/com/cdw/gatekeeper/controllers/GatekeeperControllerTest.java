package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.services.gatekeeperservices.GatekeeperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;


/**
 * Suite that holds test cases for Gatekeeper Controller Operations
 */
class GatekeeperControllerTest {

    @Mock
    private GatekeeperService gatekeeperService;

    @InjectMocks
    private GatekeeperController gatekeeperController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAllVisitors() {
        String date = "2024-12-24";
        List<VisitorPass> visitorPasses = new ArrayList<>();
        visitorPasses.add(new VisitorPass());
        when(gatekeeperService.getAllVisitors(date)).thenReturn(visitorPasses);

        ResponseEntity<SuccessResponseDTO> response = gatekeeperController.listAllVisitors(date);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(visitorPasses, response.getBody().getBody());
        verify(gatekeeperService, times(1)).getAllVisitors(date);
    }

    @Test
    void testListAllVisitorsWithoutDate() {
        List<VisitorPass> visitorPasses = new ArrayList<>();
        when(gatekeeperService.getAllVisitors(null)).thenReturn(visitorPasses);

        ResponseEntity<SuccessResponseDTO> response = gatekeeperController.listAllVisitors(null);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getBody() instanceof List);
        verify(gatekeeperService, times(1)).getAllVisitors(null);
    }

    @Test
    void testVerifyVisitor() {
        String visitorId = "123";
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
        verificationRequestDTO.setStatus(Status.APPROVED.toString());
        when(gatekeeperService.verifyVisitor(visitorId, verificationRequestDTO)).thenReturn(Status.APPROVED.toString());

        ResponseEntity<SuccessResponseDTO> response = gatekeeperController.verify(visitorId, verificationRequestDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Status.APPROVED.toString(), response.getBody().getBody());
        verify(gatekeeperService, times(1)).verifyVisitor(visitorId, verificationRequestDTO);
    }
}
