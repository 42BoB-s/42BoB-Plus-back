package com.bobPlus.repository;

import com.bobPlus.entity.Menu;
import com.bobPlus.entity.Room;
import com.bobPlus.entity.RoomMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMenuRepository extends JpaRepository<RoomMenu, Long> {

    Optional<List<RoomMenu>> findByRoom(Room room);
    Optional<List<RoomMenu>> findByMenu(Menu menu);

    void deleteAllByRoomId(Long roomId);
}
