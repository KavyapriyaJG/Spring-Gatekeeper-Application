package com.cdw.gatekeeper.exceptions;

import com.cdw.gatekeeper.dto.FailureResponseDTO;
import com.cdw.gatekeeper.utils.ExceptionUtility;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

/**
 * GlobalExceptionHandler handles all exception thrown from the Gatekeeper controllers
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles resource not found exceptions
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {

        FailureResponseDTO failureResponseDTO = new FailureResponseDTO();
        failureResponseDTO.setHttpStatus(ex.getHttpStatus());
        failureResponseDTO.setMessage(ex.getMessage());

        return new ResponseEntity<>(failureResponseDTO, ex.getHttpStatus());
    }


    /**
     * Handles bad request exceptions
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<FailureResponseDTO> handleBadRequestException(BadRequestException ex) {

        FailureResponseDTO failureResponseDTO = new FailureResponseDTO();
        failureResponseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
        failureResponseDTO.setMessage(ex.getMessage());

        return new ResponseEntity<>(failureResponseDTO, HttpStatus.BAD_REQUEST);
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

        failureResponseDTO.setHttpStatus(ex.getHttpStatus());
        failureResponseDTO.setMessage(exceptionUtility.getPropertyCode(ex.getMessage()));

        return new ResponseEntity<>(failureResponseDTO, ex.getHttpStatus());
    }


    /**
     * Handles all other generic exceptions thrown
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureResponseDTO> handleGlobalException(Exception ex) {

        FailureResponseDTO failureResponseDTO = new FailureResponseDTO();
        failureResponseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        failureResponseDTO.setMessage("OOPS something went wrong. Internal server error"); // string. since if written to fetch from properties, would need to indicate that it would thrown IOException on this global handler

        return new ResponseEntity<>(failureResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}

