package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.UpdateUserDTO;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.mappers.UserDTOMapper;
import com.cdw.gatekeeper.services.adminservices.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RestController for handling Admin operations
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public AdminController(AdminService adminService, UserDTOMapper userDTOMapper) {
        this.adminService = adminService;
        this.userDTOMapper = userDTOMapper;
    }

    /**
     * Fetches list of all users
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<SuccessResponseDTO> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody(users);

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Modifies a user's status based on request
     * @return
     */
    @PutMapping("/users/status/{id}")
    public ResponseEntity<SuccessResponseDTO> verifyUser(@PathVariable String id, @RequestBody VerificationRequestDTO verificationRequestDTO) {
        String verificationStatus = adminService.verifyUser(id, verificationRequestDTO);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody(verificationStatus);

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Updates a user based on the updateUserDTO
     * @return
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<SuccessResponseDTO> updateUser(@PathVariable String id, @RequestBody UpdateUserDTO updateUserDTO) {
        adminService.updateUser(id, updateUserDTO);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody("Updated successfully !");

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Deletes a user
     * @param id
     * @return
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        adminService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
