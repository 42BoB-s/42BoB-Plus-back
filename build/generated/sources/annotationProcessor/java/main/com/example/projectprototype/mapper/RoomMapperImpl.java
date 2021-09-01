package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.enums.Location;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-01T14:48:04+0900",
    comments = "version: 1.4.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.1.1.jar, environment: Java 11.0.12 (Oracle Corporation)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public Room toEntity(RoomDto roomDto) {
        if ( roomDto == null ) {
            return null;
        }

        Room room = new Room();

        room.setTitle( roomDto.getTitle() );
        if ( roomDto.getLocation() != null ) {
            room.setLocation( Enum.valueOf( Location.class, roomDto.getLocation() ) );
        }
        room.setCapacity( roomDto.getCapacity() );
        room.setAnnouncement( roomDto.getAnnouncement() );

        return room;
    }

    @Override
    public RoomDto toDto(Room room) {
        if ( room == null ) {
            return null;
        }

        RoomDto roomDto = new RoomDto();

        if ( room.getId() != null ) {
            roomDto.setId( room.getId() );
        }
        roomDto.setTitle( room.getTitle() );
        if ( room.getMeetTime() != null ) {
            roomDto.setMeetTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( room.getMeetTime() ) );
        }
        if ( room.getLocation() != null ) {
            roomDto.setLocation( room.getLocation().name() );
        }
        roomDto.setCapacity( room.getCapacity() );
        if ( room.getStatus() != null ) {
            roomDto.setStatus( room.getStatus().name() );
        }
        roomDto.setAnnouncement( room.getAnnouncement() );

        return roomDto;
    }
}
