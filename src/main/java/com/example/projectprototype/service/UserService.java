package com.example.projectprototype.service;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findUserById(String id)
    {
        Optional<User> findUser = userRepository.findById(id);
        if (findUser.isEmpty())
        {
            //리다이렉션 to login page
        }
        return (findUser.get());
    }

    public User createUser(String id, String profile, String role)
    {
        User user = new User();
        user.setId(id);
        user.setProfile(profile);
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

}
