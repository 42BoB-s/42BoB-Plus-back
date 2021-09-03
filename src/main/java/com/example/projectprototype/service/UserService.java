package com.example.projectprototype.service;

import com.example.projectprototype.dto.UserDto;
import com.example.projectprototype.entity.Ban;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.mapper.UserMapper;
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
    private final UserMapper userMapper;

    // userId 로 select 해서 결과값이 있으면 true, 없으면 false
    public boolean userIdCheck(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            return false;
        else
            return true;
    }

    public UserDto convertToUserDto(User user) {
        UserDto userDto = userMapper.toDto(user);

        // 내가 벤한 사람들 id
        for (Ban ban : user.getBanSrcList()) {
            userDto.getBanSrcList().add(ban.getDest().getId());
        }
        // 나를 벤한 사람들 id
        for (Ban ban : user.getBanDestList()) {
            userDto.getBanDestList().add(ban.getSrc().getId());
        }
        // 내가 방장으로 있는 방
        for (Room room : user.getOwnerList()) {
            userDto.getOwnedRoomList().add(room.getId());
        }
        return userDto;
    }
}
