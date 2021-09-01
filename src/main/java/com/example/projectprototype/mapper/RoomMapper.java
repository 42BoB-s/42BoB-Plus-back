package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Build 하면 이 인터페이스의 구현체가 자동으로 생성된다.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends GenericMapper<RoomDto, Room> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "meetTime", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "participantList", ignore = true)
    @Mapping(target = "roomMenuList", ignore = true)
    @Mapping(target = "messageList", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    Room toEntity(RoomDto roomDto);

    @Override
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "participants", ignore = true)
    RoomDto toDto(Room room);

}

// 기존에 있던 room 정보를 roomDto 로 바꾸는 작업

// roomDto 가 들어오면 이걸 room 으로 바꾸는 작업
// Room 을 생성할 때
// Room 을 업데이트 할 때

