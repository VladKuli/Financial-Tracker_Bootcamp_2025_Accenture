package org.financialTracker.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.financialTracker.model.Role;

@Getter
@Setter
public class CreateUserDTO {
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private Role role;
}
