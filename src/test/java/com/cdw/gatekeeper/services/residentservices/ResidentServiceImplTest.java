package com.cdw.gatekeeper.services.residentservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.VisitorDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.mappers.VisitorPassDTOMapper;
import com.cdw.gatekeeper.repositories.UserRepository;
import com.cdw.gatekeeper.repositories.VisitorPassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Suite that holds test cases for Resident Service Operations
 */
class ResidentServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VisitorPassRepository visitorPassRepository;

    @Mock
    private VisitorPassDTOMapper visitorPassDTOMapper;

    @InjectMocks
    private ResidentServiceImpl residentService;

    private VisitorPass visitorPass;
    private VisitorDTO visitorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup common visitor objects
        visitorPass = new VisitorPass();
        visitorPass.setId("visitor123");
        visitorPass.setResidentId("12345");
        visitorPass.setStatus(Status.APPROVED);
        visitorPass.setVisitingTime(LocalDate.of(2025, 01, 01));

        visitorDTO = new VisitorDTO();
        visitorDTO.setResidentId("12345");
        visitorDTO.setVisitingTime("2025-01-01");
    }

    @Test
    void testScheduleVisitor_Success() {
        when(userRepository.findById(visitorDTO.getResidentId())).thenReturn(Optional.of(new User()));
        when(visitorPassDTOMapper.mapVisitorDTOtoVisitorPass(visitorDTO)).thenReturn(visitorPass);

        String result = residentService.scheduleVisitor(visitorDTO);

        assertEquals("Visitor scheduled successfully !", result);
        verify(visitorPassRepository).save(visitorPass);
    }

    @Test
    void testScheduleVisitor_ResidentNotFound() {
        when(userRepository.findById(visitorDTO.getResidentId())).thenReturn(Optional.empty());

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                residentService.scheduleVisitor(visitorDTO)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK003, exception.getMessage());
    }

    @Test
    void testRemoveVisitor_Success() {
        doNothing().when(visitorPassRepository).deleteById(visitorPass.getId());

        String result = residentService.removeVisitor(visitorPass.getId());

        assertEquals("Visitor removed successfully !", result);
        verify(visitorPassRepository).deleteById(visitorPass.getId());
    }

    @Test
    void testRemoveVisitor_VisitorNotFound() {
        doThrow(new IllegalArgumentException("Visitor not found")).when(visitorPassRepository).deleteById(visitorPass.getId());

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                residentService.removeVisitor(visitorPass.getId())
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK003, exception.getMessage());
    }

    @Test
    void testGetAllVisitorsByResidentIdAndDate_WithDate() {
        when(visitorPassRepository.findByResidentIdAndVisitingTime(visitorDTO.getResidentId(), visitorPass.getVisitingTime()))
                .thenReturn(List.of(visitorPass));

        List<VisitorPass> result = residentService.getAllVisitorsByResidentIdAndDate(visitorDTO.getResidentId(), "2025-01-01");

        assertEquals(1, result.size());
        assertEquals(visitorPass, result.get(0));
    }

    @Test
    void testGetAllVisitorsByResidentIdAndDate_NoDate() {
        when(visitorPassRepository.findByResidentId(visitorDTO.getResidentId())).thenReturn(List.of(visitorPass));

        List<VisitorPass> result = residentService.getAllVisitorsByResidentIdAndDate(visitorDTO.getResidentId(), null);

        assertEquals(1, result.size());
        assertEquals(visitorPass, result.get(0));
    }

    @Test
    void testBlacklistVisitor_Success() {
        when(visitorPassRepository.findById(visitorPass.getId())).thenReturn(Optional.of(visitorPass));

        String result = residentService.blacklistVisitor(visitorPass.getId());

        assertEquals("Visitor Blacklisted successfully !", result);
        assertEquals(Status.BLACKLISTED, visitorPass.getStatus());
        verify(visitorPassRepository).save(visitorPass);
    }

    @Test
    void testBlacklistVisitor_AlreadyBlacklisted() {
        visitorPass.setStatus(Status.BLACKLISTED);

        when(visitorPassRepository.findById(visitorPass.getId())).thenReturn(Optional.of(visitorPass));

        String result = residentService.blacklistVisitor(visitorPass.getId());

        assertEquals("Visitor already Blacklisted !", result);
    }

    @Test
    void testBlacklistVisitor_VisitorNotFound() {
        when(visitorPassRepository.findById(visitorPass.getId())).thenReturn(Optional.empty());

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                residentService.blacklistVisitor(visitorPass.getId())
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK016, exception.getMessage());
    }
}
