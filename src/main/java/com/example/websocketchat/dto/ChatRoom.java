package com.example.websocketchat.dto;


import lombok.*;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChatRoom {
    private String roomId;
    private String name;

    @Builder
    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}
