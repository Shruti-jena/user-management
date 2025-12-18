package com.shruti.user_management.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruti.user_management.Config.TestSecurityConfig;
import com.shruti.user_management.DTO.LoginRequest;
import com.shruti.user_management.DTO.RegisterRequest;
import com.shruti.user_management.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Shruti");
        request.setEmail("shruti@test.com");
        request.setPassword("Password123");

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldLoginSuccessfully_AndReturnJWT() throws Exception {
        // Register first
        RegisterRequest register = new RegisterRequest();
        register.setName("Shruti");
        register.setEmail("shruti@test.com");
        register.setPassword("Password123");

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        ).andExpect(status().isCreated());

        // Login
        LoginRequest login = new LoginRequest();
        login.setEmail("shruti@test.com");
        login.setPassword("Password123");

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists());
    }
}

