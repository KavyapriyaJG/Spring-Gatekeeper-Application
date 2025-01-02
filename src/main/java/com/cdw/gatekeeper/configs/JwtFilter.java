package com.cdw.gatekeeper.configs;

import com.cdw.gatekeeper.constants.CommonConstants;
import com.cdw.gatekeeper.exceptions.GatekeeperBusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that holds fields and functions for JWT filter
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    // List of blacklisted tokens
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();
    public JwtFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Validates the jwt token with some custom logics
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @throws GatekeeperBusinessException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, GatekeeperBusinessException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Check if the token is blacklisted
                if (tokenBlacklist.contains(jwt)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    String responseBody = String.format(
                            "{ \"status\": \"%s\", \"message\": \"Token is invalidated. Please log in again.\" }",
                            HttpServletResponse.SC_UNAUTHORIZED
                    );
                    response.getWriter().write(responseBody);

                    return;
                }

                if (validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts username from the jwt
     * @param token
     * @return
     */
    private String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(CommonConstants.JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // In case of invalid token
            return null;
        }
    }

    /**
     * Validates a jwt based on the userdetails
     * @param token
     * @param userDetails
     * @return
     */
    private boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT is expired
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(CommonConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new java.util.Date());
    }

    /**
     * Invalidates a given JWT
     * @param token
     */
    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }
}
