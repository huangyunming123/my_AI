package com.atguigu.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SAALLMConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel , ToolCallbackProvider toolCallbackProvider) {

        return ChatClient.builder(chatModel)
                // 设置默认的顾问增强器 调用的就是配置文件配置的地址
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }
}
