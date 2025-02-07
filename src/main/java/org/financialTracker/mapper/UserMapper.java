package org.financialTracker.mapper;

import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.dto.response.ExpenseResponseDTO;
import org.financialTracker.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    // Convert a single User entity to UserDTO
    public static UserResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        // Convert List<Expense> to List<ExpenseDTO>
        List<ExpenseResponseDTO> expenseDTOs = user.getExpenses() != null
                ? user.getExpenses()
                .stream()
                .map(ExpenseMapper::toDTO).collect(Collectors.toList())
                : null;

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole().toString(),
                expenseDTOs
        );
    }

    // Convert a list of Users to a list of UserDTOs
    public static List<UserResponseDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

}

