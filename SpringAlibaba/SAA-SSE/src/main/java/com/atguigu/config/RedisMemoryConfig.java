package com.atguigu.config;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedisMemoryConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository()  {
        RedisChatMemoryRepository repository = RedisChatMemoryRepository.builder()
                .host(host)
                .port(port)
                .password(password)
                .build();
        return repository;
    }

//    /**
//     * 自定义键前缀的会话存储仓库
//     */
//    @Bean
//    public ChatMemoryRepository chatMemoryRepository(RedisChatMemoryRepository redisChatMemoryRepository) {
//        return new CustomPrefixChatMemoryRepository(redisChatMemoryRepository);
//    }
}
