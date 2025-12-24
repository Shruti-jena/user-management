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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "User APIs", description = "User management operations")
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

    @Operation(summary = "Get user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    //Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id){
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    //Create new User
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
         UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Update an existing user",
        description = "Updates user details by user ID. Only provided fields will be updated."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    //Update a user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,@RequestBody UserDTO userDTO) {  
    UserDTO updatedUser = userService.updateUser(id, userDTO);
    return ResponseEntity.ok(updatedUser);
    }

    @Operation(
        summary = "Delete a user",
        description = "Deletes a user by user ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
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
