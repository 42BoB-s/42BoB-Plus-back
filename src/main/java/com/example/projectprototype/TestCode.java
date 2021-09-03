package com.example.projectprototype;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class TestCode {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final RoomService roomService;
    private final UserService userService;


    @PostConstruct
    public void poc() {
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        User user2 = createTestUser("tjeong2", "/img/tjeong", "1");
        userRepository.save(user);
        userRepository.save(user2);

        List<String> menuExpected = new ArrayList<>();
        menuExpected.add("중식");
        menuExpected.add("한식");
        menuExpected.add("커피");

        List<String> meetTimeList = new ArrayList<>();
        meetTimeList.add("2021-09-10 01:00:00");
        meetTimeList.add("2021-09-10 02:00:00");
        meetTimeList.add("2021-09-10 03:00:00");
        meetTimeList.add("2021-09-10 04:00:00");
        meetTimeList.add("2021-09-10 05:00:00");
        meetTimeList.add("2021-09-10 06:00:00");
        meetTimeList.add("2021-09-10 07:00:00");
        meetTimeList.add("2021-09-10 08:00:00");
        meetTimeList.add("2021-09-10 09:00:00");
        meetTimeList.add("2021-09-10 10:00:00");

        for (int i = 0; i < 10 ; i++) {
            RoomDto roomDTO = new RoomDto();
            roomDTO.setOwner(userService.convertToUserDto(user));
            roomDTO.setCapacity(4);
            roomDTO.setStatus("active");
            roomDTO.setTitle("hello_room");
            roomDTO.setLocation("서초");
            roomDTO.setMeetTime(meetTimeList.get(i));
            roomDTO.setAnnouncement("announcement");
            roomDTO.setMenus(menuExpected);
            roomService.createRoom(roomDTO, user.getId());

            RoomDto roomDTO2 = new RoomDto();
            roomDTO2.setOwner(userService.convertToUserDto(user2));
            roomDTO2.setCapacity(4);
            roomDTO2.setStatus("active");
            roomDTO2.setTitle("hello_room");
            roomDTO2.setLocation("서초");
            roomDTO2.setMeetTime(meetTimeList.get(i));
            roomDTO2.setAnnouncement("announcement");
            roomDTO2.setMenus(menuExpected);
            roomService.createRoom(roomDTO2, user2.getId());
        }
    }

    static User createTestUser(String userId, String profile, String role) {
        User user = new User();
        user.setId(userId);
        user.setProfile(profile);
        user.setRole(role);
        return user;
    }

    static Room createTestRoom(String meetTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Room room = new Room();

        room.setTitle("hello");
        room.setLocation(Location.서초);
        room.setStatus(RoomStatus.active);
        room.setAnnouncement("sample");
        room.setMeetTime(LocalDateTime.parse(meetTime,formatter));
        room.setAnnouncement("hellohellohello~~");
        return room;
    }
}
