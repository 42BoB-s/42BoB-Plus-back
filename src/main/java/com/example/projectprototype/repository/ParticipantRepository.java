package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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
