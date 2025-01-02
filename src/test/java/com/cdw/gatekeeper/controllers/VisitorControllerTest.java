package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.services.visitorservices.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;


/**
 * Suite that holds test cases for Visitor Controller Operations
 */
class VisitorControllerTest {

    @Mock
    private VisitorService visitorService;

    @InjectMocks
    private VisitorController visitorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckStatus() {
        String entryPass = "12345";
        String expectedStatus = Status.APPROVED.toString();
        when(visitorService.checkStatus(entryPass)).thenReturn(expectedStatus);

        ResponseEntity<SuccessResponseDTO> response = visitorController.checkStatus(entryPass);

        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedStatus, response.getBody().getBody());
        verify(visitorService, times(1)).checkStatus(entryPass);
    }
}
