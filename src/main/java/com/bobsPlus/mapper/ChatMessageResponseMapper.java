package com.bobsPlus.mapper;

import com.bobsPlus.dto.ChatMessageResponseDto;
import com.bobsPlus.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageResponseMapper  extends GenericMapper<ChatMessageResponseDto, ChatMessage> {
}
