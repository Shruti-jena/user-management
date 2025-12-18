package com.shruti.user_management.Service;

import com.shruti.user_management.DTO.UserDTO;
import com.shruti.user_management.Exception.UserNotFoundException;
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
    void shouldThrowException_WhenUserDtoIsNull() {
    assertThrows(IllegalArgumentException.class, () ->
        userServiceImpl.createUser(null)
        );
    }

    @Test
    void shouldThrowException_WhenEmailIsBlank() {
    UserDTO dto = new UserDTO();
    dto.setName("Shruti");
    dto.setEmail(" ");
    dto.setPassword("password");

    assertThrows(IllegalArgumentException.class, () ->
        userServiceImpl.createUser(dto)
    );
}

    @Test
    void shouldThrowException_WhenEmailAlreadyExists() {
        UserDTO dto = new UserDTO();
        dto.setName("Shruti");
        dto.setEmail("shruti@test.com");
        dto.setPassword("password");

        when(userRepository.existsByEmail("shruti@test.com")).thenReturn(true);

        assertThrows(IllegalStateException.class, () ->
            userServiceImpl.createUser(dto)
        );
    }

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
    void shouldThrowUserNotFound_WhenUserDoesNotExist() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () ->
        userServiceImpl.getUserById(1L)
    );
}

@Test
void shouldThrowException_WhenUpdatingEmailAlreadyExists() {
    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setEmail("old@test.com");

    UserDTO updateDto = new UserDTO();
    updateDto.setEmail("new@test.com");

    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    when(userRepository.existsByEmail("new@test.com")).thenReturn(true);

    assertThrows(IllegalStateException.class, () ->
    userServiceImpl.updateUser(1L, updateDto)
    );
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

    @Test
    void shouldThrowUserNotFound_WhenDeletingInvalidUser() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () ->
        userServiceImpl.deleteUser(99L)
    );
}

}
