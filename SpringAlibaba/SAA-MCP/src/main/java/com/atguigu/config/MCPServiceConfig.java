package com.atguigu.config;

import com.atguigu.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MCPServiceConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(WeatherService weatherService) {
       return MethodToolCallbackProvider.builder()
               .toolObjects(weatherService)
               .build();
    }

}
