package com.example.controller;

import com.example.common.SparkAuthUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/spark")
public class SparkChatController {

    private final String appId ="b129b47c";
    private final String apiKey = "0a01913915ab10c51cb7ad39d2c434e8";
    private final String apiSecret = "MDc2MjQ1NWU4NmQzNTlkZmJkZDM5ZTQz";
    private final String chatUrl = "wss://spark-api.xf-yun.com/v3.5/chat";


    @PostMapping("/chat-stream")
    public SseEmitter chatStream(@RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(180_000L); // 3分钟超时

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                String authUrl = SparkAuthUtil.getAuthUrl(apiKey, apiSecret, chatUrl);
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request wsRequest = new Request.Builder().url(authUrl).build();

                WebSocket webSocket = client.newWebSocket(wsRequest, new WebSocketListener() {
                    @Override
                    public void onMessage(WebSocket webSocket, String text) {
                        JsonNode response = null;
                        try {
                            response = new ObjectMapper().readTree(text);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        if (response.has("header") && response.get("header").get("code").asInt() == 0) {
                            String content = response.at("/payload/choices/text/0/content").asText();
                            try {
                                emitter.send(SseEmitter.event().data(content));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onClosed(WebSocket webSocket, int code, String reason) {
                        emitter.complete();
                    }
                });

                // 构建星火协议请求
                String requestBody = String.format("{" +
                        "\"header\":{\"app_id\":\"%s\"}," +
                        "\"parameter\":{\"chat\":{\"domain\":\"generalv3.5\"}}," +
                        "\"payload\":{\"message\":{\"text\":[{\"role\":\"user\",\"content\":\"%s\"}]}}" +
                        "}", appId, request.getContent());

                webSocket.send(requestBody);

            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    @Data
    static class ChatRequest {
        private String content;
    }
}