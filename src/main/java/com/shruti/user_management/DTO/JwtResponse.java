package com.shruti.user_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// DTO used to return the JWT token after a successful login.
@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    
    // The generated JWT token
    private String token;
    
    // Optional: Include user details if needed, but the token is mandatory.
    private String type = "Bearer"; 

    public JwtResponse(String token) {
       this.token=token;
    }

    // Default constructor (recommended)
    public JwtResponse() {
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}