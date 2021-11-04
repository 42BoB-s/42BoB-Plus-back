package com.bobsPlus.mapper;

import com.bobsPlus.dto.ChatMessageDto;
import com.bobsPlus.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends GenericMapper<ChatMessageDto, ChatMessage> {

}
