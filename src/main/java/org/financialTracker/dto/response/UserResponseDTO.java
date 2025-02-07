package org.financialTracker.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String role;
    private List<ExpenseResponseDTO> expenses;
}

