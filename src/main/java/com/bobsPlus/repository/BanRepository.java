package com.bobsPlus.repository;

import com.bobsPlus.entity.Ban;
import com.bobsPlus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {
    Optional<Ban> findBySrcAndDest(User src, User dest);
}
