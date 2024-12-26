package com.cdw.gatekeeper.exceptions;

import com.cdw.gatekeeper.utils.ExceptionUtility;
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
        String responseBody = String.format(
                "{ \"status\": \"%s\", \"message\": \"%s\" }",
                HttpStatus.UNAUTHORIZED.toString(),
                exceptionUtility.getPropertyCode("GK008")
        );
        response.getWriter().write(responseBody);
    }
}
