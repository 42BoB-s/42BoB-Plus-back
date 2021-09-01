package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();

    Optional<Room> findByIdAndStatus(Long id, RoomStatus status);

    @Query(value =
            "SELECT DISTINCT r.id as 'distinct_id' , r.* " +
            "FROM " +
                "(SELECT * FROM room  WHERE location LIKE ?1 AND meet_time BETWEEN ?2 AND ?3 AND status = 'active' AND title LIKE ?4 ) r " +
            "JOIN room_menu rm ON rm.room_id = r.id " +
            "JOIN menu m ON m.id = rm.menu_id " +
            "WHERE m.name IN ?5 " +
            "ORDER BY r.id ",
            countQuery =
                    "SELECT COUNT(DISTINCT r.id) " +
                            "FROM (SELECT * FROM room  WHERE location = ?1 AND meet_time BETWEEN ?2 AND ?3 AND status = 'active' AND title LIKE ?4) r " +
                            "JOIN room_menu rm ON rm.room_id = r.id " +
                            "JOIN menu m ON m.id = rm.menu_id " +
                            "WHERE m.name IN ?5 " +
                            "ORDER BY r.id ",
            nativeQuery = true)
    Page<Room> SearchRoomsWithTime(String location, String startTime, String endTime, String keyword,
                               List<String> menuNameList, Pageable pageable);

    @Query(value =
            "SELECT DISTINCT r.id as 'distinct_id' , r.* " +
                    "FROM " +
                    "(SELECT * FROM room  WHERE location = ?1 AND status = 'active' AND title LIKE ?2) r " +
                    "JOIN room_menu rm ON rm.room_id = r.id " +
                    "JOIN menu m ON m.id = rm.menu_id " +
                    "WHERE m.name IN ?3 " +
                    "ORDER BY r.id ",
            countQuery =
                    "SELECT COUNT(DISTINCT r.id) " +
                            "FROM (SELECT * FROM room  WHERE location = ?1 AND status = 'active' AND title LIKE ?2) r " +
                            "JOIN room_menu rm ON rm.room_id = r.id " +
                            "JOIN menu m ON m.id = rm.menu_id " +
                            "WHERE m.name IN ?3 " +
                            "ORDER BY r.id ",
            nativeQuery = true)
    Page<Room> SearchRoomsWithoutTime(String location, String keyword,
                                   List<String> menuNameList, Pageable pageable);

}
