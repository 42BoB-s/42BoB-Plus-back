package com.bobPlus.mapper;

import com.bobPlus.dto.ChatMessageDto;
import com.bobPlus.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends GenericMapper<ChatMessageDto, ChatMessage> {

}
