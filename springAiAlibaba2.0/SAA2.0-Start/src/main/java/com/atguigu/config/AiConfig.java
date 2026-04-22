package com.atguigu.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    String deep_seek = "deepseek-r1";

    @Bean
    public DashScopeApi getDashScopeApi() {
        return DashScopeApi.builder()
                .apiKey("sk-feb9bd5742e14c9f95370ab34e426d33")
                .build();
    }


    @Bean(name = "deepSeekModel")
    public ChatModel deepSeekModel(DashScopeApi dashScopeApi) {
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(DashScopeChatOptions.builder().withModel(deep_seek).build())
                .build();
    }


    @Bean(name = "deepSeekClient")
    public ChatClient qwClient(@Qualifier("deepSeekModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .build();
    }



}
