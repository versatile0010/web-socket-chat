package com.example.websocketchat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {
    @Value("${spring.redis.port}")
    private int redisPort;
    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        // 채팅 서버가 시작되면 내장 Redis 서버도 실행되도록
        if (Objects.isNull(redisServer) || !redisServer.isActive()) {
            redisServer = RedisServer.builder()
                    .port(redisPort)
                    .setting("maxmemory 128M")
                    .build();
            redisServer.start();
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (!Objects.isNull(redisServer)) {
            redisServer.stop();
        }
    }
}
