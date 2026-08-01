package com.rohit.expensetracker.service;

import com.rohit.expensetracker.entity.Role;
import java.util.Optional;

public interface RoleService {

    Role save(Role role);

    Optional<Role> findByName(String name);
}

