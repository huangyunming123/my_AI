package com.atguigu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;


/**
 * @author pengpei
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Resource(name = "qwModel")
    private ChatModel chatModel;
//
    @Resource(name = "deepSeekClient")
    private ChatClient chatClient;

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

    /**
     * 简化版本：使用 GET 请求测试
     * @param message 用户输入的文本消息
     * @return 大模型返回的响应结果
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        return chatModel.stream(message);
    }
//
    @GetMapping("/client/stream")
    public Flux<String> clientStream(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
         return chatClient.prompt().user( message).stream().content();
    }
//
    @GetMapping("/client/message")
    public String clientMessage(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        return chatClient.prompt().user( message).call().content();
    }


    @GetMapping("/prompt/message")
    public Flux<String> promptMessage(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        return chatClient.prompt()
                //英文不行
//                .system("you are a rawer! only answer about raw question!")
                .system("你是一名法律专家 只回答法律问题 其他的无可奉告")
                .user( message).stream().content();
    }

    @GetMapping("/prompt/message2")
    public Flux<ChatResponse> promptMessage2(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        SystemMessage systemMessage = new SystemMessage("你作为一名法律专家 只回答法律问题 其他的无可奉告");
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatModel.stream(prompt);
    }

    @GetMapping("/prompt/message3")
    public Flux<String> promptMessage3(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        SystemMessage systemMessage = new SystemMessage("你作为一名法律专家 只回答法律问题 其他的无可奉告");
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatModel.stream(prompt).map(chatResponse -> chatResponse.getResult().getOutput().getText());
//        return chatModel.stream(prompt).map(chatResponse -> chatResponse.getResults().get(0).getOutput().getText());
    }

    @GetMapping("/prompt/message4")
    public String promptMessage4(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        AssistantMessage assistantMessage = chatClient.prompt()
                .system("你作为一名法律专家 只回答法律问题 其他的无可奉告")
                .user(message)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
        return assistantMessage.getText();
    }

    @GetMapping("/prompt/message5")
    public Flux<String> promptMessage5(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        Flux<String> response = chatClient.prompt()
                .user(message)
                .stream()
                .content();
        return response;
    }

    @GetMapping("/prompt/template/message1")
    public Flux<String> template1(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                我是一名{work} 今年{}岁 
                """);
        Prompt prompt = promptTemplate.create(Map.of(
                "work","法律专家",
                "age",18
        ));
        return chatClient.prompt(prompt).stream().content();
    }

    //读取文件提示词
    @Value("classpath:/hello.txt")
    private org.springframework.core.io.Resource template;


    @GetMapping("/prompt/template/message2")
    public Flux<String> template2(@RequestParam(name = "topic", defaultValue = "七龙珠") String topic,String format) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                我是一名{work} 今年{}岁 
                """);
        Prompt prompt = promptTemplate.create(Map.of(
                "work","法律专家",
                "age",18
        ));
        return chatClient.prompt(prompt).stream().content();
    }









}
