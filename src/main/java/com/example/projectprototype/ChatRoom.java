package com.example.projectprototype;

import com.example.projectprototype.entity.Room;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class ChatRoom {

    private Long id;
    private Room room;
    private Set<WebSocketSession> sessions = new HashSet<>();
}
