package com.atguigu.config;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "demo-topic",          // 主题
        consumerGroup = "demo-group", // 消费组
        selectorExpression = "order_pay" // 只消费这个tag
)
public class TagPayConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("===== 收到订单支付消息 =====");
        System.out.println("消息内容：" + message);
    }
}