package com.rohit.expensetracker.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    @Around("execution(* com.rohit.expensetracker.service..*(..))")
    public Object measure(ProceedingJoinPoint pjp)
            throws Throwable {

        long start = System.currentTimeMillis();

        Object result = pjp.proceed();

        long end = System.currentTimeMillis();

        System.out.println(
                pjp.getSignature().getName()
                        + " took "
                        + (end - start)
                        + " ms");

        return result;
    }
}