package com.example.projectprototype;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class TestSetting {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;

    @PostConstruct
    void poc() {
        User user1 = createTestUser("tjeong1", "/img/tjeong1", "1");
        User user2 = createTestUser("tjeong2", "/img/tjeong2", "1");
        User user3 = createTestUser("tjeong3", "/img/tjeong3", "1");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<String> menuList = new ArrayList<>();
        menuList.add("중식"); menuList.add("한식");menuList.add("커피");
        setTestRoom(user1,"서초", "title1-1", "notice1", "2021-09-05 13:00:00", menuList);
        setTestRoom(user1,"서초", "title1-2", "notice1", "2021-09-05 15:00:00", menuList);
        setTestRoom(user1,"서초", "title1-3", "notice1", "2021-09-05 17:00:00", menuList);
        setTestRoom(user2,"서초", "title2-1", "notice2", "2021-09-05 14:00:00", menuList);
        setTestRoom(user3,"서초", "title3-1", "notice3", "2021-09-05 15:00:00", menuList);
    }

    private User createTestUser(String userId, String profile, String role) {
        User user = new User();
        user.setId(userId);
        user.setProfile(profile);
        user.setRole(role);
        return user;
    }

    private void setTestRoom(User user, String location, String title, String notice,
                            String meetTime, List<String> menuList) {
        RoomDto roomDTO = new RoomDto();
        roomDTO.setOwner(userService.convertToUserDto(user));
        roomDTO.setCapacity(1);
        roomDTO.setStatus("active");
        roomDTO.setTitle(title);
        roomDTO.setLocation(location);
        roomDTO.setMeetTime(meetTime);
        roomDTO.setAnnouncement(notice);
        roomDTO.setMenus(menuList);
        roomService.createRoom(roomDTO, user.getId());
    }
}
