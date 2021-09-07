package com.example.projectprototype.config;

import com.example.projectprototype.dto.ChatMessageDto;
import com.example.projectprototype.dto.ResponseDto;
import com.example.projectprototype.repository.ChatMessageRepository;
import com.example.projectprototype.service.ChatServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatServiceImpl chatService;
	private final ObjectMapper objectMapper;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String msg = message.getPayload();
		ChatMessageDto chatMessageDto = objectMapper.readValue(msg, ChatMessageDto.class);
		int vaildCheck = chatService.vaildCheck(chatMessageDto);
		if (vaildCheck != 1)
		{
			String objToJson = objectMapper.writeValueAsString(
					ResponseDto.builder().interCode(vaildCheck).build()
					);
			TextMessage textMessage = new TextMessage(objToJson);
			session.sendMessage(textMessage);
			session.close();
		}
		else
		{
			chatService.createChatRoomSessionList(session, chatMessageDto);
			chatService.handleMessage(session, chatMessageDto, objectMapper);
		}
	}

}