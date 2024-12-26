package com.cdw.gatekeeper.services.authservices;

import com.cdw.gatekeeper.configs.JwtFilter;
import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.UserDTO;
import com.cdw.gatekeeper.entities.Role;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.mappers.UserDTOMapper;
import com.cdw.gatekeeper.repositories.UserRepository;
import com.cdw.gatekeeper.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Suite that holds test cases for Authentication Service Operations
 */
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDTOMapper userDTOMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtFilter jwtFilter;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setEmailAddress("test@example.com");
        userDTO.setPassword("password");
        userDTO.setRole(Role.ROLE_GATEKEEPER.toString());

        user = new User();
        user.setEmailAddress("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.ROLE_RESIDENT);
        user.setStatus(Status.APPROVED);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmailAddress(user.getEmailAddress())).thenReturn(Optional.empty());
        when(userDTOMapper.mapUserToUserDTO(userDTO)).thenReturn(user);

        authService.registerUser(userDTO);

        verify(userRepository).save(user);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userRepository.findByEmailAddress(user.getEmailAddress())).thenReturn(Optional.of(user));

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                authService.registerUser(userDTO)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK001, exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        String email = "test@example.com";
        String password = "password";

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(email, user.getRole().toString())).thenReturn("generatedToken");

        String token = authService.login(email, password);

        assertEquals("generatedToken", token);
    }

    @Test
    void testLogin_UserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.empty());

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                authService.login(email, password)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK003, exception.getMessage());
    }

    @Test
    void testLogin_InvalidPassword() {
        String email = "test@example.com";
        String password = "wrongPassword";

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                authService.login(email, password)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK004, exception.getMessage());
    }

    @Test
    void testLogin_UserNotApproved() {
        user.setStatus(Status.PENDING);

        String email = "test@example.com";
        String password = "password";

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        GatekeeperBusinessException exception = assertThrows(GatekeeperBusinessException.class, () ->
                authService.login(email, password)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertEquals(GKExceptionConstants.GK005, exception.getMessage());
    }

    @Test
    void testLogout_Success() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");

        authService.logout(request);

        verify(jwtFilter).invalidateToken("token123");
    }

    @Test
    void testLogout_NoAuthHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        authService.logout(request);

        verify(jwtFilter, never()).invalidateToken(anyString());
    }
}
