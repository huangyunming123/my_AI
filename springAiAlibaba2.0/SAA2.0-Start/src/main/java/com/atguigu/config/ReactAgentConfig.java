package com.atguigu.config;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactAgentConfig {

    @Bean
    public ReactAgent reactAgent(@Qualifier("deepSeekModel") ChatModel chatModel) {
        return ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .instruction("你是一个天气机器人助手")
                .build();
    }
}
