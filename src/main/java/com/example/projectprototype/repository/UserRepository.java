package com.example.projectprototype.repository;

import com.example.projectprototype.dto.MealHistoryDto;
import com.example.projectprototype.dto.StatDto;
import com.example.projectprototype.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.*;
import java.util.List;


public interface UserRepository extends JpaRepository<User, String> {

    @Query(name = "searchStatQuery", nativeQuery = true)
    List<StatDto> searchStat(@Param("id") String id);

    @Query(name = "searchHistoryQuery", nativeQuery = true)
    List<MealHistoryDto> searchHistory(@Param("id") String id);
}
