package org.financialTracker.mapper;

import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.dto.response.TransactionResponseDTO;
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

        // Convert List<Transaction> to List<TransactionDTO>
        List<TransactionResponseDTO> transactionDTOs = user.getExpens() != null
                ? user.getExpens()
                .stream()
                .map(TransactionMapper::toDTO).collect(Collectors.toList())
                : null;

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole().toString(),
                transactionDTOs
        );
    }

    // Convert a list of Users to a list of UserDTOs
    public static List<UserResponseDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

}

