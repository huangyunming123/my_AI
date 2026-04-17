package com.atguigu.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/embed2Vector")
public class Embed2VectorController {

    @Resource
    private EmbeddingModel embeddingModel;
//
    @Resource
    private VectorStore vectorStore;


    @RequestMapping("/message")
    public String embed2Vector(String message) {
        EmbeddingResponse embeddingResponse = embeddingModel.call(new EmbeddingRequest(List.of(message)
                , DashScopeEmbeddingOptions.builder().withModel("text-embedding-v3").build()));

        return Arrays.toString(embeddingResponse.getResult().getOutput());
    }

    @RequestMapping("/add")
    public String embed2Vector() {
//   VectorStore 之所以添加文本就自动向量化，是因为：
//✅ 依赖注入：VectorStore 内部注入了 EmbeddingModel
//✅ 封装逻辑：add() 方法内部自动调用了 embeddingModel.embed()
//✅ 一站式服务：向量化 + 存储 + 索引，一步搞定
//✅ 自动配置：Spring AI 根据配置文件自动完成所有装配
//        这就是 Spring AI 的约定优于配置理念——你只需要 add()，框架帮你搞定剩下的所有事情！🎉
        vectorStore.add(
        List.of(new Document("朱棣"), new Document("朱标")));
        return "success";
    }

    @RequestMapping("/query")
    public  List<Document> query(String message) {
        SearchRequest req =  SearchRequest.builder()
                .query( message)
                .topK(2).build();
        return vectorStore.similaritySearch(req);
    }
}
