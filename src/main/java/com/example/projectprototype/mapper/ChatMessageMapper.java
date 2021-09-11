package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.ChatMessageDto;
import com.example.projectprototype.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends GenericMapper<ChatMessageDto, ChatMessage> {

}
