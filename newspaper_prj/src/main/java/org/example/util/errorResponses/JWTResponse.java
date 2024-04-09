package org.example.util.errorResponses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JWTResponse {
    private String jwt;
}
