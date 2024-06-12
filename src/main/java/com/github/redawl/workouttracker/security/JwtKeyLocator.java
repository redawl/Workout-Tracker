package com.github.redawl.workouttracker.security;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.ProtectedHeader;
import io.jsonwebtoken.security.Jwk;
import io.jsonwebtoken.security.Jwks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.Key;

@Component
public class JwtKeyLocator extends LocatorAdapter<Key> {
    @Value("${auth.api.url}")
    private String authApiUrl;
    @Value("${auth.api.jwks}")
    private String authApiJwks;

    private static final String KEYS = "keys";

    private final RestTemplate restTemplate;

    public JwtKeyLocator(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    protected Key locate(ProtectedHeader header) {
        JsonNode response =
                restTemplate.exchange(authApiUrl + authApiJwks, HttpMethod.GET, null, JsonNode.class).getBody();

        if(response != null
                && response.get(KEYS) != null
                && response.get(KEYS).isArray()
                && response.get(KEYS).size() > 1){
            String token = response.get(KEYS).get(0).toString();
            Jwk<?> jwk = Jwks.parser()
                .build()
                .parse(token);
            return jwk.toKey();
        }

        return null;
    }
}
