package com.atguigu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private ChatModel chatModel;

//    @Resource
//    private ChatClient chatClient;

    /**
     * 调用大模型进行文本对话
     * @param message 用户输入的文本消息
     * @return 大模型返回的响应结果
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        // 调用大模型
        return chatModel.call(message);
    }

//    /**
//     * 简化版本：使用 GET 请求测试
//     * @param message 用户输入的文本消息
//     * @return 大模型返回的响应结果
//     */
//    @GetMapping("/stream")
//    public Flux<String> stream(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
//        return chatModel.stream(message);
//    }
//
//    @GetMapping("/client/stream")
//    public Flux<String> clientStream(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
//         return chatClient.prompt().user( message).stream().content();
//    }
//
//    @GetMapping("/client/message")
//    public String clientMessage(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
//        return chatClient.prompt().user( message).call().content();
//    }
}
