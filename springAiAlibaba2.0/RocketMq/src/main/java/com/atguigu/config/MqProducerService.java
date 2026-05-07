package com.atguigu.config;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqProducerService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendSimpleMessage(String topic, String message) {
        // 发送普通消息
        rocketMQTemplate.convertAndSend(topic, message);
    }

    public String sendWithTag(String topic, String tag) {

        // 3. 消息内容
        String content = "这是一条带Tag的订单支付消息";

        // 拼接格式：topicName:tagName
        String destination = topic + ":" + tag;

        // 发送消息
        SendResult sendResult = rocketMQTemplate.syncSend(destination, content);

        return "发送成功！msgId = " + sendResult.getMsgId();
    }
}