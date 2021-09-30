package com.bobPlus.repository;

import com.bobPlus.entity.Ban;
import com.bobPlus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {
    Optional<Ban> findBySrcAndDest(User src, User dest);
}
