package com.nobelcareers.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Retryable(
            value = { RedisConnectionFailureException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))
    public void connectToRedis() throws RedisConnectionFailureException {
        try {
            RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
            if (connectionFactory == null) {
                throw new RedisConnectionFailureException("A fábrica de conexões Redis não está disponível.");
            }

            RedisConnection connection = connectionFactory.getConnection();
            if (connection == null) {
                throw new RedisConnectionFailureException("Não foi possível obter uma conexão Redis.");
            }

            // Verifica a conectividade com o comando PING
            String pingResponse = connection.ping();
            if (!"PONG".equals(pingResponse)) {
                throw new RedisConnectionFailureException("Falha ao verificar a conectividade com Redis. Resposta: " + pingResponse);
            }

            log.info("Conexão com Redis bem-sucedida! Resposta: " + pingResponse);
        } catch (RedisConnectionFailureException ex) {
            log.warn("Falha ao conectar ao Redis. Tentando novamente...");
            throw ex;
        }
    }
}
