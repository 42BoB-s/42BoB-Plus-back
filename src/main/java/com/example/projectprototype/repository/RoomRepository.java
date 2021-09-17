package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();
    List<Room> findByOwner(User user);
    List<Room> findByStatus(RoomStatus status);
    List<Room> findByStatusAndMeetTimeBetween(RoomStatus roomStatus, LocalDateTime fromDate, LocalDateTime toDate);

    @Query(value = "update room r set r.status = ?2 where r.id = ?1 ", nativeQuery = true)
    void updateStatus(Long id, String status);

    @Query(value = "update room r set r.title = ?2 where r.id = ?1", nativeQuery = true)
    void updateTitle(Long id, String title);

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

    @Query(value = "SELECT DISTINCT r.id as 'distinct_id', r.* FROM " +
            "(SELECT * FROM room WHERE status = 'actice' AND location LIKE ?1 " +
            "AND meet_time BETWEEN ?2 AND ?3 AND title LIKE ?4) r " +
            "JOIN room_menu rm ON r.id = rm.room_id " +
            "JOIN menu m ON m.id = rm.menu_id " +
            "WHERE m.name IN ?6 ",
    countQuery =
            "SELECT COUNT(DISTINCT r.id) FROM " +
            "(SELECT * FROM room WHERE status = 'actice' AND location LIKE ?1 " +
            "AND meet_time BETWEEN ?2 AND ?3 AND title LIKE ?4) r " +
            "JOIN room_menu rm ON r.id = rm.room_id " +
            "JOIN menu m ON m.id = rm.menu_id " +
            "WHERE m.name IN ?6 ",
    nativeQuery = true)
    Page<Room> searchRooms(String location, String startTime, String endTime,
                           String keyword, List<String> menuNameList, Pageable pageable);
}
