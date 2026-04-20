package com.atguigu.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherService {

    @Tool(description = "获取天气，只有当用户查询天气的时候才会调用此方法")
    public String getWeather(String city) {
        Map<String,String> map = Map.of(
                "北京","晴转多云111",
                "上海","多云",
                "广州","多云",
                "深圳","多云"
        );
        return map.getOrDefault(city, "未知城市");
    }
}
