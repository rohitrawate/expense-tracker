//package com.rohit.expensetracker.aspect;
//
//public class LoggingAspect {
//}
package com.rohit.expensetracker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.rohit.expensetracker.service..*(..))")
    public void logBefore(JoinPoint joinPoint) {

        System.out.println(
                "[LOG] Executing : "
                        + joinPoint.getSignature().getName());

    }

}