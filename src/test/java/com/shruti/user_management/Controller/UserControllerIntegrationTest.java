package com.shruti.user_management.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.shruti.user_management.Config.TestSecurityConfig;
import com.shruti.user_management.Model.User;
import com.shruti.user_management.Repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired 
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    //To test GET /api/users/{id} → 200
    @Test
    void shouldReturnUser_WhenUserExists() throws Exception{
        User user = new User();
        user.setName("Shruti");
        user.setEmail("shruti@test.com");
        user.setPassword("encodedPassword");

        User savedUser = userRepository.save(user);

        mockMvc.perform(
            get("/api/users/{id}", savedUser.getId())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value( "shruti@test.com"))
            .andExpect(jsonPath("$.password").doesNotExist());

    }

    @Test
    void shouldDeleteUser() throws Exception{
        User user = new User();
        user.setName("Delete Me");
        user.setEmail("delete@test.com");
        user.setPassword("encodedPassword");

        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/api/users/{id}",savedUser.getId())
                )
                .andExpect(status().isOk());


    }
}
