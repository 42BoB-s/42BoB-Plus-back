package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.dto.RoomDto.RoomDtoBuilder;
import com.example.projectprototype.dto.UserDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-01T18:44:58+0900",
    comments = "version: 1.4.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.1.1.jar, environment: Java 11.0.12 (Oracle Corporation)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public Room toEntity(RoomDto d) {
        if ( d == null ) {
            return null;
        }

        Room room = new Room();

        room.setOwner( userDtoToUser( d.getOwner() ) );
        room.setTitle( d.getTitle() );
        room.setMeetTime( d.getMeetTime() );
        room.setLocation( d.getLocation() );
        room.setCapacity( d.getCapacity() );
        room.setStatus( d.getStatus() );

        return room;
    }

    @Override
    public void updateFromDto(RoomDto dto, Room entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getOwner() != null ) {
            if ( entity.getOwner() == null ) {
                entity.setOwner( new User() );
            }
            userDtoToUser1( dto.getOwner(), entity.getOwner() );
        }
        if ( dto.getTitle() != null ) {
            entity.setTitle( dto.getTitle() );
        }
        if ( dto.getMeetTime() != null ) {
            entity.setMeetTime( dto.getMeetTime() );
        }
        if ( dto.getLocation() != null ) {
            entity.setLocation( dto.getLocation() );
        }
        entity.setCapacity( dto.getCapacity() );
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
    }

    @Override
    public RoomDto toDto(Room e) {
        if ( e == null ) {
            return null;
        }

        RoomDtoBuilder roomDto = RoomDto.builder();

        roomDto.roomId( e.getId() );
        roomDto.participants( mapParticipant( e.getParticipantList() ) );
        roomDto.menus( mapMenuToString( e.getRoomMenuList() ) );
        roomDto.title( e.getTitle() );
        roomDto.meetTime( e.getMeetTime() );
        roomDto.location( e.getLocation() );
        roomDto.capacity( e.getCapacity() );
        roomDto.owner( userToUserDto( e.getOwner() ) );
        roomDto.status( e.getStatus() );

        return roomDto.build();
    }

    protected User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setProfile( userDto.getProfile() );

        return user;
    }

    protected void userDtoToUser1(UserDto userDto, User mappingTarget) {
        if ( userDto == null ) {
            return;
        }

        if ( userDto.getId() != null ) {
            mappingTarget.setId( userDto.getId() );
        }
        if ( userDto.getProfile() != null ) {
            mappingTarget.setProfile( userDto.getProfile() );
        }
    }

    protected UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setProfile( user.getProfile() );

        return userDto;
    }
}
