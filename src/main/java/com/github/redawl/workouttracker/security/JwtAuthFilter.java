package com.github.redawl.workouttracker.security;

import com.github.redawl.workouttracker.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Security filter to authenticate Jwt cookie
 * @author Eli Burch
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String S_ACCESS_TOKEN = "sAccessToken";

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if(request.getCookies() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<Jws<Claims>> jws = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(S_ACCESS_TOKEN))
                    .map(Cookie::getValue)
                    .findAny().flatMap(jwtService::authenticate);

            if(jws.isPresent()){
                String userid = jws.get().getPayload().getSubject();
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(jws.get(), userid, null);

                // Check if email is verified
                if(jws.get().getPayload().getOrDefault("st-ev", new LinkedHashMap<>()) instanceof Map<?,?> stEv
                        && stEv.get("v") instanceof Boolean emailVerified && Boolean.FALSE.equals(emailVerified)){
                    authenticationToken.setAuthenticated(false);
                }

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // If we have no auth, send unauthorized
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().contains("/api/");
    }
}
