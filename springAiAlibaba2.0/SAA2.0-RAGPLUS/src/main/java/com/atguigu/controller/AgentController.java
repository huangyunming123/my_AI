package com.atguigu.controller;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Value("${spring.ai.dashscope.agent.options.app-id}")
    private String appId;

    private DashScopeAgent dashScopeAgent;


    public AgentController (DashScopeAgentApi dashScopeAgentApi) {
        this.dashScopeAgent = new DashScopeAgent(dashScopeAgentApi);
    }


    @RequestMapping("/test")
    public String test(@RequestParam(name = "message", defaultValue = "今天吃啥") String message) {
        DashScopeAgentOptions build = DashScopeAgentOptions.builder().appId(appId).build();
        Prompt prompt = new Prompt(message, build);
        return dashScopeAgent.call(prompt).getResult().getOutput().getText();
    }

    public DashScopeAgent getDashScopeAgent() {
        return dashScopeAgent;
    }
}
