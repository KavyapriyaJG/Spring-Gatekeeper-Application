package com.cdw.gatekeeper.controllers;

import com.cdw.gatekeeper.dto.SuccessResponseDTO;
import com.cdw.gatekeeper.dto.UserDTO;
import com.cdw.gatekeeper.services.authservices.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


/**
 * RestController for handling Authentication operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    /**
     * Registers a user
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> register(@RequestBody UserDTO userDTO) {
        authService.registerUser(userDTO);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody("User created successfully !");

        return ResponseEntity.created(URI.create("/register")).body(successResponseDTO);
    }

    /**
     * Logs in a user
     * @param email
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDTO> login(@RequestParam String email, @RequestParam String password) {
        String token = authService.login(email, password);
        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody(token);
        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Logs out a user
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<SuccessResponseDTO> logout(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            authService.logout(request);
        }

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody("User Logged out successfully !");

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
