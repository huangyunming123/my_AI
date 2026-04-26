package com.atguigu.controller;

import com.atguigu.service.TimeToolService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/tool")
public class ToolCallingController {

    @Autowired
    private ChatClient chatClient;


    @RequestMapping("/call")
    public Flux<String> callTool(@RequestParam(value = "message",defaultValue = "你是谁,现在几点了") String message) {
        return chatClient.prompt().tools(new TimeToolService())
                .user(message)
                .stream()
                .content();
    }
}
