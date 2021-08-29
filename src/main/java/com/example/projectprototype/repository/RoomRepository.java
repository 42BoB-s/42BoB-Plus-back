package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    List<Room> findAll();

    Room findRoomByMeetTimeBetweenAndOwner(LocalDateTime startTime, LocalDateTime endTime, User user);
    List<Room> findRoomByMeetTimeBetweenAndStatusEqualsAndLocationEqualsOrderByIdAsc(LocalDateTime startTime, LocalDateTime endTime, RoomStatus status, Location location);
    List<Room> findRoomByMeetTimeBetweenAndStatusEqualsAndLocationEquals(LocalDateTime startTime, LocalDateTime endTime, RoomStatus status, Location location, Pageable pageable);
}
