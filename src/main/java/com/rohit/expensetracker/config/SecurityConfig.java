package com.rohit.expensetracker.config;

import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                .authorizeHttpRequests(auth ->
                    auth.requestMatchers(
                            "/api/v1/auth/**",
                            "/actuator/health"
                    ).permitAll()
                     .anyRequest()
                     .authenticated()
            )
             .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {

        UserDetails user1 =
                User.withUsername("rohit")
                        .password( passwordEncoder().encode("password") )
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user1);
    }

}
