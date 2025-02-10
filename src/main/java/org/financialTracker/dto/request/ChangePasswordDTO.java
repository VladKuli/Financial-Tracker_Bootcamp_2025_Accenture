package org.financialTracker.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class ChangePasswordDTO {
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}
