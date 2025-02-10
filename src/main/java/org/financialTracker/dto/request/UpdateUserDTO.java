package org.financialTracker.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class UpdateUserDTO {
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String email;
}
