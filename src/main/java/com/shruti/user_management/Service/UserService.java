package com.shruti.user_management.Service;

import com.shruti.user_management.DTO.UserDTO;
import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDto);//C
    List<UserDTO> getAllUsers();//R
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDto);//U
    void deleteUser(Long id);//D
} 