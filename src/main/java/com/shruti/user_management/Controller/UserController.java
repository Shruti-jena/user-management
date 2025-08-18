package com.shruti.user_management.Controller;

//Spring MVC Annotations
import org.springframework.web.bind.annotation.*;

//For injecting dependencies
import org.springframework.beans.factory.annotation.Autowired;

//Our DTO and Service
import com.shruti.user_management.DTO.UserDTO;
import com.shruti.user_management.Service.UserService;

import java.util.List;

@RestController  //Marks this as a REST API Controller
@RequestMapping("/api/users")  //Base URL path for all endpoints here
public class UserController {

    private final UserService userService;

    @Autowired //Constructor Injection for UserService
    public UserController(UserService userService){
        this.userService =  userService;
    }
    
    //Get all users
    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    //Get User by ID
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    //Create new User
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO){
          return userService.createUser(userDTO);
    }

    //Update a user
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO){
        return userService.updateUser(id, userDTO);
    }

    //Delete a user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }
}
