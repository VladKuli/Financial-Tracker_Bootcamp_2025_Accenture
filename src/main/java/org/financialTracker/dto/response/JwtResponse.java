package org.financialTracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor

//This class represents the response returned
// to the user and contains access- and refresh-tokens:
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}

