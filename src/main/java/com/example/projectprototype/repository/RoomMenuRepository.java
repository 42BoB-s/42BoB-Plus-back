package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoomMenuRepository extends CrudRepository <RoomMenu, Long> {

    Optional<RoomMenu> findByRoom(Room room);
    Optional<RoomMenu> findByMenu(Menu menu);
}
