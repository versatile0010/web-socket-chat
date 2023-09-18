package com.example.websocketchat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; // 메세지 타입
    private String roomId; // 채팅방 번호
    private String sender;
    private String message;
}
