package com.cdw.gatekeeper.exceptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 *  GatekeeperBusinessException entity
 */
@Data
@AllArgsConstructor
public class GatekeeperBusinessException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
}
