package com.shruti.user_management.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Used for POST /api/auth/signup
@Getter
@Setter
public class registerRequest {
    
    // We expect email, password, and name from the user.
    
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Password is required.")
    private String password;
}
