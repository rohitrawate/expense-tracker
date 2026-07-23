package com.rohit.expensetracker.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class LocalTimeService implements TimeService {

    @Override
    public String currentTime() {
        return LocalDateTime.now().toString();
    }

}