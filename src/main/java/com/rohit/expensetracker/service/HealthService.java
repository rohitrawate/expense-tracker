package com.rohit.expensetracker.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public String getStatus() {
        return "Expense Tracker is running successfully!";
    }
}