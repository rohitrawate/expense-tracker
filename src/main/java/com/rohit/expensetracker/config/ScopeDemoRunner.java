package com.rohit.expensetracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ScopeDemoRunner implements CommandLineRunner {

    private final ApplicationContext context;

    public ScopeDemoRunner(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {

        System.out.println(
                context.getBean(RequestTracker.class));

        System.out.println(
                context.getBean(RequestTracker.class));

        System.out.println(
                context.getBean(RequestTracker.class));

    }

}