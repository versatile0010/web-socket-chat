package com.example.websocketchat.chat;

import com.example.websocketchat.dto.ChatMessage;
import com.example.websocketchat.dto.ChatRoom;
import com.example.websocketchat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        // 1. 웹 소켓 클라이언트로부터 채팅 메세지를 전달받아서 chatMessage 객체로 변환
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        // 2. 채팅방 조회
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        // 3. 해당 채팅방에 입장한 모든 클라이언트에게 메세지 발송
        room.handleActions(session, chatMessage, chatService);
    }
}
