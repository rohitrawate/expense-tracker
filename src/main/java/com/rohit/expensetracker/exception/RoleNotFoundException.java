package com.rohit.expensetracker.exception;

public class RoleNotFoundException extends  RuntimeException {

    public RoleNotFoundException(String roleName) {
        super("Role not found: "+  roleName);
    }
}
