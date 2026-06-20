package com.taskmanagementsystem.security.jwt;

import com.taskmanagementsystem.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private int jwtExpirationsMs;

    // We are removing the bearer from the request and returning the token
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Generates the token
    public String generateToken(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
        return Jwts.builder().subject(username).claim("roles", roles).issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationsMs))).signWith(getSecretKey()).compact();
    }

    // Key method for signing the JWT token
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) getSecretKey()).build().parseSignedClaims(token).getPayload()
                .getSubject();
        // subject is going to have the username because while creating token we have
        // embedded username in the token
    }

    public Boolean validateToken(String authToken){
        try {
            Jwts.parser().verifyWith((SecretKey) getSecretKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
