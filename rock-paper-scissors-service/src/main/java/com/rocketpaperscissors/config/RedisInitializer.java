package com.rocketpaperscissors.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisInitializer {

    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void init() {
        redisService.connectToRedis(); // Chama o método após a inicialização da aplicação
    }
}
