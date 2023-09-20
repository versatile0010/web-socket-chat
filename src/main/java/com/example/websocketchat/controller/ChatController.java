package com.example.websocketchat.controller;

import antlr.Token;
import com.example.websocketchat.dto.ChatMessage;
import com.example.websocketchat.repository.ChatRoomRepository;
import com.example.websocketchat.service.RedisPublisher;
import com.example.websocketchat.service.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final RedisPublisher publisher;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/chat/message") // client 에서 /pub/chat/message 으로 발행 요청 시
    public void message(ChatMessage message, @Header("Authorization") String token) {
        String name = jwtTokenProvider.getUserNameFromToken(token);
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        publisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }
}
