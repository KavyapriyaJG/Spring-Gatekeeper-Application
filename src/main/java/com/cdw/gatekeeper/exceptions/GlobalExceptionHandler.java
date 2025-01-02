package com.cdw.gatekeeper.exceptions;

import com.cdw.gatekeeper.dto.FailureResponseDTO;
import com.cdw.gatekeeper.utils.ExceptionUtility;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

/**
 * GlobalExceptionHandler handles all exception thrown from the Gatekeeper controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles resource not found exceptions
     * @param ex
     * @return String
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }


    /**
     * Handles bad request exceptions
     * @param ex
     * @return String
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(BadRequestException ex) {
        return ex.getMessage();
    }


    /**
     * Handles GatekeeperBusinessExceptions thrown
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(GatekeeperBusinessException.class)
    public ResponseEntity<Object> handleGatekeeperException(GatekeeperBusinessException ex, WebRequest request) throws IOException {

        ExceptionUtility exceptionUtility = new ExceptionUtility();
        FailureResponseDTO  failureResponseDTO = new FailureResponseDTO();

        failureResponseDTO.setSuccess(false);
        failureResponseDTO.setMessage(exceptionUtility.getPropertyCode(ex.getMessage()));

        return new ResponseEntity<>(failureResponseDTO, ex.getHttpStatus());
    }


    /**
     * Handles all other generic exceptions thrown
     * @param ex
     * @return String
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex) {
        return "OOPS something went wrong. Internal server error"; // string. since if written to fetch from properties, would need to indicate that it would thrown IOException on this global handler
    }
}

