package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomMenuRequestDto;
import com.example.projectprototype.dto.RoomMenuRequestDto.RoomMenuRequestDtoBuilder;
import com.example.projectprototype.entity.RoomMenu;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-30T20:48:41+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class RoomMenuRequestMapperImpl implements RoomMenuRequestMapper {

    @Override
    public RoomMenuRequestDto toDto(RoomMenu arg0) {
        if ( arg0 == null ) {
            return null;
        }

        RoomMenuRequestDtoBuilder roomMenuRequestDto = RoomMenuRequestDto.builder();

        roomMenuRequestDto.room( arg0.getRoom() );
        roomMenuRequestDto.menu( arg0.getMenu() );

        return roomMenuRequestDto.build();
    }

    @Override
    public RoomMenu toEntity(RoomMenuRequestDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        RoomMenu roomMenu = new RoomMenu();

        roomMenu.setRoom( arg0.getRoom() );
        roomMenu.setMenu( arg0.getMenu() );

        return roomMenu;
    }

    @Override
    public void updateFromDto(RoomMenuRequestDto arg0, RoomMenu arg1) {
        if ( arg0 == null ) {
            return;
        }

        if ( arg0.getRoom() != null ) {
            arg1.setRoom( arg0.getRoom() );
        }
        if ( arg0.getMenu() != null ) {
            arg1.setMenu( arg0.getMenu() );
        }
    }
}
