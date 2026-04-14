package com.atguigu.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSAConfig {
    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    // 常用通义千问模型（从百炼平台确认 higher version not match）
    private final String qw = "qwen-plus";
//    private final String qw = "qwen-max";

    private final String deep_seek = "deepseek-r1";


    @Bean(name = "deepSeekModel")
    public ChatModel deepSeekModel() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder()
                        .apiKey(apiKey)
                        .build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(deep_seek).build())
                .build();
    }

    @Bean(name = "qwModel")
    public ChatModel qwModel() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder()
                        .apiKey(apiKey)
                        .build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(qw).build())
                .build();
    }

//
//
    @Bean(name = "deepSeekClient")
    public ChatClient qwClient(@Qualifier("deepSeekModel") ChatModel chatModel ,RedisChatMemoryRepository redisChatMemoryRepository) {
        MessageWindowChatMemory windowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(10)
                .build();

        // 将窗口内存存储器以顾问增强器的形式添加到 ChatClient 中
        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder().model(deep_seek).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(windowChatMemory).build())
                .build();
    }
//
//
    @Bean(name = "qwClient")
    public ChatClient aliClient(@Qualifier("qwModel") ChatModel chatModel , RedisChatMemoryRepository redisChatMemoryRepository) {
        MessageWindowChatMemory windowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(10)
                .build();

        // 将窗口内存存储器以顾问增强器的形式添加到 ChatClient 中
        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder().model(qw).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(windowChatMemory).build())
                .build();
    }

}
