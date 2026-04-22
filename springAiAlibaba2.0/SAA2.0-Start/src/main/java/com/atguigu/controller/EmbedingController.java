package com.atguigu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/embedding")
@RestController
public class EmbedingController {

    @Autowired
    private VectorStore vectorStore;

    //todo  有冲突 可能不是这么用了

    @RequestMapping("/add")
    public String add() {
        vectorStore.add(List.of(new Document("朱棣"), new Document("朱标")));
        return "success";
    }



}
