package com.cdw.gatekeeper.services.gatekeeperservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.repositories.VisitorPassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Suite that holds test cases for Gatekeeper Service Operations
 */
public class GatekeeperServiceImplTest {

    @Mock
    private VisitorPassRepository visitorPassRepository;

    @InjectMocks
    private GatekeeperServicesImpl gatekeeperServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVisitors_WithDate() {
        // Setup
        String dateInput = "2025-01-01";
        LocalDate date = LocalDate.of(2025, 01, 01);
        VisitorPass visitorPass1 = new VisitorPass();
        visitorPass1.setId("1");
        visitorPass1.setResidentId("12345");
        visitorPass1.setStatus(Status.APPROVED);
        visitorPass1.setVisitingTime(date);

        VisitorPass visitorPass2 = new VisitorPass();
        visitorPass2.setId("2");
        visitorPass2.setResidentId("12345");
        visitorPass2.setStatus(Status.PENDING);
        visitorPass2.setVisitingTime(date);

        List<VisitorPass> expectedVisitorPasses = Arrays.asList(visitorPass1, visitorPass2);

        when(visitorPassRepository.findByVisitingTime(date)).thenReturn(expectedVisitorPasses);

        List<VisitorPass> result = gatekeeperServices.getAllVisitors(dateInput);

        assertEquals(expectedVisitorPasses.size(), result.size());
        assertEquals(expectedVisitorPasses, result);
        verify(visitorPassRepository).findByVisitingTime(date);
    }

    @Test
    void testGetAllVisitors_NoDate() {
        String dateInput = "2025-01-01";
        LocalDate date = LocalDate.of(2025, 01, 01);
        VisitorPass visitorPass1 = new VisitorPass();
        visitorPass1.setId("1");
        visitorPass1.setResidentId("12345");
        visitorPass1.setStatus(Status.APPROVED);
        visitorPass1.setVisitingTime(date);

        VisitorPass visitorPass2 = new VisitorPass();
        visitorPass2.setId("2");
        visitorPass2.setResidentId("12345");
        visitorPass2.setStatus(Status.PENDING);
        visitorPass2.setVisitingTime(date);

        List<VisitorPass> expectedVisitorPasses = Arrays.asList(visitorPass1, visitorPass2);

        when(visitorPassRepository.findAll()).thenReturn(expectedVisitorPasses);

        List<VisitorPass> result = gatekeeperServices.getAllVisitors(null);

        assertEquals(expectedVisitorPasses.size(), result.size());
        assertEquals(expectedVisitorPasses, result);
        verify(visitorPassRepository).findAll();
    }

    @Test
    void testGetAllVisitors_InvalidDate() {
        String invalidDateInput = "invalid-date";

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                gatekeeperServices.getAllVisitors(invalidDateInput)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK011, exception.getMessage());
    }

    @Test
    void testVerifyVisitor_Success_Approved() {
        String visitorId = "1";
        VerificationRequestDTO requestDTO = new VerificationRequestDTO();
        requestDTO.setStatus(Status.APPROVED.toString());

        VisitorPass visitorPass = new VisitorPass();
        visitorPass.setId(visitorId);
        visitorPass.setResidentId("12345");
        visitorPass.setStatus(Status.APPROVED);

        when(visitorPassRepository.findById(visitorId)).thenReturn(Optional.of(visitorPass));

        String result = gatekeeperServices.verifyVisitor(visitorId, requestDTO);

        assertEquals("Approved Successfully !", result);
        assertEquals(Status.APPROVED, visitorPass.getStatus());
        verify(visitorPassRepository).save(visitorPass);
    }

    @Test
    void testVerifyVisitor_Success_Rejected() {
        String visitorId = "1";
        VerificationRequestDTO requestDTO = new VerificationRequestDTO();
        requestDTO.setStatus(Status.REJECTED.toString());

        VisitorPass visitorPass = new VisitorPass();
        visitorPass.setId(visitorId);
        visitorPass.setResidentId("12345");
        visitorPass.setStatus(Status.REJECTED);

        when(visitorPassRepository.findById(visitorId)).thenReturn(Optional.of(visitorPass));

        String result = gatekeeperServices.verifyVisitor(visitorId, requestDTO);

        assertEquals("Rejected Successfully !", result);
        assertEquals(Status.REJECTED, visitorPass.getStatus());
        verify(visitorPassRepository).save(visitorPass);
    }

    @Test
    void testVerifyVisitor_Success_Blacklisted() {
        String visitorId = "1";
        VerificationRequestDTO requestDTO = new VerificationRequestDTO();
        requestDTO.setStatus(Status.BLACKLISTED.toString());

        VisitorPass visitorPass = new VisitorPass();
        visitorPass.setId(visitorId);
        visitorPass.setResidentId("12345");
        visitorPass.setStatus(Status.PENDING);

        when(visitorPassRepository.findById(visitorId)).thenReturn(Optional.of(visitorPass));

        String result = gatekeeperServices.verifyVisitor(visitorId, requestDTO);

        assertEquals("Blacklisted Successfully !", result);
        assertEquals(Status.BLACKLISTED, visitorPass.getStatus());
        verify(visitorPassRepository).save(visitorPass);
    }

    @Test
    void testVerifyVisitor_VisitorNotFound() {
        String visitorId = "1";
        VerificationRequestDTO requestDTO = new VerificationRequestDTO();
        requestDTO.setStatus(Status.APPROVED.toString());

        when(visitorPassRepository.findById(visitorId)).thenReturn(Optional.empty());

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                gatekeeperServices.verifyVisitor(visitorId, requestDTO)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK003, exception.getMessage());
    }

    @Test
    void testVerifyVisitor_AlreadyBlacklisted() {
        String visitorId = "1";
        VerificationRequestDTO requestDTO = new VerificationRequestDTO();
        requestDTO.setStatus(Status.APPROVED.toString());

        VisitorPass visitorPass = new VisitorPass();
        visitorPass.setId(visitorId);
        visitorPass.setResidentId("12345");
        visitorPass.setStatus(Status.BLACKLISTED);

        when(visitorPassRepository.findById(visitorId)).thenReturn(Optional.of(visitorPass));

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                gatekeeperServices.verifyVisitor(visitorId, requestDTO)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK014, exception.getMessage());
    }

    @Test
    void testVerifyVisitor_InvalidStatus() {
        String visitorId = "1";
        VerificationRequestDTO requestDTO = new VerificationRequestDTO();
        requestDTO.setStatus("INVALID");

        VisitorPass visitorPass = new VisitorPass();
        visitorPass.setId(visitorId);
        visitorPass.setResidentId("12345");
        visitorPass.setStatus(Status.APPROVED);

        when(visitorPassRepository.findById(visitorId)).thenReturn(Optional.of(visitorPass));

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                gatekeeperServices.verifyVisitor(visitorId, requestDTO)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK009, exception.getMessage());
    }
}
