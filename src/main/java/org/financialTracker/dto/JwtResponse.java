package org.financialTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@AllArgsConstructor

//This class represents the response returned
// to the user and contains access- and refresh-tokens:
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}

