package com.example.PartTrip.config;

import org.springframework.context.annotation.Configuration;


@Configuration
public class SecurityConfig {

    private final CorsConfig corsConfig;

    public SecurityConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

}