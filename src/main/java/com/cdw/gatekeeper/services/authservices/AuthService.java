package com.cdw.gatekeeper.services.authservices;

import com.cdw.gatekeeper.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for Authentication Operations
 */
public interface AuthService {
    void registerUser(UserDTO userDTO);
    String login(String email, String password);

    void logout(HttpServletRequest request);
}
