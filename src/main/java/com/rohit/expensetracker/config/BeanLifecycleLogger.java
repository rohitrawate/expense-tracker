package com.rohit.expensetracker.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanLifecycleLogger implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(
            Object bean,
            String beanName)
            throws BeansException {

        if (beanName.startsWith("health")
//                || beanName.startsWith("application")
                || beanName.startsWith("utc")
                || beanName.startsWith("local")) {

            System.out.println(
                    "[Before] "
                            + beanName
                            + " -> "
                            + bean.getClass().getSimpleName());
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName)
            throws BeansException {

        if (beanName.startsWith("health")
//                || beanName.startsWith("application")
                || beanName.startsWith("utc")
                || beanName.startsWith("local")) {

            System.out.println(
                    "[After] "
                            + beanName
                            + " -> "
                            + bean.getClass().getSimpleName());
        }

        return bean;
    }

}