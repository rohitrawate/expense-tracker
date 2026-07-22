package com.rohit.expensetracker.service;


import org.springframework.stereotype.Service;

@Service
public class ApplicationInfoService {

    public String applicationName() {
        return "Expense Tracker";
    }

    public String version() {
        return "0.0.1-SNAPSHOT";
    }
}