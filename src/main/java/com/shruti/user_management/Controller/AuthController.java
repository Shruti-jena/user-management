package com.shruti.user_management.Controller;

import com.shruti.user_management.DTO.JwtResponse;
import com.shruti.user_management.DTO.loginRequest;
import com.shruti.user_management.DTO.registerRequest;
import com.shruti.user_management.Model.User;
import com.shruti.user_management.Repository.UserRepository;
import com.shruti.user_management.Service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // Handles user registration
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody registerRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        // Store password securely
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    // Handles user login and token generation (FIXED)
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody loginRequest loginRequest) {

        // 1. Authenticate the user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 2. Set the authentication object in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate the JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails.getUsername());

        // 4. Return the token in the response DTO
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
    }
}