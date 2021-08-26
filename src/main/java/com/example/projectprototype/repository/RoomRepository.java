package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.service.RoomService;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {

    List<Room> findAll();
    Room findByIdAndStatus(Room room, RoomStatus status);
}
