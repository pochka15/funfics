package com.pochka15.funfics.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token)
            throws io.jsonwebtoken.MalformedJwtException,
            io.jsonwebtoken.SignatureException,
            io.jsonwebtoken.ExpiredJwtException;

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
            throws io.jsonwebtoken.MalformedJwtException,
            io.jsonwebtoken.SignatureException,
            io.jsonwebtoken.ExpiredJwtException;

    String generateToken(UserDetails userDetails);
}
