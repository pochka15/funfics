package com.pochka15.funfics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${front.url}")
    private String frontOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("Configured front origin CORS: " + frontOrigin);
        registry.addMapping("/**")
                .allowedOrigins(frontOrigin);
    }
}