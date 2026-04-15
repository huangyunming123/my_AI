package com.atguigu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/chatMemory")
public class ChatMemoryController {

    @Resource(name = "deepSeekClient")
    private ChatClient client;

    @RequestMapping(value = "/message", method = {RequestMethod.GET, RequestMethod.POST})
    public Flux<String> message(String message, String userId) {
        return client.prompt(message).advisors(new Consumer<ChatClient.AdvisorSpec>() {
            @Override
            public void accept(ChatClient.AdvisorSpec advisorSpec) {
                //记录你的回话id
                advisorSpec.param(CONVERSATION_ID, userId);
            }
        }).stream().content();
    }


}
