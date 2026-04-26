package com.atguigu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RequestMapping("/api/embedding")
@RestController
public class EmbedingController {

    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private ChatClient chatClient;


    @RequestMapping("/add")
    public String add() {
        vectorStore.add(List.of(new Document("朱棣"), new Document("朱标")));
        return "success";
    }


    @RequestMapping("/rag")
    public Flux<String> rag(@RequestParam("message") String message) {
            
        String systemInfo = "你是一名全能助手。请根据提供的参考文档回答问题，但如果问题超出了文档范围，你完全可以利用自己的知识来回答。文档内容仅供参考，不是限制。";
        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .build())
                .build();
        Flux<String> content = chatClient.prompt().system(systemInfo).user(message).advisors(advisor).stream().content();
        return content;
    }

}
