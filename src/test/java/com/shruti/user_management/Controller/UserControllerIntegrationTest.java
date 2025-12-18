package com.shruti.user_management.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruti.user_management.Config.TestSecurityConfig;
import com.shruti.user_management.DTO.UserDTO;
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

    @Autowired
    ObjectMapper objectMapper;

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

    @Test
    void shouldUpdateUserNameSuccessfully() throws Exception {
    User user = new User();
    user.setName("Old Name");
    user.setEmail("shruti@test.com");
    user.setPassword("encoded");
    User saved = userRepository.save(user);

    UserDTO updateDto = new UserDTO();
    updateDto.setName("New Name");
    updateDto.setEmail("shruti@test.com");
    updateDto.setPassword("new-password");

    mockMvc.perform(
            put("/api/users/{id}", saved.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDto))
    )
    .andExpect(status().isOk());
    User updated = userRepository.findById(saved.getId()).orElseThrow();
    assertEquals("New Name", updated.getName());
}


    @Test
    void shouldReturn404_WhenUserDoesNotExist() throws Exception {
        long nonExistingUserId = 9999L;
        mockMvc.perform(
            get("/api/users/{id}", nonExistingUserId)
                    .contentType(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isNotFound());// not calling DB service layer throws exception converted by mockmvc into response
}

@Test
void shouldReturnEmptyList_WhenNoUsersExist() throws Exception {
    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
}

@Test
void shouldDeleteUserSuccessfully() throws Exception {
    // Arrange – create user
    User user = new User();
    user.setName("Shruti");
    user.setEmail("shruti@test.com");
    user.setPassword("encoded");
    User saved = userRepository.save(user);

    // Act + Assert
    mockMvc.perform(delete("/api/users/{id}", saved.getId()))
            .andExpect(status().isOk());

    // Verify user is gone
    assertFalse(userRepository.findById(saved.getId()).isPresent());
}

}
