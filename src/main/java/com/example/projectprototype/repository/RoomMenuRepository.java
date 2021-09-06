package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMenuRepository extends JpaRepository<RoomMenu, Long> {

    Optional<List<RoomMenu>> findByRoom(Room room);
    Optional<List<RoomMenu>> findByMenu(Menu menu);

    void deleteAllByRoomId(Long roomId);
}
