package com.atguigu.controller;

import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private DeepSeekChatModel chatModel;

    @RequestMapping("/deepSeek")
    public String deepSeek(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        //要付钱才能用
        return chatModel.call(message);
    }
}
