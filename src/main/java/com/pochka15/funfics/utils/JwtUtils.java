package com.pochka15.funfics.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token)
            throws io.jsonwebtoken.MalformedJwtException,
            io.jsonwebtoken.SignatureException,
            io.jsonwebtoken.ExpiredJwtException {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
            throws io.jsonwebtoken.MalformedJwtException,
            io.jsonwebtoken.SignatureException,
            io.jsonwebtoken.ExpiredJwtException {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token)
            throws io.jsonwebtoken.MalformedJwtException,
            io.jsonwebtoken.SignatureException,
            io.jsonwebtoken.ExpiredJwtException {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        final int hourToMills = 1000 * 60 * 60;
        final int tenHours = hourToMills * 10;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tenHours))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}
