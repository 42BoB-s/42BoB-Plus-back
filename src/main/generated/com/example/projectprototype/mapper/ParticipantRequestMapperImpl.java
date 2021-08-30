package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.ParticipantRequestDto;
import com.example.projectprototype.entity.Participant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-30T10:21:33+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class ParticipantRequestMapperImpl implements ParticipantRequestMapper {

    @Override
    public ParticipantRequestDto toDto(Participant e) {
        if ( e == null ) {
            return null;
        }

        ParticipantRequestDto participantRequestDto = new ParticipantRequestDto();

        participantRequestDto.setRoom( e.getRoom() );
        participantRequestDto.setUser( e.getUser() );

        return participantRequestDto;
    }

    @Override
    public Participant toEntity(ParticipantRequestDto d) {
        if ( d == null ) {
            return null;
        }

        Participant participant = new Participant();

        participant.setUser( d.getUser() );
        participant.setRoom( d.getRoom() );

        return participant;
    }

    @Override
    public void updateFromDto(ParticipantRequestDto dto, Participant entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getUser() != null ) {
            entity.setUser( dto.getUser() );
        }
        if ( dto.getRoom() != null ) {
            entity.setRoom( dto.getRoom() );
        }
    }
}
