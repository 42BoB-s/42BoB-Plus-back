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
        userRepository.save(user);

        List<String> menuExpected = new ArrayList<>();
        menuExpected.add("중식");
        menuExpected.add("한식");
        menuExpected.add("커피");

        List<String> meetTimeList = new ArrayList<>();
        meetTimeList.add("2021-09-01 01:00:00");
        meetTimeList.add("2021-09-01 02:00:00");
        meetTimeList.add("2021-09-01 03:00:00");
        meetTimeList.add("2021-09-01 04:00:00");
        meetTimeList.add("2021-09-01 05:00:00");
        meetTimeList.add("2021-09-01 06:00:00");
        meetTimeList.add("2021-09-01 07:00:00");
        meetTimeList.add("2021-09-01 08:00:00");
        meetTimeList.add("2021-09-01 09:00:00");
        meetTimeList.add("2021-09-01 10:00:00");

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
