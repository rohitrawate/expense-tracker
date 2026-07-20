package com.rohit.expensetracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("==================================");
        System.out.println("Expense Tracker Started");
        System.out.println("Spring Boot is Ready!");
        System.out.println("==================================");
    }
}