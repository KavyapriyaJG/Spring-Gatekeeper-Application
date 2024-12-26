package com.cdw.gatekeeper.services.adminservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.UpdateUserDTO;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.mappers.UserDTOMapper;
import com.cdw.gatekeeper.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * Suite that holds test cases for Admin Service Operations
 */
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDTOMapper userDTOMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private String userId;
    private UpdateUserDTO updateUserDTO;
    private VerificationRequestDTO verificationRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = "user123";
        user = new User();
        user.setId(userId);
        user.setStatus(Status.PENDING);
        updateUserDTO = new UpdateUserDTO();
        verificationRequestDTO = new VerificationRequestDTO();
    }

    @Test
    void testVerifyUser_Success_Approved() {
        verificationRequestDTO.setStatus(Status.APPROVED.toString());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        String response = adminService.verifyUser(userId, verificationRequestDTO);

        assertEquals("Approved Successfully !", response);
        assertEquals(Status.APPROVED, user.getStatus());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testVerifyUser_Success_Rejected() {
        verificationRequestDTO.setStatus(Status.REJECTED.toString());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        String response = adminService.verifyUser(userId, verificationRequestDTO);

        assertEquals("Rejected Successfully !", response);
        assertEquals(Status.REJECTED, user.getStatus());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testVerifyUser_Success_Blacklisted() {
        verificationRequestDTO.setStatus(Status.BLACKLISTED.toString());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        String response = adminService.verifyUser(userId, verificationRequestDTO);

        assertEquals("Blacklisted Successfully !", response);
        assertEquals(Status.BLACKLISTED, user.getStatus());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testVerifyUser_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                adminService.verifyUser(userId, verificationRequestDTO)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK003, exception.getMessage());
    }

    @Test
    void testVerifyUser_UserBlacklisted() {
        user.setStatus(Status.BLACKLISTED);
        verificationRequestDTO.setStatus(Status.BLACKLISTED.toString());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                adminService.verifyUser(userId, verificationRequestDTO)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK014, exception.getMessage());
    }

    @Test
    void testUpdateUser_Success() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("KopMop");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userDTOMapper.mapUpdatedUserToUserDTO(any(), any())).thenReturn(user);

        adminService.updateUser(userId, updateUserDTO);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(userId)).thenReturn(true);

        adminService.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                adminService.deleteUser(userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK003, exception.getMessage());
    }

    @Test
    void testDeleteUser_InternalError() {
        when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(userId);

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                adminService.deleteUser(userId)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK010, exception.getMessage());
    }
}
