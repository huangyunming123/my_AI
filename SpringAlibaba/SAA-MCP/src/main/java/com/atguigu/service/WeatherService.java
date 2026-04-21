package com.atguigu.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class WeatherService {

    @Tool(description = "查询指定城市的天气信息。当用户询问天气时调用，参数city必须是具体的城市名称，如：北京、上海、广州、深圳。示例：'北京天气'调用时city='北京'")
    public String getWeather(String city) {
        log.info("收到天气查询请求，城市参数: [{}]", city);
        
        Map<String,String> map = Map.of(
                "北京","晴转多云111",
                "上海","多云",
                "广州","多云",
                "深圳","多云"
        );
        
        String result = map.getOrDefault(city, "未知城市: " + city);
        log.info("返回天气结果: [{}]", result);
        return result;
    }
}
