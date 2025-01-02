package com.cdw.gatekeeper.configs;
import com.cdw.gatekeeper.constants.GKExceptionConstants;
import com.cdw.gatekeeper.entities.User;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import com.cdw.gatekeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * Class that extends the UserDetailsService class
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by the specified id (email address)
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new GatekeeperBusinessException(HttpStatus.NOT_FOUND, GKExceptionConstants.GK006));

        // Return UserDetails object with user's email, password, and roles/authorities
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailAddress())
                .password(user.getPassword())
                // Role-based authority
                .authorities(new ArrayList<GrantedAuthority>() {{
                    add(new SimpleGrantedAuthority(user.getRole().name())); // Convert enum to String
                }})
                .build();
    }
}
