package org.financialTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//This class represents the request that the user sends to retrieve the JWT token.
@Getter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}

