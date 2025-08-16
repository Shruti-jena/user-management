package com.shruti.user_management.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id; //Primary Key

    private String name;
    private String email;
    private String password;

    //Default Constructor By JPA
    public User() {
    }

    //Parameterised Constructor
    public User(String name, String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Getters & Setters
    public Long getId(){
        return Id;
    }

    public void setId(Long Id){
        this.Id = Id;
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

}
