package com.atguigu.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel , ToolCallbackProvider toolCallbackProvider) {
        return ChatClient.builder(chatModel)
                //mcp协议: 配置减yml文件  此处只赋能给chatClient对象
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
                .build();


    }
}
