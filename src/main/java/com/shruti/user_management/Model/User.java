package com.shruti.user_management.Model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Primary Key

    private String name;
    private String email;
    private String password;
    private String role;

    //Default Constructor By JPA
    public User() {
    }

    //Parameterised Constructor
    public User(String name, String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

     // --- USERDETAILS IMPLEMENTATION (Required by Spring Security) ---

    /**
     * CRITICAL FIX: This method provides the user's roles to Spring Security.
     * We grant a default "ROLE_USER" to allow the user to pass the 
     * .anyRequest().authenticated() check.
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    //Getters & Setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }
    
    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRole(String role) {
        this.role=role;
    }

    public String getRole() {
        return role;    
    }

}
