package com.pochka15.funfics.config.filter;

import com.pochka15.funfics.service.JwtService;
import com.pochka15.funfics.service.users.DbUserDetailsService;
import com.pochka15.funfics.utils.http.HeadersUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.pochka15.funfics.utils.http.HeadersUtils.extractToken;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public JwtRequestFilter(DbUserDetailsService userDetailsService,
                            JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .map(HeadersUtils::extractToken)
                .map(jwtService::extractUsername)
                .map(userDetailsService::loadUserByUsername)
                .ifPresent(userDetails -> SecurityContextHolder.getContext()
                        .setAuthentication(tokenForAuthenticationContext(userDetails, request)));
        chain.doFilter(request, response);
    }

    private Authentication tokenForAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        var token = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return token;
    }
}