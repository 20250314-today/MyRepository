package com.example.controller;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/api")
@RestController
public class ChatController {
    //RestTemplate：Spring 提供的 HTTP 客户端，用于发送 HTTP 请求。通过构造方法注入，确保实例可用。
    //apiKey：用于存储 DeepSeek API 密钥（当前硬编码在代码中，存在安全风险，需从配置文件读取）
    private final RestTemplate restTemplate;
//    / 从配置文件读取API密钥

    private String apiKey;
    @Autowired
    public ChatController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    // Spring Boot 示例
    @PostMapping("/chat")
    public ResponseEntity<String> proxyChat(@RequestBody String body) {
        String apiUrl = "https://api.deepseek.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("sk-0981a2511137492d8dd5ca819db272de");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class  //指定响应体解析为字符串类型
        );
    }

}
