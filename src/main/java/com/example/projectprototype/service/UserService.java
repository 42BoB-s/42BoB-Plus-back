package com.example.projectprototype.service;

import com.example.projectprototype.entity.Ban;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BanService banService;

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

    public boolean checkBanList(User src, User dest)
    {
        for (Ban ban : src.getBanSrcList()){
            if (ban.getDest() == dest)
                return true;
        }
        return false;
    }

    public long addBan(String src, String dest)
    {
        Optional<User> findSrc = userRepository.findById(src);
        Optional<User> findDest = userRepository.findById(dest);

        if (findDest.isEmpty())
            return -3L;
        if (checkBanList(findSrc.get(),findDest.get()))
            return -5L;
        banService.addBan(findSrc.get(),findDest.get());
        return 1L;
    }

    public long deleteBan(String src, String dest)
    {
        Optional<User> findSrc = userRepository.findById(src);
        Optional<User> findDest = userRepository.findById(dest);

        if (findDest.isEmpty())
            return -3L;
        if (!checkBanList(findSrc.get(),findDest.get()))
            return -5L;
        banService.deleteBan(findSrc.get(),findDest.get());
        return 1L;
    }


}
