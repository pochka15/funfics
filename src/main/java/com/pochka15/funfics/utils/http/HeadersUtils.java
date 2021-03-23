package com.pochka15.funfics.utils.http;

public class HeadersUtils {
    public static String extractToken(String authHeaderValue) {
        final String bearerPart = "Bearer ";
        return authHeaderValue.substring(bearerPart.length());
    }
}
