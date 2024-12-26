package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.UpdateUserDTO;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.mappers.UserDTOMapper;
import com.cdw.gatekeeper.services.adminservices.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Suite that holds test cases for Admin Controller Operations
 */
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private UserDTOMapper userDTOMapper;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(adminService.getAllUsers()).thenReturn(users);

        ResponseEntity<SuccessResponseDTO> response = adminController.getAllUsers();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(users, response.getBody().getBody());
        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    void testVerifyUser() {
        String userId = "123";
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
        verificationRequestDTO.setStatus(Status.APPROVED.toString());
        when(adminService.verifyUser(userId, verificationRequestDTO)).thenReturn(Status.APPROVED.toString());

        ResponseEntity<SuccessResponseDTO> response = adminController.verifyUser(userId, verificationRequestDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Status.APPROVED.toString(), response.getBody().getBody());
        verify(adminService, times(1)).verifyUser(userId, verificationRequestDTO);
    }

    @Test
    void testUpdateUser() {
        String userId = "123";
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Kavyapriya");
        updateUserDTO.setLastName("Govindarajan");
        doNothing().when(adminService).updateUser(userId, updateUserDTO);

        ResponseEntity<SuccessResponseDTO> response = adminController.updateUser(userId, updateUserDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated successfully !", response.getBody().getBody());
        verify(adminService, times(1)).updateUser(userId, updateUserDTO);
    }

    @Test
    void testDeleteUser() {
        String userId = "123";
        doNothing().when(adminService).deleteUser(userId);

        ResponseEntity<SuccessResponseDTO> response = adminController.deleteUser(userId);

        assertEquals(NO_CONTENT, response.getStatusCode());
        assertEquals("User deleted successfully !", response.getBody().getBody());
        verify(adminService, times(1)).deleteUser(userId);
    }
}
