package com.atguigu.controller;


import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/mcp")
public class McpController {

    @Resource
    private ChatClient client;

    @Resource
    private ChatModel model;


    @RequestMapping("/mcp")
    public Flux<String> mcp(@RequestParam("message") String message) {
    return client.prompt(message).stream().content();
    }


    @RequestMapping("/mcp2")
    public Flux<String> mcp2(@RequestParam("message") String message) {
       return model.stream( message);
    }

}
