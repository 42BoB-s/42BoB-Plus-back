package com.bobPlus.repository;

import com.bobPlus.entity.Room;
import com.bobPlus.entity.Participant;
import com.bobPlus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByUserIdAndRoomId(String userId, Long roomId);
    List<Participant> findByRoomId(Long roomId);
    List<Participant> findByUserId(String userId);

    void deleteByUserId(String userId);
    void deleteByRoomId(Long roomId);
    long countByRoomId(Long roomId);
    Participant findParticipantByRoomEqualsAndUserEquals(Room room, User user);

}
