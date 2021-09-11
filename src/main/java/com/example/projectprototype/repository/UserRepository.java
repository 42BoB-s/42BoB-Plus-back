package com.example.projectprototype.repository;

import com.example.projectprototype.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "UPDATE user u SET u.profile = ?1 WHERE u.id = ?2 ",
            nativeQuery = true)
    void updateProfilePath(String path, String userId);
}
