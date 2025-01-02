package com.cdw.gatekeeper.services.authservices;

import com.cdw.gatekeeper.configs.JwtFilter;
import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.UserDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.mappers.UserDTOMapper;
import com.cdw.gatekeeper.repositories.UserRepository;
import com.cdw.gatekeeper.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class that holds Authentication Operations
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;

    /**
     * Registers a user collecting details from the userDTO
     * @param userDTO
     */
    @Transactional
    public void registerUser(UserDTO userDTO) {
        try {
            User user = userDTOMapper.mapUserToUserDTO(userDTO);

            if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
                throw new GatekeeperBusinessException(HttpStatus.CONFLICT, GKExceptionConstants.GK002);
            }
            userRepository.save(user);

        } catch (GatekeeperBusinessException gke) {
            throw gke;

        } catch (Exception e) {
            throw new GatekeeperBusinessException(HttpStatus.CONFLICT, GKExceptionConstants.GK001);
        }
    }

    /**
     * Logs in user
     * @param email
     * @param password
     * @return
     */
    public String login(String email, String password) {
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK003));

        // Password check
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new GatekeeperBusinessException(HttpStatus.UNAUTHORIZED, GKExceptionConstants.GK004);
        }

        // Access Denial for non-approved users
        if (!Status.APPROVED.equals(user.getStatus())) {
            throw new GatekeeperBusinessException(HttpStatus.FORBIDDEN, GKExceptionConstants.GK005);
        }

        return jwtUtil.generateToken(user.getEmailAddress(), user.getRole().toString());
    }

    /**
     * Logs a user out
     * @param request
     */
    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtFilter.invalidateToken(token);
        }
    }
}
