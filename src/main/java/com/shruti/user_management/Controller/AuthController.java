package com.shruti.user_management.Controller;

import com.shruti.user_management.DTO.AuthRequest;
import com.shruti.user_management.DTO.AuthResponse;
import com.shruti.user_management.DTO.UserDTO;
import com.shruti.user_management.Service.JwtService;
import com.shruti.user_management.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Handles:
 * - POST /api/auth/signup → create user (password is hashed)
 * - POST /api/auth/login  → authenticate and return JWT
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationmanager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    @Autowired
    public AuthController(AuthenticationManager authenticationmanager,PasswordEncoder passwordEncoder,JwtService jwtService,UserService userService){
            this.authenticationmanager=authenticationmanager;
            this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
            this.userService = userService;
    }
    
    //SIGNUP : Create a new user with hashed password
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody UserDTO request){
        //Hash the plain-text password before saving
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        UserDTO created =  userService.createUser(request);
        return ResponseEntity.status(201).body(created);
    }

    //LOGIN: Verify credentials, then return a JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        try{
            //We treat "email" as the username(principal)
            var authToken =  new UsernamePasswordAuthenticationToken(
                request.getEmail(),request.getPassword()
            );
            authenticationmanager.authenticate(authToken);//throws Exception if invalid

            //If we get here, credentials are valid - issue JWT
            String token = jwtService.generateToken(request.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        catch(AuthenticationException ex){
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
    
}
