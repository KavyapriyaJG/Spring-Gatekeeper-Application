package com.cdw.gatekeeper.services.visitorservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.VisitorPass;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.repositories.VisitorPassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Suite that holds test cases for Visitor Service Operations
 */
public class VisitorServiceImplTest {

    @Mock
    private VisitorPassRepository visitorPassRepository;

    @InjectMocks
    private VisitorServiceImpl visitorService;

    private VisitorPass visitorPass;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup a common VisitorPass object
        visitorPass = new VisitorPass();
        visitorPass.setEntryPass("ENTRY-123");
        visitorPass.setStatus(Status.APPROVED);
    }

    @Test
    void testCheckStatus_Success() {
        when(visitorPassRepository.findByEntryPass("ENTRY-123")).thenReturn(Optional.of(visitorPass));

        String result = visitorService.checkStatus("ENTRY-123");

        assertEquals("This visitor pass is in status : " + Status.APPROVED.toString(), result);
        verify(visitorPassRepository).findByEntryPass("ENTRY-123");
    }

    @Test
    void testCheckStatus_VisitorPassNotFound() {
        when(visitorPassRepository.findByEntryPass("ENTRY-456")).thenReturn(Optional.empty());

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                visitorService.checkStatus("ENTRY-456")
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK012, exception.getMessage());
        verify(visitorPassRepository).findByEntryPass("ENTRY-456");
    }
}
