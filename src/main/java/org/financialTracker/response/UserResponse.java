package org.financialTracker.response;

import lombok.AllArgsConstructor;
import org.financialTracker.model.User;

import java.util.List;

public class UserResponse extends CoreResponse {
    private String message;


    public UserResponse(String message) {
        super(null);
        this.message = message;
    }

    public UserResponse(List<CoreError> errorList) {
        super(errorList);
    }

    public String getMessage() {
        return message;
    }
}
