package com.example.projectprototype.repository;

import com.example.projectprototype.domain.ChatMessage;
import com.example.projectprototype.domain.Room;
import com.example.projectprototype.domain.enums.MessageType;
import org.springframework.data.repository.CrudRepository;

import java.awt.*;
import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoom(Room room);
    List<ChatMessage> findByMessageType(MessageType messageType);
}
