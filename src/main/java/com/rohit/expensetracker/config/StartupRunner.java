package com.rohit.expensetracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("--------------------------------------");
        System.out.println("Expense Tracker Started Successfully");
        System.out.println("Health Endpoint : http://localhost:8080/api/v1/health");
        System.out.println("--------------------------------------");
    }
}