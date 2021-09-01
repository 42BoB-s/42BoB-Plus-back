package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Ban;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanRepository extends CrudRepository<Ban, Long> {
}
