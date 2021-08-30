package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    List<Room> findAll();

    Room findRoomByStatusEqualsAndMeetTimeBetweenAndOwner(RoomStatus status, LocalDateTime startTime, LocalDateTime endTime,  User user);
    List<Room> findRoomByMeetTimeBetweenAndStatusEqualsAndLocationEqualsOrderByIdAsc(LocalDateTime startTime, LocalDateTime endTime, RoomStatus status, Location location);
    @Query(value = "select r.*, p.cnt from room r " +
            "LEFT JOIN ( " +
                "SELECT room_id, COUNT(*) cnt FROM participant  " +
                "GROUP BY room_id  " +
                "HAVING room_id NOT IN (SELECT room_id FROM participant WHERE user_id = ?1) " +
                "AND cnt < 4 " +
                ") p " +
            "ON r.id = p.room_id " +
            "WHERE p.cnt IS NOT NULL AND location = ?2 AND status = 0  " +
                "AND meet_time Between ?3 AND ?4 " +
                "AND title LIKE ?5 " +
                "AND r.id IN (SELECT DISTINCT room_id FROM room_menu r " +
                    "LEFT JOIN menu m " +
                    "ON r.menu_id = m.id " +
                "WHERE NAME IN ?6 " +
                ") " +
            "order by r.id", nativeQuery = true)
    List<Room> findDefaultView(String user_id, String location, LocalDateTime startTime, LocalDateTime endTime, String keword, List<String> menuList, Pageable pageable);
}
