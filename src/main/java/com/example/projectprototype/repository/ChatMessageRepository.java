package com.example.projectprototype.repository;

import com.example.projectprototype.entity.ChatMessage;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.enums.MessageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoom(Room room);
    List<ChatMessage> findByMessageType(MessageType messageType);
}
