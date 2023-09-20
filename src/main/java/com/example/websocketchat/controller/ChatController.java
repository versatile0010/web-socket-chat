package com.example.websocketchat.controller;

import com.example.websocketchat.dto.ChatMessage;
import com.example.websocketchat.service.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChannelTopic channelTopic;

    @MessageMapping("/chat/message") // client 에서 /pub/chat/message 으로 발행 요청 시
    public void message(ChatMessage message, @Header("Authorization") String token) {
        String name = jwtTokenProvider.getUserNameFromToken(token);
        message.setSender(name);
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setSender("[Notification]");
            message.setMessage(name + "님이 입장하셨습니다.");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
