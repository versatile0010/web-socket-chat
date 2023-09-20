package com.example.websocketchat.interceptor;

import com.example.websocketchat.dto.ChatMessage;
import com.example.websocketchat.repository.ChatRoomRepository;
import com.example.websocketchat.service.ChatService;
import com.example.websocketchat.service.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) { // 연결 요청 시
            String token = accessor.getFirstNativeHeader("Authorization");
            jwtTokenProvider.validateToken(token);
            log.info("Authorization: {}", token);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String roomId = chatService.getRoomId(Optional.ofNullable(
                            (String) message.getHeaders().get("simpDestination"))
                    .orElse("InvalidRoomId"));
            String sessionId = (String) message.getHeaders().get("simpSessionId");

            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
            chatRoomRepository.plusUserCount(roomId);

            String name = Optional.ofNullable(
                            (Principal) message.getHeaders().get("simpUser"))
                    .map(Principal::getName)
                    .orElse("UnknownUser");

            ChatMessage chatMessage = ChatMessage.of(ChatMessage.MessageType.ENTER, roomId, name, "", 0L);
            chatService.sendChatMessage(chatMessage);
            log.info("SUBSCRIBE {}:{}", name, roomId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.findRoomById(sessionId).getRoomId();

            chatRoomRepository.minusUserCount(roomId);

            String name = Optional.ofNullable(
                            (Principal) message.getHeaders().get("simpUser"))
                    .map(Principal::getName)
                    .orElse("UnknownUser");
            ChatMessage chatMessage = ChatMessage.of(ChatMessage.MessageType.QUIT, roomId, name, "", 0L);

            chatService.sendChatMessage(chatMessage);
            chatRoomRepository.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}:{}", sessionId, roomId);
        }
        return message;
    }
}
