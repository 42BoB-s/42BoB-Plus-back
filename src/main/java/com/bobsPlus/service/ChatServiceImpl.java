package com.bobsPlus.service;

import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.enums.RoomStatus;
import com.bobsPlus.mapper.ChatMessageMapper;
import com.bobsPlus.mapper.ChatMessageResponseMapper;
import com.bobsPlus.repository.ChatMessageRepository;
import com.bobsPlus.repository.ParticipantRepository;
import com.bobsPlus.repository.RoomRepository;
import com.bobsPlus.repository.UserRepository;
import com.bobsPlus.dto.ChatMessageDto;
import com.bobsPlus.dto.ChatMessageResponseDto;
import com.bobsPlus.entity.ChatMessage;
import com.bobsPlus.entity.User;
import com.bobsPlus.entity.enums.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService{
	private final UserRepository userRepository;
	private final ParticipantRepository participantRepository;
	private final RoomRepository roomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatMessageMapper chatMessageMapper;
	private final ChatMessageResponseMapper chatMessageResponseMapper;
	private LinkedHashMap<Long, Map<String, WebSocketSession>> map;

	public int vaildCheck(ChatMessageDto chatMessageDto)
	{
		int result;

		Optional<User> user = userRepository.findById(chatMessageDto.getWriter());
		Optional<Room> room = roomRepository.findById(chatMessageDto.getRoom_id());
		if (user.isEmpty())
			result = -7;
		else if (room.isEmpty())
			result = -8;
		else if (participantRepository.findParticipantByRoomEqualsAndUserEquals(room.get(), user.get()) == null)
			result = -9;
		else if (!room.get().getStatus().equals(RoomStatus.active))
			result = -10;
		else
		{
			chatMessageDto.setRoom(room.get());
			chatMessageDto.setTime(LocalDateTime.now());
			result = 1;
		}
		return result;
	}

	public void createChatRoomSessionList(WebSocketSession session, ChatMessageDto chatMessageDto)
	{
		if (map.get(chatMessageDto.getRoom_id()) == null) {
			Map<String, WebSocketSession> sessions = new HashMap<>();
			map.put(chatMessageDto.getRoom_id(), sessions);
		}
	}

	public void handleMessage(WebSocketSession session, ChatMessageDto chatMessageDto, ObjectMapper objectMapper) throws IOException {
		Map<String, WebSocketSession> sessions = map.get(chatMessageDto.getRoom_id());
		JSONArray jsonArray = new JSONArray();
		if (chatMessageDto.getMessageType() == MessageType.enter)
		{
			//???????????? ??????
			jsonArray = loadHistory(chatMessageDto, objectMapper, jsonArray);
			if (sessions.get(chatMessageDto.getWriter()) == null) { // ?????? ?????? ??????(?????? ???????????? ???????????? ??????)
				chatMessageDto.setMessage(chatMessageDto.getWriter() + "?????? ?????????????????????.");
			}
			else
				chatMessageDto.setMessage(null); // ??????????????? ????????? ?????? ?????? ???????????? ?????? ????????? ??????, ?????? ?????? ????????????.
			sessions.put(chatMessageDto.getWriter(), session);
		}
		else if (chatMessageDto.getMessageType() == MessageType.leave)
		{
			sessions.remove(chatMessageDto.getWriter());
			chatMessageDto.setMessage(chatMessageDto.getWriter() + "?????? ?????????????????????.");
		}
		send(session, chatMessageDto, objectMapper, jsonArray);
	}

	public JSONArray loadHistory(ChatMessageDto chatMessageDto, ObjectMapper objectMapper, JSONArray jsonArray) throws IOException
	{
		List<ChatMessage> chatMessageList = chatMessageRepository.findByRoom(chatMessageDto.getRoom());
		for (ChatMessage ch : chatMessageList)
		{
			ChatMessageResponseDto dto = chatMessageResponseMapper.toDto(ch);
			dto.setTime(ch.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			String json = objToJson(chatMessageMapper.toDto(ch), objectMapper);
			JSONObject jsonObj = new JSONObject(json);
			jsonArray.put(jsonObj);
		}
		return jsonArray;
	}

	public String objToJson(ChatMessageDto chatMessageDto, ObjectMapper objectMapper) throws IOException
	{
		ChatMessageResponseDto dto = ChatMessageResponseDto.builder()
				.writer(chatMessageDto.getWriter())
				.messageType(chatMessageDto.getMessageType())
				.message(chatMessageDto.getMessage())
				.time(chatMessageDto.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.build();
		return objectMapper.writeValueAsString(dto);
	}

	public void send(WebSocketSession session, ChatMessageDto chatMessageDto, ObjectMapper objectMapper, JSONArray jsonArray) throws IOException {

		//???????????? : ?????? ????????? ??????????????? ??????
		if (chatMessageDto.getMessageType() == MessageType.enter) // ???????????????
		{
			TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(jsonArray.toString()));
			session.sendMessage(textMessage);
			jsonArray.clear();
			if (chatMessageDto.getMessage() == null) // ??????????????? ????????? ?????? ?????? ???????????? ?????? ????????? ??????, ?????? ?????? ????????????.
				return ;
		}
		// ??? ????????? ??????
		jsonArray.put(new JSONObject(objToJson(chatMessageDto, objectMapper)));
		for (Map.Entry<String, WebSocketSession> userSession : (map.get(chatMessageDto.getRoom_id()).entrySet())) {
			if (!userSession.getValue().isOpen())
				continue; // ?????? ???????????? ?????? map?????? ????????? ?????? ?????????. (?????? ????????? ????????? ??????, ????????? ??????)
			else {
				TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(jsonArray.toString()));
				userSession.getValue().sendMessage(textMessage);
			}
		}
		//??????
		if (chatMessageDto.getMessageType() == MessageType.text) // ????????? ???????????? ??????
		{
			ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDto);
			chatMessageRepository.save(chatMessage);
		}
		//?????????
		/*
		for (Map.Entry<String, WebSocketSession> userSession : (map.get(chatMessageDto.getRoom_id()).entrySet())) {
			if (!userSession.getValue().isOpen())
				System.out.println("[dead] room_id : " + userSession.getKey() + ", session : " + userSession.getValue());
			else {
				System.out.println("[alive] room_id : " + userSession.getKey() + ", session : " + userSession.getValue());
			}
		}
		*/
	}

	public void succeedSend(Long roomId, ObjectMapper objectMapper) throws IOException {
		JSONArray jsonArray = new JSONArray();

		ChatMessageDto chatMessageDto = new ChatMessageDto();
		chatMessageDto.setWriter("Admin");
		chatMessageDto.setMessageType(MessageType.text);
		chatMessageDto.setMessage("?????? ????????? ???????????????. ?????????????????? ?????????!");
		chatMessageDto.setTime(LocalDateTime.now());

		jsonArray.put(new JSONObject(objToJson(chatMessageDto, objectMapper)));
		for (Map.Entry<String, WebSocketSession> userSession : (map.get(roomId).entrySet())) {
				TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(jsonArray.toString()));
				userSession.getValue().sendMessage(textMessage);
			}
		}

	public void removeChat(Long roomId) {
		try {
			// ????????? ???????????? ???????????? ?????? ?????? ???????????? ?????????????????? ??? ?????? ?????? ??????
			if (map.get(roomId).size() > 0)
				map.get(roomId).clear();
			map.remove(roomId);
		} catch (Exception e) {
			e.printStackTrace();
			// error ??????
		}
	}
}