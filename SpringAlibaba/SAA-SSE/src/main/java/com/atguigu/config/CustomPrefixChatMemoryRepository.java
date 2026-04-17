package com.atguigu.config;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * 自定义 Redis 会话存储仓库
 * 通过反射修改 RedisChatMemoryRepository 的内部前缀
 */
public class CustomPrefixChatMemoryRepository implements ChatMemoryRepository {

    private final RedisChatMemoryRepository delegate;

    public CustomPrefixChatMemoryRepository(RedisChatMemoryRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> findConversationIds() {
        return delegate.findConversationIds();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        return delegate.findByConversationId(conversationId);
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        delegate.saveAll(conversationId, messages);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        delegate.deleteByConversationId(conversationId);
    }
}
