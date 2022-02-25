package com.bobsPlus.mapper;

import com.bobsPlus.dto.TokenDto;
import com.bobsPlus.entity.Token;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper  extends GenericMapper<TokenDto, Token> {
}