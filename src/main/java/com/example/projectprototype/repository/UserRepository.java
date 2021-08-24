package com.example.projectprototype.repository;

import com.example.projectprototype.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
