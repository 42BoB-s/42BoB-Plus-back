package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.ChatMessageResponseDto;
import com.example.projectprototype.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageResponseMapper  extends GenericMapper<ChatMessageResponseDto, ChatMessage> {
}
