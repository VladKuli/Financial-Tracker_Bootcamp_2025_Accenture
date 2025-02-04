package org.financialTracker.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
    private List<ExpenseDTO> expenses;
}

