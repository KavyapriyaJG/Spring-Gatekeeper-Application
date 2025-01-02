package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.VisitorDTO;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.services.residentservices.ResidentService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;


/**
 * Suite that holds test cases for Resident Controller Operations
 */
class ResidentControllerTest {

    @Mock
    private ResidentService residentService;

    @InjectMocks
    private ResidentController residentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVisitors() {
        String residentId = "123";
        List<VisitorPass> visitorPasses = new ArrayList<>();
        visitorPasses.add(new VisitorPass());
        when(residentService.getAllVisitorsByResidentIdAndDate(residentId, null)).thenReturn(visitorPasses);

        ResponseEntity<SuccessResponseDTO> response = residentController.getAllVisitors(residentId, null);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(visitorPasses, response.getBody().getBody());
        verify(residentService, times(1)).getAllVisitorsByResidentIdAndDate(residentId, null);
    }

    @Test
    void testGetAllVisitorsWithDate() {
        String residentId = "123";
        String date = "2024-12-24";
        List<VisitorPass> visitorPasses = new ArrayList<>();
        visitorPasses.add(new VisitorPass());
        when(residentService.getAllVisitorsByResidentIdAndDate(residentId, date)).thenReturn(visitorPasses);

        ResponseEntity<SuccessResponseDTO> response = residentController.getAllVisitors(residentId, date);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(visitorPasses, response.getBody().getBody());
        verify(residentService, times(1)).getAllVisitorsByResidentIdAndDate(residentId, date);
    }

    @Test
    void testScheduleVisitor() {
        VisitorDTO visitorDTO = new VisitorDTO();
        visitorDTO.setResidentId("ResidentId123");
        visitorDTO.setVisitingTime("2024-12-25");
        when(residentService.scheduleVisitor(visitorDTO)).thenReturn("Visitor scheduled successfully !");

        ResponseEntity<SuccessResponseDTO> response = residentController.scheduleVisitor(visitorDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Scheduled successfully !", response.getBody().getBody());
        verify(residentService, times(1)).scheduleVisitor(visitorDTO);
    }

    @Test
    void testRemoveVisitor() {
        String visitorId = "123";
        when(residentService.removeVisitor(visitorId)).thenReturn("Visitor removed successfully !");

        ResponseEntity<SuccessResponseDTO> response = residentController.removeVisitor(visitorId);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Schedule cancelled successfully !", response.getBody().getBody());
        verify(residentService, times(1)).removeVisitor(visitorId);
    }

    @Test
    void testBlacklistVisitor() {
        String visitorId = "123";
        when(residentService.blacklistVisitor(visitorId)).thenReturn("Visitor blacklisted successfully");

        ResponseEntity<SuccessResponseDTO> response = residentController.blacklistVisitor(visitorId);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Visitor blacklisted successfully", response.getBody().getBody());
        verify(residentService, times(1)).blacklistVisitor(visitorId);
    }
}
