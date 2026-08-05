package com.rohit.expensetracker.mapper;

import com.rohit.expensetracker.dto.auth.RegisterRequest;
import com.rohit.expensetracker.dto.auth.RegisterResponse;
import com.rohit.expensetracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {

        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());

        return  user;
    }

    public RegisterResponse toRegisterResponse(User user) {

        return new RegisterResponse(
                user.getUuid(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
