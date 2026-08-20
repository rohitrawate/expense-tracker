package com.rohit.expensetracker.service.impl;

import com.rohit.expensetracker.dto.auth.LoginRequest;
import com.rohit.expensetracker.dto.auth.LoginResponse;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.email(),
                        request.password()
                );

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        User user = (User) authentication.getPrincipal();
        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toUnmodifiableSet());

        return new LoginResponse(
                user.getUuid(),
                user.getEmail(),
                roles
        );
    }
}
