package com.bobsPlus.dto;

import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.enums.MessageType;
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
