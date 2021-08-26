package com.example.projectprototype.repository;

import com.example.projectprototype.ChatRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Getter @Setter
public class ChatRoomRepository {

    private static Map<Long, ChatRoom> chatRoomMap = new HashMap<>();

    public ChatRoom findById(Long id) {
        if (chatRoomMap.get(id) != null) {
            return chatRoomMap.get(id);
        }
        return null;
    }

    public void save(ChatRoom chatRoom) {
        chatRoomMap.put(chatRoom.getId(), chatRoom);
    }
}
