package com.rohit.expensetracker.controller;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohit.expensetracker.config.PostgresTestContainerConfig;
import com.rohit.expensetracker.dto.auth.LoginRequest;
import com.rohit.expensetracker.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import tools.jackson.databind.ObjectMapper;
import com.rohit.expensetracker.dto.auth.RegisterRequest;
import com.rohit.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestContainerConfig.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;


    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        String email = "Demo.integr@example.com";

        RegisterRequest request =
                new RegisterRequest(
                        "Demo",
                        "Patil",
                        email,
                        "Spring@123"
                );

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(
                                MediaType.APPLICATION_JSON
                        ),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message")
                                .value("User registered successfully"),
                        jsonPath("$.data.uuid").exists(),
                        jsonPath("$.data.firstName").value("Demo"),
                        jsonPath("$.data.lastName").value("Patil"),
                        jsonPath("$.data.email")
                                .value(email),
                        jsonPath("$.data.password").doesNotExist()
                );

        var user = userRepository
                .findByEmail(email)
                .orElseThrow();

        assertNotEquals(
                "Spring@123",
                user.getPassword()
        );

        assertTrue(
                passwordEncoder.matches(
                        "Spring@123",
                        user.getPassword()
                )
        );
    }

    @Test
    void shouldRejectInvalidRegistrationRequest() throws Exception {

        RegisterRequest request =
                new RegisterRequest(
                        "",
                        "Rawate",
                        "invalid-email",
                        "123"
                );

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message")
                                .value("ValidationError"),
                        jsonPath("$.errors").isArray()
                );
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {

        String email = "rohit.duplicate@example.com";

        RegisterRequest firstRequest =
                new RegisterRequest(
                        "Rohit",
                        "Rawate",
                        email,
                        "Spring@123"
                );

        /* First registration should succeed. */
        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content( objectMapper.writeValueAsString(firstRequest)
                                )
                ).andExpect(status().isCreated());

        /*Second registration with the same email should be rejected. */
        RegisterRequest duplicateRequest =
                new RegisterRequest(
                        "Another",
                        "User",
                        email,
                        "Spring@123"
                );

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(duplicateRequest)
                                )
                ).andExpectAll(
                        status().isConflict(),
                        jsonPath("$.success").value(false)
                );
    }

    @Test
    void shouldStorePasswordAsEncodedValue() throws Exception {

        String email =
                "rohit.password@example.com";

        String rawPassword =
                "Spring@123";

        RegisterRequest request =
                new RegisterRequest(
                        "Rohit",
                        "Rawate",
                        email,
                        rawPassword
                );

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString( request )
                        )
        ).andExpect(
                status().isCreated()
        );

        var savedUser =
                userRepository.findByEmail(email)
                        .orElseThrow();

        /* The raw password must never be stored. */
        assertThat(savedUser.getPassword())
                .isNotEqualTo(rawPassword);

        /*The encoded password must successfully match the original raw password.  */
        assertThat(
                passwordEncoder.matches(
                        rawPassword,
                        savedUser.getPassword()
                )
        ).isTrue();
    }

    @Test
    void shouldAssignDefaultUserRoleAfterRegistration() throws Exception {

        String email = "role-test@example.com";

        RegisterRequest request = new RegisterRequest(
                                "Rohit",
                                "Rawate",
                                         email,
                                "Spring@123"
                        );

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(
                        status().isCreated()
                );

        var user = userRepository
                .findWithRolesByEmail(email)
                .orElseThrow();

        assertTrue(
                user.getRoles().stream()
                        .anyMatch(role -> "ROLE_USER".equals(role.getName()))
        );
    }

    @Test
    void shouldAllowOnlyOneUserWhenTwoRequestsUseSameEmailConcurrently()
            throws Exception {

        String email = "concurrent@example.com";

        RegisterRequest request =
                new RegisterRequest(
                        "Rohit",
                        "Rawate",
                        email,
                        "Spring@123"
                );

        CountDownLatch startLatch = new CountDownLatch(1);

        try (ExecutorService executor =
                     Executors.newFixedThreadPool(2)) {

            Future<Integer> firstRequest =
                    executor.submit(() -> {

                        startLatch.await();

                        return mockMvc.perform(
                                        post("/api/v1/auth/register")
                                                .contentType(
                                                        MediaType.APPLICATION_JSON
                                                )
                                                .content(
                                                        objectMapper
                                                                .writeValueAsString(
                                                                        request
                                                                )
                                                )
                                )
                                .andReturn()
                                .getResponse()
                                .getStatus();
                    });

            Future<Integer> secondRequest =
                    executor.submit(() -> {

                    startLatch.await();

                    return mockMvc.perform(
                                post("/api/v1/auth/register")
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(
                                                objectMapper
                                                        .writeValueAsString(
                                                                request
                                                        )
                                        )
                        )
                                .andReturn()
                                .getResponse()
                                .getStatus();
                    });

            startLatch.countDown();

            int firstStatus = firstRequest.get();
            int secondStatus = secondRequest.get();

            Assertions.assertEquals(1, userRepository.countByEmail(email));

            Assertions.assertEquals(201, firstStatus == 201 ? firstStatus : secondStatus);

            Assertions.assertEquals(409, firstStatus == 409 ? firstStatus : secondStatus);
        }
    }

    @Test
    void shouldRejectLoginWithInvalidPassword() throws Exception {

        LoginRequest request =
                new LoginRequest(
                        "registered@example.com",
                        "WrongPassword"
                );

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectLoginForUnknownEmail() throws Exception {

        LoginRequest request =
                new LoginRequest(
                        "does-not-exist@example.com",
                        "Spring@123"
                );

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAuthenticateValidUser() throws Exception {

        String email = "login-success@example.com";
        String password = "Spring@123";

        RegisterRequest registerRequest =
                new RegisterRequest(
                        "Rohit",
                        "Rawate",
                        email,
                        password
                );

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        registerRequest
                                )
                        )
        ).andExpect(
                status().isCreated()
        );

        LoginRequest loginRequest =
                new LoginRequest(
                        email,
                        password
                );

        Authentication authentication =
                authenticationService.authenticate(loginRequest);

        Assertions.assertTrue(authentication.isAuthenticated());

        Assertions.assertEquals(
                email,
                authentication.getName()
        );

        Assertions.assertTrue(
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                "ROLE_USER".equals(
                                        authority.getAuthority()
                                )
                        )
        );
    }

}