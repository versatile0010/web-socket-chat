package com.example.websocketchat.controller;

import com.example.websocketchat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message") // client 에서 /pub/chat/message 으로 발행 요청 시
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.JOIN.equals(message.getType())) { // join 일 경우
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        // client 는 /sub/chat/room/{room-id} 으로 subscribe
        // message 가 전달되면 화면에 출력 해주면 됨
        // 여기서 /sub/chat/room/{room-id} 가 topic 에 해당
    }
}
