package com.shruti.user_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;  

    public String getToken(){
        return token;
    }
}
