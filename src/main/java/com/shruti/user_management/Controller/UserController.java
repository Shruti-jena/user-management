package com.shruti.user_management.Controller;

//Spring MVC Annotations
import org.springframework.web.bind.annotation.*;

//For injecting dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//Our DTO and Service
import com.shruti.user_management.DTO.UserDTO;
import com.shruti.user_management.Service.UserService;

import jakarta.validation.Valid;

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
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id){
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    //Create new User
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
         UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //Update a user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO){
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    //Delete a user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }
    @GetMapping("/me")
public UserDTO getAuthenticatedUser(Authentication authentication){
    // 'authentication.getName()' retrieves the email (the subject of the JWT)
    String authenticatedEmail = authentication.getUsername();
    
    // You'd need to add a new method in UserService to find a user by email
    // Since you don't have that, we'll use a placeholder for now:
    // return userService.getUserByEmail(authenticatedEmail); 
    
    // Placeholder using existing service methods (replace with findByEmail later)
    // NOTE: This is a placeholder and may not be feasible without findByEmail
    // You would typically call a new service method like:
    // return userService.getUserByEmail(authenticatedEmail);
    
    return userService.getAllUsers().stream()
        .filter(user -> user.getEmail().equals(authenticatedEmail))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
}
}
