package com.cdw.gatekeeper.services.adminservices;

import com.cdw.gatekeeper.dto.UpdateUserDTO;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.User;

import java.util.List;

/**
 * Interface for Admin Operations
 */
public interface AdminService {
    List<User> getAllUsers();

    String verifyUser(String id, VerificationRequestDTO verificationRequestDTO);

    void updateUser(String userId, UpdateUserDTO updateUserDTO);

    void deleteUser(String id);
}
