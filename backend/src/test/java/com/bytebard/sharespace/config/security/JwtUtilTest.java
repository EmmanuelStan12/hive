package com.bytebard.sharespace.config.security;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        jwtUtil = new JwtUtil();
        jwtUtil.setIssuer("TestIssuer");
        jwtUtil.setSecretKey(Jwts.SIG.HS256.key().build());
    }

    @Test
    public void test_generateToken_1() {
        String username = "test.user";
        String token = jwtUtil.generateToken(username);
        assertNotNull(token); // Assert token is not null
        assertFalse(token.isEmpty()); // Assert token has some length
    }

    @Test
    public void test_generateToken_2() {
        String username = "test.user";
        String token1 = jwtUtil.generateToken(username);
        String token2 = jwtUtil.generateToken(username);
        assertEquals(token1, token2);
    }

    @Test
    public void test_validateToken_1() {
        String username = "test.user";
        String token = jwtUtil.generateToken(username);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    public void test_validateToken_2() {
        assertFalse(jwtUtil.validateToken("test.user"));
    }

    @Test
    public void test_getUsernameFromToken() {
        String username = "test.user";
        String token = jwtUtil.generateToken(username);
        assertEquals(username, jwtUtil.getUsernameFromToken(token));
    }
}
