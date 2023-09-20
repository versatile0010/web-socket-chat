package com.example.websocketchat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type; // 메세지 타입
    private String roomId; // 채팅방 번호
    private String sender;
    private String message;
    private Long userCount;

    public static ChatMessage of(MessageType type, String roomId, String sender, String message, Long userCount) {
        return ChatMessage.builder()
                .type(type)
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .userCount(userCount).build();
    }
}
