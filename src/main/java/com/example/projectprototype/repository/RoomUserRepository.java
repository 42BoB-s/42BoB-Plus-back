package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomUser;
import com.example.projectprototype.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomUserRepository extends CrudRepository<RoomUser, Long> {

    List<RoomUser> findByRoom(Room room);
    List<RoomUser> findByRoom(User user);
}
