package com.cdw.gatekeeper.security;

import com.cdw.gatekeeper.constants.CommonConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Class that holds utilities for JWT operations
 */
@Component
public class JwtUtil {
    private final String SECRET_KEY = CommonConstants.JWT_SECRET;

    /**
     * Generates JWTs on call for a user with the given role
     * @param email
     * @param role
     * @return String
     */
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3)) // 3 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
