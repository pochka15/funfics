package com.pochka15.funfics.service;

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

import static org.apache.commons.lang3.time.DateUtils.addHours;

@Component
public class DefaultJwtService implements JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    public String extractUsername(String token)
            throws io.jsonwebtoken.MalformedJwtException,
            io.jsonwebtoken.SignatureException,
            io.jsonwebtoken.ExpiredJwtException {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
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

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(addHours(new Date(System.currentTimeMillis()), 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}
