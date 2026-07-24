
package com.rohit.expensetracker.config;

import com.rohit.expensetracker.service.HealthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanInspectorRunner implements CommandLineRunner {

    private final ApplicationContext context;

    public BeanInspectorRunner(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {

        System.out.println("========== APPLICATION BEANS ==========");

        String[] beanNames = context.getBeanDefinitionNames();

        System.out.println("Total Beans : " + beanNames.length);

        for (String bean : beanNames) {

            if (bean.startsWith("health")
                    || bean.startsWith("application")
                    || bean.startsWith("utc")
                    || bean.startsWith("local")) {

                System.out.println(bean);
            }
        }
//        Singleton Bean: Same hashCode() = 1134011961
//        System.out.println(
//                context.getBean(HealthService.class).hashCode());
//        System.out.println(
//                context.getBean(HealthService.class).hashCode());

//      Prototype Bean : Different hashCode()
        RequestTracker one =
                context.getBean(RequestTracker.class);
        RequestTracker two =
                context.getBean(RequestTracker.class);

        System.out.println(one.hashCode());
        System.out.println(two.hashCode());
    }

}