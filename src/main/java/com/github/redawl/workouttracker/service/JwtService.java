package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.security.JwtKeyLocator;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.SignatureException;

import java.util.Optional;

@Service
public class JwtService {

    private final JwtKeyLocator jwtKeyLocator;
    JwtService(JwtKeyLocator jwtKeyLocator){
        this.jwtKeyLocator = jwtKeyLocator;
    }

    public Jws<Claims> getJwt(String token) throws SignatureException, ExpiredJwtException {
        return Jwts.parser()
                 .keyLocator(jwtKeyLocator)
                 .build()
                .parseSignedClaims(token);
    }

    public Optional<Jws<Claims>> authenticate(String token){
        try{
            return Optional.of(getJwt(token));
        } catch (ExpiredJwtException | SignatureException ex){
            return Optional.empty();
        }
    }
}
