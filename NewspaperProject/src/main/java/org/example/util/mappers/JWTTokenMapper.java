package org.example.util.mappers;

import org.example.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenMapper {
    private final JWTUtil jwtUtil;

    @Autowired
    public JWTTokenMapper(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getEmailFromToken(String token){
        String jwtToken = token.substring(7);
        return jwtUtil.validateTokenAndRetrieveClaim(jwtToken);
    }
}
