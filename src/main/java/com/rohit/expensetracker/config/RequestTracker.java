package com.rohit.expensetracker.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RequestTracker {

    public RequestTracker() {
        System.out.println(
                "Prototype Bean : "
                        + hashCode());
    }

}