package org.financialTracker.mapper;

import org.financialTracker.dto.UserDTO;
import org.financialTracker.dto.ExpenseDTO;
import org.financialTracker.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    // Convert a single User entity to UserDTO
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        // Convert List<Expense> to List<ExpenseDTO>
        List<ExpenseDTO> expenseDTOs = user.getExpenses() != null
                ? user.getExpenses()
                .stream()
                .map(ExpenseMapper::toDTO).collect(Collectors.toList())
                : null;

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                expenseDTOs
        );
    }

    // Convert a list of Users to a list of UserDTOs
    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}

