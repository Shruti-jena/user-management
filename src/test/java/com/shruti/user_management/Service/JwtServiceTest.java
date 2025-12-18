package com.shruti.user_management.Service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        String rawSecret = "my-super-secret-key-my-super-secret-key";
        String base64Secret = Base64.getEncoder().encodeToString(rawSecret.getBytes());
        ReflectionTestUtils.setField(jwtService, "secret", base64Secret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1000 * 60 * 60); 
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String email = "shruti@test.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertEquals(email, jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, email));
        assertFalse(jwtService.isTokenExpired(token));
    }
}
