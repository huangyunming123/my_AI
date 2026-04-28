package com.atguigu.controller;

import com.alibaba.cloud.ai.advisor.DocumentRetrievalAdvisor;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/saa2")
public class SAA2Controller {

    @Resource
    private ChatClient client;
    @Resource
    private DashScopeApi dashScopeApi;


    @RequestMapping("/saa2")
    public Flux<String> saa2() {
        return client
                .prompt("hello world")
                .stream()
                .content();
    }

    @RequestMapping("/test")
    public Flux<String> test(@RequestParam(value = "message",defaultValue = "你是谁") String message) {
        DashScopeDocumentRetriever dashScopeDocumentRetriever = new DashScopeDocumentRetriever(dashScopeApi, DashScopeDocumentRetrieverOptions.builder()
                .indexName("ai智能助手").build());
        return client.prompt()
                .user(message)
                .advisors(new DocumentRetrievalAdvisor(dashScopeDocumentRetriever))
                .stream()
                .content();
    }
}
