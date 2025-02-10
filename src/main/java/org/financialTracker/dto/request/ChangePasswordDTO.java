package org.financialTracker.dto.request;

import lombok.*;

@Getter
@Setter
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
