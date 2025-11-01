package com.shruti.user_management.Service;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    //Secret Key for signing the token
    @Value("${jwt.secret}") //secret key from application.properties
    private String secret;

    @Value("${jwt.expiration}") //expiration time in ms from application.properties
    private long jwtExpiration;

    // Extract username
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Generic Claim Extractor
    public <T> T extractClaim(String token,Function<Claims,T> claimsResolver){
        final Claims claims = extractAll(Claims(token));
        return claimsResolver.apply(claims);
    }

    //Generate token with only username
    public String generateToken(String username){
        return generateToken(new HashMap<>(), username);
    }

    //Generate token with extra claims
    public String generateToken(Map<String,Object> extraClaims,String username){
        return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
    }

    //Validate token against username
    public boolean isTokenValid(String token, String username){
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

    //Check if token expired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    //Parse all claims
    private Claims extractAllClaims(String token){
        return Jwts.parseBuilder()
                   .setSigningKey(getSignInKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    //Decode base secret and create signing key
    private key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
