package com.example.projectprototype.roomServiceTest;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class roomSearchTest {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findSearchRoomsQueryTest() {

        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        List<String> meetTimeList = new ArrayList<>();
        meetTimeList.add("2021-08-28 09:00:00");
        meetTimeList.add("2021-08-28 10:00:00");
        meetTimeList.add("2021-08-28 11:00:00");
        meetTimeList.add("2021-08-28 12:00:00");
        List<String> menuInput = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String meetTime = meetTimeList.get(i);
            menuInput.add("중식");
            menuInput.add("한식");
            menuInput.add("커피");
            roomService.convertToRoom(BuildRoomDTO(user, meetTime, menuInput), user.getId());
        }
        /*
        Optional<List<Room>> rooms = roomRepository.findSearchRooms("서초",
                "2021-08-28 08:00:00",
                "2021-08-28 13:00:00",
                menuInput);


        Assertions.assertThat(rooms.get().size()).isEqualTo(4);

         */

       List<String> menuOthers = new ArrayList<>();
        menuOthers.add("일식");
        menuOthers.add("양식");
        /*
        Optional<List<Room>> rooms2 = roomRepository.findSearchRooms("서초",
                "2021-08-28 08:00:00",
                "2021-08-28 13:00:00", menuOthers);

        Assertions.assertThat(rooms2.get().size()).isEqualTo(0);
        */

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

    static RoomDto BuildRoomDTO(User user, String meetTime, List<String> menuExpected) {
        RoomDto roomDTO = new RoomDto();
        roomDTO.setOwner(user);
        roomDTO.setCapacity(4);
        roomDTO.setStatus("active");
        roomDTO.setTitle("hello_room");
        roomDTO.setLocation("서초");
        roomDTO.setMeetTime(meetTime);
        roomDTO.setMenus(menuExpected);

        return roomDTO;
    }
}
