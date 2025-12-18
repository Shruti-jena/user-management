// Package Declaration
package com.shruti.user_management.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

// Validation Annotations
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//Lombok to generate boiler plate code
import lombok.Data;

@Data
public class UserDTO {

private Long id;

@NotBlank(message = "Name is required")
private String name;

@Email(message = "Email should be  valid")
@NotBlank(message = "Email is required" )
private String email;

@NotBlank(message = "Passowrd is required")
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private String password;

private String role;
}
