package com.cdw.gatekeeper.utils;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Suite that holds test cases for common utilities
 */
public class UtilitiesTest {

    @Test
    void testConvertStringToLocalDate_Success() {
        String dateTimeString = "2025-01-01";

        LocalDate result = Utilities.convertStringToLocalDate(dateTimeString);
        assertEquals(LocalDate.of(2025, 01, 01), result);
    }

    @Test
    void testConvertStringToLocalDate_InvalidDateFormat() {
        String dateTimeString = "January 1st,2025";

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                Utilities.convertStringToLocalDate(dateTimeString)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK015, exception.getMessage());
    }

    @Test
    void testConvertStringToLocalDate_EmptyString() {
        String dateTimeString = "";

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                Utilities.convertStringToLocalDate(dateTimeString)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK015, exception.getMessage());
    }

    @Test
    void testConvertStringToLocalDate_NullInput() {
        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                Utilities.convertStringToLocalDate(null)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK015, exception.getMessage());
    }
}
