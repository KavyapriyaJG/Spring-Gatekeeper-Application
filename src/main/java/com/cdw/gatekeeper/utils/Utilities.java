package com.cdw.gatekeeper.utils;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class that holds Utilities
 */
public class Utilities {
    public static LocalDate convertStringToLocalDate(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateTimeString, formatter);
        } catch (Exception e) {
            throw new GatekeeperBusinessException(HttpStatus.CONFLICT, GKExceptionConstants.GK015);
        }
    }

}
