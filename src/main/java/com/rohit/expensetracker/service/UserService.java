package com.rohit.expensetracker.service;

import com.rohit.expensetracker.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    boolean existsByEmail(String email);

}