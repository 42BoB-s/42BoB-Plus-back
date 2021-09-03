package com.example.projectprototype.service;

import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.repository.ParticipantRepository;
import com.example.projectprototype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public long setParticipate(Room room, String userId) {
        if (!userService.userIdCheck(userId)) return -3L;

        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(userRepository.findById(userId).get());
        participantRepository.save(participant);
        return room.getId();
    }

    public boolean isParticipant(Room room, String userId) {
        for ( Participant part : room.getParticipantList()) {
            if(part.getUser().getId().equals(userId))
                return true;
        }
        return false;
    }

}
