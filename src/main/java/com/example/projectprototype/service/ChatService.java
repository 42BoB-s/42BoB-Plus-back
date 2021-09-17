package com.example.projectprototype.service;

import com.example.projectprototype.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;

public interface ChatService {
	int vaildCheck(ChatMessageDto chatMessageDto);
	void createChatRoomSessionList(WebSocketSession session, ChatMessageDto chatMessageDto);
	void handleMessage(WebSocketSession session, ChatMessageDto chatMessageDto, ObjectMapper objectMapper) throws IOException;
	JSONArray loadHistory(ChatMessageDto chatMessageDto, ObjectMapper objectMapper, JSONArray jsonArray) throws IOException;
	String objToJson(ChatMessageDto chatMessageDto, ObjectMapper objectMapper) throws IOException;
	void send(WebSocketSession session, ChatMessageDto chatMessageDto, ObjectMapper objectMapper, JSONArray jsonArray) throws IOException;
	void removeChat(Long roomId);
}
