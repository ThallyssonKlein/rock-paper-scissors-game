package com.rocketpaperscissors.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> envVars = new HashMap<>();
        envVars.put("spring.datasource.url", System.getenv("MYSQL_DATABASE_URL"));
        envVars.put("spring.datasource.username", System.getenv("MYSQL_DATABASE_USERNAME"));
        envVars.put("spring.datasource.password", System.getenv("MYSQL_DATABASE_PASSWORD"));

        environment.getPropertySources().addFirst(new MapPropertySource("customEnvVars", envVars));
    }
}


