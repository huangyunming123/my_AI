package com.atguigu.controller;

import com.atguigu.config.MqProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqController {

    @Autowired
    private MqProducerService mqProducerService;

    @GetMapping("/send")
    public String sendMessage() {
        String topic = "demo-topic";
        String message = "Hello, RocketMQ!";
        // 发送普通消息
        mqProducerService.sendSimpleMessage(topic, message);
        return "消息发送成功";
    }

    @GetMapping("/sendWithTag")
    public String sendWithTag() {
        // 1. 主题
        String topic = "demo-topic";
        // 2. 标签（你可以自定义：order_pay / order_refund / user_register 等）
        String tag = "order_pay";
        return mqProducerService.sendWithTag(topic, tag);
    }

}
