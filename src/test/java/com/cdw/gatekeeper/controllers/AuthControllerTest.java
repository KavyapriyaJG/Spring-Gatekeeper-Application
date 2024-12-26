package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.UserDTO;
import com.cdw.gatekeeper.entities.Role;
import com.cdw.gatekeeper.services.authservices.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;


/**
 * Suite that holds test cases for Authentication Controller Operations
 */
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Kavya");
        userDTO.setLastName("priya");
        userDTO.setDob(null);
        userDTO.setGender("Female");
        userDTO.setEmailAddress("test@example.com");
        userDTO.setRole(Role.ROLE_ADMIN.toString());
        userDTO.setPassword("password123");
        doNothing().when(authService).registerUser(userDTO);

        ResponseEntity<SuccessResponseDTO> response = authController.register(userDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals("User created successfully !", response.getBody().getBody());
        verify(authService, times(1)).registerUser(userDTO);
    }

    @Test
    void testLogin() {
        String email = "test@example.com";
        String password = "password123";
        String token = "mock-token";
        when(authService.login(email, password)).thenReturn(token);

        ResponseEntity<SuccessResponseDTO> response = authController.login(email, password);

        assertEquals(OK, response.getStatusCode());
        assertEquals(token, response.getBody().getBody());
        verify(authService, times(1)).login(email, password);
    }

    @Test
    void testLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Authentication auth = mock(Authentication.class);

        SecurityContextHolder.getContext().setAuthentication(auth);

        doNothing().when(authService).logout(request);

        ResponseEntity<SuccessResponseDTO> response = authController.logout(request);

        assertEquals(OK, response.getStatusCode());
        assertEquals("User Logged out successfully !", response.getBody().getBody());
        verify(authService, times(1)).logout(request);

        SecurityContextHolder.clearContext();
    }
}
