package com.shruti.user_management.Service;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import java.util.function.Function;
import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    //Secret Key for signing the token
    @Value("${jwt.secret}") //secret key from application.properties
    private String secret;

    @Value("${jwt.expiration}") //expiration time in ms from application.properties
    private long jwtExpiration;

   //Decodes the base64 Secret key and creates the signing key
   private Key getSignInKey(){
    byte[] keyBytes =  Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
    }

    //Public Method for simple token generation
    public String generateToken(String username)
    {
        Map<String, Object> claims =  new HashMap<>();
        return createToken(claims,username);
    }

    //Private method to build the token String
     private String createToken(Map<String,Object> claims, String username)
    {
        return Jwts.builder()
                    .setClaims(claims) //Custom data/roles'
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                    .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                    .compact();
    }

    //Implement Claim Extraction to read back data from token
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Public generic method to extract any single Claim
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Public generic method to extracty the username(subject)
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Public generic method to extracty the username(subject)
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //Implement Token Validation

    //Checks if token expiration date has crossed current time
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //Checks if the token is valid for given username and hasnt expired
    public boolean isTokenValid(String token, String username){
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
