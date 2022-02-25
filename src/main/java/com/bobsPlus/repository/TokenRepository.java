package com.bobsPlus.repository;

import com.bobsPlus.entity.Token;
import com.bobsPlus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByAccessToken(String accessToken);


}
