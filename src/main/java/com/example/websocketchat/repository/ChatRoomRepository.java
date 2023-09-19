package com.example.websocketchat.repository;

import com.example.websocketchat.dto.ChatRoom;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {
    // socket - chat 실습을 위한 토이 프로젝트이므로 별도의 DB 를 사용하지 않고, Map 으로 관리합니다.
    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        // 채팅방 생성 순서가 최근인 순으로 조회
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findRoomById(String id) {
        return chatRoomMap.get(id);
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void enterChatRoom(String roomId) {
        // todo
    }

    public ChannelTopic getTopic(String roomId) {
        // todo
        return null;
    }
}
