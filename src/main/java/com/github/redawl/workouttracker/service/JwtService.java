package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.security.JwtKeyLocator;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.SignatureException;

import java.util.Optional;

/**
 * Perform Jwt related services for Spring Security
 * @author Eli Burch
 */
@Service
public class JwtService {
    private final JwtKeyLocator jwtKeyLocator;

    JwtService(JwtKeyLocator jwtKeyLocator){
        this.jwtKeyLocator = jwtKeyLocator;
    }

    /**
     * Parses a raw Jws token, verifying the signature with the keyserver
     * @param token Token to parse
     * @return Parsed Jws
     * @throws SignatureException If signature is invalid
     * @throws ExpiredJwtException If Jwt is expired
     */
    public Jws<Claims> getJwt(String token) throws SignatureException, ExpiredJwtException {
        return Jwts.parser()
                .keyLocator(jwtKeyLocator)
                .build()
                .parseSignedClaims(token);
    }

    /**
     * Wrapper for {@code getJwt}, catches exceptions and returns Optional based on
     * authentication success or failure
     * @param token Token to authenticate
     * @return {@code Optional.of} if auth success, {@code Optional.empty} if failure
     */
    public Optional<Jws<Claims>> authenticate(String token){
        try{
            return Optional.of(getJwt(token));
        } catch (ExpiredJwtException | SignatureException ex){
            return Optional.empty();
        }
    }
}
