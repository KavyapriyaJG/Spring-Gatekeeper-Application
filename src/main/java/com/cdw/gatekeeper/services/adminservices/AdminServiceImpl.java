package com.cdw.gatekeeper.services.adminservices;

import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.dto.UpdateUserDTO;
import com.cdw.gatekeeper.dto.VerificationRequestDTO;
import com.cdw.gatekeeper.entities.Status;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.mappers.UserDTOMapper;
import com.cdw.gatekeeper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class that holds Admin Operations
 */
@Service
public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

    /**
     * Fetches list of all users
     * @return
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Checks the status of a user
     * @param userId
     * @param verificationRequestDTO
     * @return
     */
    @Transactional
    @Override
    public String verifyUser(String userId, VerificationRequestDTO verificationRequestDTO) {
        String response;
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK003));

        if(user.getStatus() == Status.BLACKLISTED) {
            throw new GatekeeperBusinessException(HttpStatus.BAD_REQUEST, GKExceptionConstants.GK014);
        }

        switch (verificationRequestDTO.getStatus()) {
            case "APPROVED" -> {
                user.setStatus(Status.APPROVED);
                response = "Approved Successfully !";
            }
            case "REJECTED" -> {
                user.setStatus(Status.REJECTED);
                response = "Rejected Successfully !";
            }
            case "BLACKLISTED" -> {
                user.setStatus(Status.BLACKLISTED);
                response = "Blacklisted Successfully !";
            }
            default -> throw new GatekeeperBusinessException(HttpStatus.BAD_REQUEST, GKExceptionConstants.GK009);
        }

        userRepository.save(user);

        return response;
    }

    /**
     * Updates the user with data from the updateUserDTO
     * @param userId
     * @param updateUserDTO
     */
    @Transactional
    @Override
    public void updateUser(String userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK003));

        user = userDTOMapper.mapUpdatedUserToUserDTO(updateUserDTO, user);
        userRepository.save(user);
    }

    /**
     * Deletes user by id
     * @param userId
     */
    @Transactional
    @Override
    public void deleteUser(String userId) {
        if(! userRepository.existsById(userId)) {
            throw new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK003);
        }
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new GatekeeperBusinessException(HttpStatus.INTERNAL_SERVER_ERROR, GKExceptionConstants.GK010);
        }
    }
}
