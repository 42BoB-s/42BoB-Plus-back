package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomMenuRequestDto;
import com.example.projectprototype.entity.RoomMenu;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-29T11:58:10+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class RoomMenuRequestMapperImpl implements RoomMenuRequestMapper {

    @Override
    public RoomMenuRequestDto toDto(RoomMenu e) {
        if ( e == null ) {
            return null;
        }

        RoomMenuRequestDto roomMenuRequestDto = new RoomMenuRequestDto();

        roomMenuRequestDto.setRoom( e.getRoom() );
        roomMenuRequestDto.setMenu( e.getMenu() );

        return roomMenuRequestDto;
    }

    @Override
    public RoomMenu toEntity(RoomMenuRequestDto d) {
        if ( d == null ) {
            return null;
        }

        RoomMenu roomMenu = new RoomMenu();

        roomMenu.setRoom( d.getRoom() );
        roomMenu.setMenu( d.getMenu() );

        return roomMenu;
    }

    @Override
    public void updateFromDto(RoomMenuRequestDto dto, RoomMenu entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getRoom() != null ) {
            entity.setRoom( dto.getRoom() );
        }
        if ( dto.getMenu() != null ) {
            entity.setMenu( dto.getMenu() );
        }
    }
}
