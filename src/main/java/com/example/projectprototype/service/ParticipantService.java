package com.example.projectprototype.service;

import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.repository.ParticipantRepository;
import com.example.projectprototype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository partRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    public long setParticipate(Room room, String userId) {
        if (!userService.userIdCheck(userId)) return -3L;

        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(userRepository.findById(userId).get());
        partRepository.save(participant);
        return room.getId();
    }

    public boolean isParticipant(Room room, String userId) {
        for (Participant part : room.getParticipantList()) {
            if(part.getUser().getId().equals(userId))
                return true;
        }
        return false;
    }

    public long cancelParticipation(Room room, String userId) {
        // 방장 넘기는 로직
        if (room.getOwner().getId().equals(userId)) {
          rotateOwner(room);
        }
        // room 의  participantList 에서 해당 participant 를 제외.
        Optional<Participant> targetPart = partRepository.findByUserIdAndRoomId(userId, room.getId());
        if (targetPart.isEmpty()) return -5L;
        room.getParticipantList().remove(targetPart.get());
        // 해당 participant 삭제
        partRepository.delete(targetPart.get());
        return partRepository.countByRoomId(room.getId());
    }

    // 해당 room 의 participant 를 모두 조회하면서 첫번째로 ownerId 와 다른 userId 를 가진 user 가 owner 가 됨.
    private void rotateOwner(Room room) {
        String ownerId = room.getOwner().getId();
        for (Participant part : partRepository.findByRoomId(room.getId())) {
            if (!part.getUser().getId().equals(ownerId)) {
                room.setOwner(part.getUser());
            }
        }
    }
}
