package com.example.projectprototype.repository;

import com.example.projectprototype.entity.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMenuRepository extends CrudRepository <RoomMenu, Long> {

    Optional<RoomMenu> findByRoom(Room room);
    Optional<RoomMenu> findByMenu(Menu menu);

}
