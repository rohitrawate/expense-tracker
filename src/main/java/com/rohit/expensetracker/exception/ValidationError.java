package com.rohit.expensetracker.exception;

public record ValidationError(
        String field,
        String message
  )  { }
