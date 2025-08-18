// Package Declaration
package com.shruti.user_management.DTO;

// Validation Annotations
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//Lombok to generate boiler plate code
import lombok.Data;

@Data
public class UserDTO {

@NotBlank(message = "Id is required")
private Long id;

@NotBlank(message = "Name is required")
private String name;

@Email(message = "Email should be  valid")
@NotBlank(message = "Email is required" )
private String email;

@NotBlank(message = "Passowrd is required")
private String password;
}
