package com.atguigu.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class RAGInitConfig {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("classpath:ops.txt")
    private org.springframework.core.io.Resource resource;


    @PostConstruct
    public void init() {
        TextReader textReader = new TextReader(resource);
        textReader.setCharset(Charset.defaultCharset());
        List<Document> transform = new TokenTextSplitter().transform(textReader.read());

        // 清除旧的标记，重新加载数据（调试用）
        String source = textReader.getCustomMetadata().get("source").toString();
        String key = "vectore-distinct:" + source;
        
        // 删除旧标记，强制重新加载
        redisTemplate.delete(key);
        
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key, "1");
        if(b){
            vectorStore.add(transform);
            System.out.println("✅ 向量数据加载成功，文档数量: " + transform.size());
        } else {
            System.out.println("⚠️ 数据已存在，跳过加载");
        }
    }
}
