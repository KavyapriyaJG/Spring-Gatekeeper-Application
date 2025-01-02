package com.cdw.gatekeeper.mappers;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.UpdateUserDTO;
import com.cdw.gatekeeper.dto.UserDTO;
import com.cdw.gatekeeper.entities.Role;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.utils.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Class that holds UserDTO mapper functions
 */
@Component
@RequiredArgsConstructor
public class UserDTOMapper {

    private final PasswordEncoder passwordEncoder;

    /**
     * Maps UserDTO to User Entity
     * @param userDTO
     * @return user
     */
    public User mapUserToUserDTO(UserDTO userDTO) {
        User user = new User();
        LocalDate dob;
        Role role;

        // DOB
        if(userDTO.getDob() != null) {
            dob = Utilities.convertStringToLocalDate(userDTO.getDob());
            user.setDob(dob);
        }

        // STATUS
        user.setStatus(Status.PENDING);

        // ROLE
        if(userDTO.getRole()!= null) {
            String roleInput = userDTO.getRole().toUpperCase();
            switch (roleInput) {
                case "ADMIN" -> {
                    user.setRole(Role.ROLE_ADMIN);
                    user.setStatus(Status.APPROVED);
                }
                case "GATEKEEPER" -> user.setRole(Role.ROLE_GATEKEEPER);
                case "RESIDENT" -> user.setRole(Role.ROLE_RESIDENT);
                default -> throw new GatekeeperBusinessException(HttpStatus.CONFLICT, GKExceptionConstants.GK013);
            }
        }

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setGender(userDTO.getGender());
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedDateTime(LocalDateTime.now());
        user.setUpdatedDateTime(LocalDateTime.now());

        return user;
    }

    /**
     * Maps the updateUserDTO on to the userDTO
     * @param updateUserDTO
     * @param user
     * @return
     */
    public User mapUpdatedUserToUserDTO(UpdateUserDTO updateUserDTO, User user) {

        // DOB
        LocalDate dob = user.getDob();
        if(updateUserDTO.getDob() != null) {
            dob = Utilities.convertStringToLocalDate(updateUserDTO.getDob());
        }
        user.setDob(dob);

        user.setFirstName(updateUserDTO.getFirstName() != null ? updateUserDTO.getFirstName() : user.getFirstName());
        user.setLastName(updateUserDTO.getLastName() != null ? updateUserDTO.getLastName() : user.getLastName());
        user.setGender(updateUserDTO.getGender() != null ? updateUserDTO.getGender() : user.getGender());

        user.setUpdatedDateTime(LocalDateTime.now());

        return user;
    }

}
