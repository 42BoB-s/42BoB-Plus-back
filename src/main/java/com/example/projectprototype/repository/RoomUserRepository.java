package com.example.projectprototype.repository;

import com.example.projectprototype.domain.Room;
import com.example.projectprototype.domain.RoomUser;
import com.example.projectprototype.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomUserRepository extends CrudRepository<RoomUser, Long> {

    List<RoomUser> findByRoom(Room room);
    List<RoomUser> findByRoom(User user);
}
