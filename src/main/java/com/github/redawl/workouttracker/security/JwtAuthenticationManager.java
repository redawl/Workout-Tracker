package com.github.redawl.workouttracker.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationManager implements AuthenticationManager {
    /**
     * Always reject requests to authenticate.
     * Users should navigate to /auth to authenticate using SuperTokens
     * @param authentication the authentication request object
     * @return authentication without authenticating
     * @throws AuthenticationException Never throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }
}
