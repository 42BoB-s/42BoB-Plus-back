package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.dto.UserDto;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper extends GenericMapper<RoomDto, Room>{


    @Override
    @Mapping(source = "id", target = "roomId")
    @Mapping(source = "participantList", target = "participants")
    @Mapping(source = "roomMenuList", target = "menus")
    RoomDto toDto(Room e);

    default public List<String> mapMenuToString(List<RoomMenu> value)
    {
        List<String> menus = new ArrayList<>();
        for (RoomMenu roomMenu : value)
            menus.add(roomMenu.getMenu().getName().toString());
        return menus;
    }

    default public List<UserDto> mapParticipant(List<Participant> participantList)
    {
        List<UserDto> participants = new ArrayList<>();
        for (Participant participant : participantList)
        {
            // userDto를 멋지게 만드는 방법을 더 고민해야함
            UserDto userDto = new UserDto();
            userDto.setId(participant.getUser().getId());
            userDto.setProfile(participant.getUser().getProfile());
            participants.add(userDto);
        }
        return participants;
    }
}
