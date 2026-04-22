package com.atguigu.controller;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
public class SAA2Controller {

    @Resource
    private ChatClient client;


    @Resource
    private ReactAgent agent;


    @RequestMapping("/saa1")
    public Flux<String> saa2(@RequestParam(value = "message", defaultValue = "你是谁") String message) {

        return client
                .prompt(message)
                .stream()
                .content();
    }


    @RequestMapping("/saa2")
    public String saa2Stream(@RequestParam(value = "message", defaultValue = "你是谁") String message) throws GraphRunnerException {
        // 使用 ReactAgent 的流式调用
        String text = agent.call(message).getText();
        return text;
    }
}
