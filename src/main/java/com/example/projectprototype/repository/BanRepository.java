package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Ban;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {
    Optional<Ban> findBySrcAndDest(User src, User dest);
}
