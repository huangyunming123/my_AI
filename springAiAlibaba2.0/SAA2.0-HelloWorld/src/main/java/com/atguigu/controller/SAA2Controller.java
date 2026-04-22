package com.atguigu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/saa2")
public class SAA2Controller {

    @Resource
    private ChatClient client;
    @RequestMapping("/saa2")
    public Flux<String> saa2() {
        return client
                .prompt("hello world")
                .stream()
                .content();
    }
}
