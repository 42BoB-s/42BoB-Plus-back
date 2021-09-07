package com.example.projectprototype.service;

import com.example.projectprototype.entity.Ban;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // userId 로 select 해서 결과값이 있으면 true, 없으면 false
    public boolean userIdCheck(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }

    public List<String> searchBanList(String userId)
    {
        List<String> banDestNames = new ArrayList<>();

        Optional<User> findUser = userRepository.findById(userId);
        for (Ban banSrc : findUser.get().getBanSrcList()) {
            banDestNames.add(banSrc.getDest().getId());
        }
        return banDestNames;
    }

    public long addBan(String src, String dest)
    {

    }

}
