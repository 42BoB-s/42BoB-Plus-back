package com.bobPlus.repository;

import com.bobPlus.entity.ChatMessage;
import com.bobPlus.entity.Room;
import com.bobPlus.entity.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoom(Room room);
    List<ChatMessage> findByMessageType(MessageType messageType);
}
