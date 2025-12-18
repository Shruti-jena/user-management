package com.shruti.user_management.Service;

import com.shruti.user_management.DTO.UserDTO;
import com.shruti.user_management.Model.User;
import com.shruti.user_management.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void shouldReturnUserById_WhenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Shruti");
        user.setEmail("shruti@test.com");
        user.setPassword("encoded");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userServiceImpl.getUserById(1L);

        assertNotNull(result);
        assertEquals("Shruti", result.getName());
        assertEquals("shruti@test.com", result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userServiceImpl.getUserById(99L)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("not"));
    }
}
