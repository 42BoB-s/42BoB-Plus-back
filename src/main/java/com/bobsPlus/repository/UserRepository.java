package com.bobsPlus.repository;

import com.bobsPlus.dto.MealHistoryDto;
import com.bobsPlus.dto.StatDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

   Optional<User> findById(String id);

   @Query(value = "UPDATE user u SET u.profile = ?1 WHERE u.id = ?2 ",
            nativeQuery = true)
    void updateProfilePath(String path, String userId);
  
    @Query(name = "searchStatQuery", nativeQuery = true)
    List<StatDto> searchStat(@Param("id") String id);

    @Query(name = "searchHistoryQuery", nativeQuery = true)
    List<MealHistoryDto> searchHistory(@Param("id") String id);
}
