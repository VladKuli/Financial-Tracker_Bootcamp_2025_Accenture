package org.financialTracker.response;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIResponse extends CoreResponse {

    private String message;

    public OpenAIResponse(List<CoreError> errorList) {
        super(errorList);
    }

    public OpenAIResponse(String message) {
        this.message = message;
    }
}
