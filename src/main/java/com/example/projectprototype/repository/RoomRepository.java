package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();

    @Query("update room r set r.status = :status where r.id = :id")
    void updateStatus(@Param(value = "id") Long id,
            @Param(value = "status") String status);

    @Query("update room r set r.title = :title where r.id = :id")
    void updateTitle(@Param(value = "id") Long id, @Param(value = "title") String title);

    @Query(value =
            "SELECT r.* FROM (SELECT * FROM room WHERE status = 'active') r " +
            "JOIN (SELECT * FROM participant WHERE user_id = ?1) p ON r.id = p.room_id"
            ,nativeQuery = true)
    List<Room> findEnteredRoom(String userId);

    @Query(value = "SELECT DISTINCT r.id as 'distinct_id', r.* " +
            " FROM (SELECT * FROM room WHERE status = 'active' AND location LIKE ?1 " +
                                "AND meet_time BETWEEN ?2 AND ?3 AND title LIKE ?4) r " +
            " JOIN room_menu rm ON r.id = rm.room_id " +
            " JOIN menu m ON rm.menu_id = m.id " +
            " JOIN (SELECT room_id, COUNT(*) as 'cnt' FROM participant GROUP BY room_id " +
                    " HAVING room_id NOT IN (SELECT room_id FROM  participant WHERE user_id = ?5)) p " +
            " ON r.id =p.room_id AND p.cnt < r.capacity " +
            " WHERE m.name IN ?6 " +
            " AND r.id NOT IN (SELECT room_id FROM participant WHERE user_id IN (SELECT src FROM ban WHERE dest = ?5))"
            ,countQuery = "SELECT COUNT(DISTINCT r.id) " +
            " FROM (SELECT * FROM room WHERE status = 'active' AND location LIKE ?1 " +
            "AND meet_time BETWEEN ?2 AND ?3 AND title LIKE ?4) r " +
            " JOIN room_menu rm ON r.id = rm.room_id " +
            " JOIN menu m ON rm.menu_id = m.id " +
            " JOIN (SELECT room_id, COUNT(*) as 'cnt' FROM participant GROUP BY room_id " +
            " HAVING room_id NOT IN (SELECT room_id FROM  participant WHERE user_id = ?5)) p " +
            " ON r.id =p.room_id AND p.cnt < r.capacity " +
            " WHERE m.name IN ?6 " +
            " AND r.id NOT IN (SELECT room_id FROM participant WHERE user_id IN (SELECT src FROM ban WHERE dest = ?5))"
            ,nativeQuery = true)
    Page<Room> searchRooms(String location, String startTime, String endTime,
                           String keyword, String userId, List<String> menuNameList, Pageable pageable );
}
