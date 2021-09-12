package com.example.projectprototype.service;

import com.example.projectprototype.entity.Ban;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.BanRepository;
import com.example.projectprototype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BanService {
    private final BanRepository banRepository;

    public void addBan(User src, User dest)
    {
        Ban ban = new Ban();
        ban.setSrc(src);
        ban.setDest(dest);
        banRepository.save(ban);
    }

    public void deleteBan(User src, User dest)
    {
        Optional<Ban> findBan = banRepository.findBySrcAndDest(src, dest);
        src.getBanSrcList().remove(findBan.get());
        dest.getBanDestList().remove(findBan.get());
        banRepository.delete(findBan.get());
    }
}
