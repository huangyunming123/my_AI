package com.atguigu.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisOutput;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

@RestController
@RequestMapping("/api/picture")
public class AliPictureController {

    String imageMode = "wanx2.0-t2i-turbo";

    String voiceMode = "cosyvoice-v3-flash";
    String voice = "longhua_v3";

    @Autowired
    private ImageModel imageModel;

    @Resource
    private SpeechSynthesisModel speechSynthesisModel;


    @GetMapping("/generate")
    public String generate(@RequestParam(name = "prompt", defaultValue = "Goku") String prompt) {
        // 提交图片生成任务并等待完成
        ImageResponse response = imageModel.call(
                new ImagePrompt(prompt, DashScopeImageOptions.builder()
                        .withModel(imageMode)
                        .build())
        );
        // 获取图片 URL
        String imageUrl = response.getResult().getOutput().getUrl();
        return imageUrl;
    }

    @GetMapping("/generateVoilce")
    public String generateVoilce(@RequestParam(name = "prompt", defaultValue = "咪哥咪哥阿咪") String prompt) throws IOException {
        String filePath = "/Users/pengpei/Desktop/页面效果/"+ UUID.randomUUID()+".mp3";
        DashScopeSpeechSynthesisOptions build = DashScopeSpeechSynthesisOptions.builder()
                .model(voiceMode)
                .voice(voice)
                .build();

        SpeechSynthesisResponse speechSynthesisResponse = speechSynthesisModel.call(new SpeechSynthesisPrompt(prompt, build));
        SpeechSynthesisOutput output = speechSynthesisResponse.getResult().getOutput();
        ByteBuffer audio = output.getAudio();
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(audio.array());
        return filePath;


    }
}
