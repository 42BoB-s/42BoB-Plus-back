package com.example.projectprototype.repository;

import com.example.projectprototype.domain.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {

    List<Room> findAll();
}
