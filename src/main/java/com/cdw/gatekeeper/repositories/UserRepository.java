package com.cdw.gatekeeper.repositories;

import com.cdw.gatekeeper.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository for User document
 */
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAddress(String emailAddress);
}
