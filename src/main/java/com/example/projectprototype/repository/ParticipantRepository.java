package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {

    List<Participant> findByRoom(Room room);
    List<Participant> findByUser(User user);
}
