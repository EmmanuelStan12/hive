package com.bytebard.sharespace.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Setter
    @Value("${spring.application.name}")
    private String issuer;

    private SecretKey secretKey;

    @Autowired
    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(String username) {
        Date today = new Date();
        long tomorrowInMillis = today.getTime() + (1000 * 60 * 60 * 24); // Add milliseconds in a day
        Date tomorrow = new Date(tomorrowInMillis);

        Claims claims = Jwts.claims().subject(username)
                .issuedAt(today)
                .issuer(issuer)
                .expiration(tomorrow)
                .build();
        return Jwts.builder()
                .claims(claims)
                .expiration(tomorrow)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        var jsonWebToken = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        return jsonWebToken.getPayload().getSubject();
    }
}
