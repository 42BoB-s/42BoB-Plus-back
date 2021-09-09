package com.example.projectprototype.repository;

import com.example.projectprototype.dto.StatDto;
import com.example.projectprototype.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select (select count(*) from participant where user_id = ?1) as succeedStat," +
            "       (select location FROM  (select * from participant where user_id = ?1 ) p" +
            "           left join room r on r.id = p.room_id" +
            "       group by location order by count(*) desc LIMIT 1) as locationStat," +
            "       (select m.name from (select * from participant where user_id = ?1 ) p" +
            "           left join room_menu rm on rm.room_id=p.room_id" +
            "           join menu m on rm.menu_id = m.id group by m.name order by count(*) desc LIMIT 1) as menusStat," +
            "       (select user_id" +
            "from (select room_id from participant where user_id =?1) p1" +
            "    join (select * from participant where user_id!=?1) p2 on p1.room_id = p2.room_id" +
            "group by user_id order by count(*) desc limit 1) as name"
            ,nativeQuery = true)
    StatDto searchStat(String userId);
}
