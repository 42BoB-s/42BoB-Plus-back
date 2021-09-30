package com.bobPlus.mapper;

import com.bobPlus.dto.ChatMessageResponseDto;
import com.bobPlus.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageResponseMapper  extends GenericMapper<ChatMessageResponseDto, ChatMessage> {
}
