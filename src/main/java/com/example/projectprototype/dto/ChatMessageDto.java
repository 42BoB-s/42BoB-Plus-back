package com.example.projectprototype.dto;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
	private Long id;
	private Long room_id; // request
	private Room room;
	private String writer; // request
	private String message;
	private MessageType messageType;
	private LocalDateTime time;
}
