package com.atguigu.controller;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Resource
    private ChatModel chatModel;

    @RequestMapping("/deepSeek")
    public String deepSeek(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        return chatModel.chat( message);
    }
}
