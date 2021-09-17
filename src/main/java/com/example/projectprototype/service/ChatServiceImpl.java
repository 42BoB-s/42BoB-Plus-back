package com.example.projectprototype.service;

import com.example.projectprototype.dto.ChatMessageDto;
import com.example.projectprototype.dto.ChatMessageResponseDto;
import com.example.projectprototype.entity.ChatMessage;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.MessageType;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.mapper.ChatMessageMapper;
import com.example.projectprototype.mapper.ChatMessageResponseMapper;
import com.example.projectprototype.repository.ChatMessageRepository;
import com.example.projectprototype.repository.ParticipantRepository;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
			//히스토리 저장
			jsonArray = loadHistory(chatMessageDto, objectMapper, jsonArray);
			if (sessions.get(chatMessageDto.getWriter()) == null) { // 처음 접속 경우(또는 나갔다가 들어왔을 경우)
				chatMessageDto.setMessage(chatMessageDto.getWriter() + "님이 입장하셨습니다.");
			}
			else
				chatMessageDto.setMessage(null); // 정상적으로 나가지 않고 세션 끊겼다가 다시 접속한 경우, 접속 공지 필요없음.
			sessions.put(chatMessageDto.getWriter(), session);
		}
		else if (chatMessageDto.getMessageType() == MessageType.leave)
		{
			sessions.remove(chatMessageDto.getWriter());
			chatMessageDto.setMessage(chatMessageDto.getWriter() + "님이 퇴장하셨습니다.");
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

		//히스토리 : 새로 들어온 사람한테만 표시
		if (chatMessageDto.getMessageType() == MessageType.enter) // 히스토리는
		{
			TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(jsonArray.toString()));
			session.sendMessage(textMessage);
			jsonArray.clear();
			if (chatMessageDto.getMessage() == null) // 정상적으로 나가지 않고 세션 끊겼다가 다시 접속한 경우, 접속 공지 필요없음.
				return ;
		}
		// 각 세션에 전달
		jsonArray.put(new JSONObject(objToJson(chatMessageDto, objectMapper)));
		for (Map.Entry<String, WebSocketSession> userSession : (map.get(chatMessageDto.getRoom_id()).entrySet())) {
			if (!userSession.getValue().isOpen())
				continue; // 다시 들어왔을 경우 map이기 때문에 덮어 씌운다. (구지 제거할 필요가 없음, 나가기 예외)
			else {
				TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(jsonArray.toString()));
				userSession.getValue().sendMessage(textMessage);
			}
		}
		//저장
		if (chatMessageDto.getMessageType() == MessageType.text) // 공지는 저장하지 않음
		{
			ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDto);
			chatMessageRepository.save(chatMessage);
		}
		//디버그
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
		chatMessageDto.setMessage("약속 시간이 되었습니다. 약속장소에서 만나요!");
		chatMessageDto.setTime(LocalDateTime.now());

		jsonArray.put(new JSONObject(objToJson(chatMessageDto, objectMapper)));
		for (Map.Entry<String, WebSocketSession> userSession : (map.get(roomId).entrySet())) {
				TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(jsonArray.toString()));
				userSession.getValue().sendMessage(textMessage);
			}
		}

	public void removeChat(Long roomId) {
		try {
			// 갈비지 컬랙터가 처리하기 전에 직접 내용물을 지워줌으로써 힙 영역 용량 확보
			if (map.get(roomId).size() > 0)
				map.get(roomId).clear();
			map.remove(roomId);
		} catch (Exception e) {
			e.printStackTrace();
			// error 로깅
		}
	}
}