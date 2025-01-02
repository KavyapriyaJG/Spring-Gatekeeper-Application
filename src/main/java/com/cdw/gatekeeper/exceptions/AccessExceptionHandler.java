package com.cdw.gatekeeper.exceptions;

import com.cdw.gatekeeper.dto.FailureResponseDTO;
import com.cdw.gatekeeper.utils.ExceptionUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handles exceptions thrown from unauthorized route accesses
 */
@Component
public class AccessExceptionHandler implements AccessDeniedHandler {

    /**
     * Handles the exception thrown and returns the unauthorized message in the response
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ExceptionUtility exceptionUtility = new ExceptionUtility();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        FailureResponseDTO failureResponseDTO = new FailureResponseDTO();
        failureResponseDTO.setSuccess(false);
        failureResponseDTO.setMessage(exceptionUtility.getPropertyCode("GK008"));

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(failureResponseDTO));
    }
}
