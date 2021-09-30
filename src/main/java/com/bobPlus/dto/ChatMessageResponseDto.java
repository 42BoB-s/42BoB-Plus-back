package com.bobPlus.dto;

import com.bobPlus.entity.enums.MessageType;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class ChatMessageResponseDto {
	private String writer;
	private String message;
	private MessageType messageType;
	private String time;

	@Builder
	public ChatMessageResponseDto(String writer, String message, MessageType messageType, String time)
	{
		this.writer = writer;
		this.message = message;
		this.messageType = messageType;
		this.time = time;
	}
}
