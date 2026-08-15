package com.rohit.expensetracker.exception;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException(){
        super("Invalid input");
    }
}
