package com.example.projectprototype.repository;

import com.example.projectprototype.domain.Menu;
import com.example.projectprototype.domain.Room;
import com.example.projectprototype.domain.RoomMenu;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoomMenuRepository extends CrudRepository <RoomMenu, Long> {

    Optional<RoomMenu> findByRoom(Room room);
    Optional<RoomMenu> findByMenu(Menu menu);
}
