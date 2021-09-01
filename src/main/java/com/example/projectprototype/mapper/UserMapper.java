package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.UserDto;
import com.example.projectprototype.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserDto, User> {

    @Override
    @Mapping(target = "banSrcList", ignore = true)
    @Mapping(target = "banDestList", ignore = true)
    @Mapping(target = "ownedRoomList", ignore = true)
    UserDto toDto(User user);

    @Override
    @Mapping(target = "banSrcList", ignore = true)
    @Mapping(target = "banDestList", ignore = true)
    @Mapping(target = "ownerList", ignore = true)
    @Mapping(target = "participantList", ignore = true)
    User toEntity(UserDto userDto);
}
