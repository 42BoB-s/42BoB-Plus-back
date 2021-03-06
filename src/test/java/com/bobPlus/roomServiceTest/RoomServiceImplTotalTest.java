package com.bobsPlus.roomServiceTest;

import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.RoomMenu;
import com.bobsPlus.entity.User;
import com.bobsPlus.entity.enums.Location;
import com.bobsPlus.mapper.UserMapper;
import com.bobsPlus.repository.ParticipantRepository;
import com.bobsPlus.repository.RoomMenuRepository;
import com.bobsPlus.repository.RoomRepository;
import com.bobsPlus.repository.UserRepository;
import com.bobsPlus.service.RoomServiceImpl;
import com.bobsPlus.service.UserService;
import com.bobsPlus.dto.RoomDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class RoomServiceImplTotalTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired private RoomServiceImpl roomServiceImpl;
    @Autowired private UserMapper userMapper;
    @Autowired private UserService userService;
    @Autowired private RoomRepository roomRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoomMenuRepository rmRepository;
    @Autowired private ParticipantRepository partRepository;

    @Test
    void createRoomTest() {
        User user1 = createTestUser("tjeong1", "/img/tjeong", "1");
        userRepository.save(user1);

        List<String> menuList = new ArrayList<>();
        menuList.add("중식"); menuList.add("한식");
        setTestRoom(user1,"서초", "title1", "notice1",
               "2021-09-04 12:00:00", menuList);

        Room room = roomRepository.findByOwner(user1).get(0);

        Assertions.assertThat(room.getLocation()).isEqualTo(Location.서초);
        Assertions.assertThat(room.getMeetTime()).isEqualTo(LocalDateTime.parse("2021-09-04 12:00:00", formatter));

        Assertions.assertThat(room.getParticipantList().get(0).getUser().getId()).isEqualTo(user1.getId());

        List<String> extractedList = new ArrayList<>();
        for(RoomMenu roomMenu : room.getRoomMenuList()) {
            extractedList.add(roomMenu.getMenu().getName().toString());
        }
        Assertions.assertThat(extractedList).isEqualTo(menuList);
    }

    @Test
    void searchMyRoomTest() {

    }

    @Test
    void searchRoomTest() {

    }

    @Test
    void enterRoomTest() {

    }

    @Test
    void exitRoomTest() {

    }

    @Test
    void updateTitleTest() {

    }


    public User createTestUser(String userId, String profile, String role) {
        User user = new User();
        user.setId(userId);
        user.setProfile(profile);
        user.setRole(role);
        return user;
    }

    public void setTestRoom(User user, String location, String title, String notice,
                                 String meetTime, List<String> menuList) {
        RoomDto roomDTO = new RoomDto();
        roomDTO.setOwner(userMapper.convertToUserDto(user));
        roomDTO.setCapacity(1);
        roomDTO.setStatus("active");
        roomDTO.setTitle(title);
        roomDTO.setLocation(location);
        roomDTO.setMeetTime(meetTime);
        roomDTO.setAnnouncement(notice);
        roomDTO.setMenus(menuList);
        roomServiceImpl.createRoom(roomDTO, user.getId());
    }
}
