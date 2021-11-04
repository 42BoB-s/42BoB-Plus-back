package com.bobsPlus.repository;

import com.bobsPlus.entity.ChatMessage;
import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoom(Room room);
    List<ChatMessage> findByMessageType(MessageType messageType);
}
