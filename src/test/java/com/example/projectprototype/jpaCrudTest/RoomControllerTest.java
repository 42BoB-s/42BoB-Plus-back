package com.example.projectprototype.jpaCrudTest;


import com.example.projectprototype.controller.RoomController;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.service.ParticipantService;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(RoomController.class)
@Transactional
public class RoomControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    RoomService roomService;
    @MockBean
    UserService userService;
    @MockBean
    ParticipantService participantService;

    @Test
    @DisplayName("나의 방 조회 테스트")
    void getMyRoomList() throws Exception{
        User user1 = userService.createUser("user1","peofileEx(user1)","normal");
        User user2 = userService.createUser("user2","peofileEx(user2)","normal");
        User user3 = userService.createUser("user3","peofileEx(user3)","normal");
        List<MenuName> menus= new ArrayList<>();
        menus.add(MenuName.분식);
        menus.add(MenuName.중식);

        Room room1 = roomService.createRoom("title(owner는 user1)",LocalDateTime.now(),Location.서초,5,user1,"default",menus);
        Room room2 = roomService.createRoom("title(owner는 user2)",LocalDateTime.now(),Location.개포,3,user2,"default",menus);

    }



    static User createTestUser(String userId, String profile, String role) {
        User user = new User();
        user.setId(userId);
        user.setProfile(profile);
        user.setRole(role);
        return user;
    }

    static Room createTestRoom() {
        Room room = new Room();
        room.setTitle("hello");
        room.setLocation(Location.서초);
        room.setStatus(RoomStatus.active);
        room.setAnnouncement("sample");
        room.setMeetTime(LocalDateTime.now());
        return room;
    }
}
