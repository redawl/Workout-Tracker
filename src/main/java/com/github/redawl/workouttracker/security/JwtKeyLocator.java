package com.github.redawl.workouttracker.security;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.ProtectedHeader;
import io.jsonwebtoken.security.Jwk;
import io.jsonwebtoken.security.Jwks;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.Key;

@Component
public class JwtKeyLocator extends LocatorAdapter<Key> {
    private final RestTemplate restTemplate;

    public JwtKeyLocator(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    protected Key locate(ProtectedHeader header) {
        JsonNode response =
                restTemplate.exchange("http://127.0.0.1/api/auth/jwt/jwks.json", HttpMethod.GET, null, JsonNode.class).getBody();

        if(response != null && response.get("keys") != null && response.get("keys").isArray() && response.get("keys").size() == 2){
            String token = response.get("keys").get(0).toString();
            Jwk<?> jwk = Jwks.parser()
                .build()
                .parse(token);
            return jwk.toKey();
        }

        return null;
    }
}
