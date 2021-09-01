package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.UserDto;
import com.example.projectprototype.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserDto, User>{

}
