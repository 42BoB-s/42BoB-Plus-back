package com.example.projectprototype.repository;

import com.example.projectprototype.entity.ChatMessage;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoom(Room room);
    List<ChatMessage> findByMessageType(MessageType messageType);
}
