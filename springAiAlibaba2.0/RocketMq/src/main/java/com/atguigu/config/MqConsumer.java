package com.atguigu.config;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "demo-topic", consumerGroup = "demo-consumer-group")
public class MqConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("收到消息：" + message);
        // 处理你的业务逻辑
    }
}
