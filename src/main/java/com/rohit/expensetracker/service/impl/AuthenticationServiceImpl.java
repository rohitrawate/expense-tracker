package com.rohit.expensetracker.service.impl;

import com.rohit.expensetracker.dto.auth.RegisterRequest;
import com.rohit.expensetracker.dto.auth.RegisterResponse;
import com.rohit.expensetracker.entity.Role;
import com.rohit.expensetracker.entity.User;
import com.rohit.expensetracker.exception.EmailAlreadyExistsException;
import com.rohit.expensetracker.exception.RoleNotFoundException;
import com.rohit.expensetracker.mapper.UserMapper;
import com.rohit.expensetracker.repository.RoleRepository;
import com.rohit.expensetracker.repository.UserRepository;
import com.rohit.expensetracker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE)
                        .orElseThrow( () -> new RoleNotFoundException(DEFAULT_ROLE));

        User user = userMapper.toEntity(request);
        user.setPassword(
                passwordEncoder.encode( request.password() )
        );

        user.getRoles().add(defaultRole);

        User savedUser = userRepository.save(user);

        return userMapper.toRegisterResponse(savedUser);
    }
}
