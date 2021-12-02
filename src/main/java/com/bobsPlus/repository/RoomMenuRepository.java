package com.bobsPlus.repository;

import com.bobsPlus.entity.Menu;
import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.RoomMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMenuRepository extends JpaRepository<RoomMenu, Long> {

    Optional<List<RoomMenu>> findByRoom(Room room);
    Optional<List<RoomMenu>> findByMenu(Menu menu);

    void deleteAllByRoomId(Long roomId);
}
