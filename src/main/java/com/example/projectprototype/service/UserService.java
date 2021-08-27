package com.example.projectprototype.service;

import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // userId 로 select 해서 결과값이 있으면 true, 없으면 false
    public boolean userIdCheck(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            return false;
        else
            return true;
    }
}
