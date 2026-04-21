package com.atguigu.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {


    @Bean
    public ChatModel getChatModel() {
        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .apiKey("sk-feb9bd5742e14c9f95370ab34e426d33")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .modelName("deepseek-r1")
                .build();

        return openAiChatModel;
    }
}
