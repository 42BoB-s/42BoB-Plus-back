package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.entity.Room;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-30T10:21:33+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class RoomCreateRequestMapperImpl implements RoomCreateRequestMapper {

    @Override
    public RoomCreateRequestDto toDto(Room e) {
        if ( e == null ) {
            return null;
        }

        RoomCreateRequestDto roomCreateRequestDto = new RoomCreateRequestDto();

        roomCreateRequestDto.setTitle( e.getTitle() );
        roomCreateRequestDto.setLocation( e.getLocation() );
        roomCreateRequestDto.setCapacity( e.getCapacity() );

        return roomCreateRequestDto;
    }

    @Override
    public Room toEntity(RoomCreateRequestDto d) {
        if ( d == null ) {
            return null;
        }

        Room room = new Room();

        room.setTitle( d.getTitle() );
        room.setLocation( d.getLocation() );
        room.setCapacity( d.getCapacity() );

        return room;
    }

    @Override
    public void updateFromDto(RoomCreateRequestDto dto, Room entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getTitle() != null ) {
            entity.setTitle( dto.getTitle() );
        }
        if ( dto.getLocation() != null ) {
            entity.setLocation( dto.getLocation() );
        }
        entity.setCapacity( dto.getCapacity() );
    }
}
