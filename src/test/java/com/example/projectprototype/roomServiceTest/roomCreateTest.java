package com.example.projectprototype.roomServiceTest;

import com.example.projectprototype.dto.RoomDTO;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * roomConvert 장상 케이스 테스트
 * roomConvert 오류 케이스 테스트
 */
@SpringBootTest
@Transactional
public class roomCreateTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void convertToRoomNormalTest() {
        String meetTime = "2021-08-28 11:30:00";
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        List<String> menuExpected = new ArrayList<>();
        menuExpected.add("중식");
        menuExpected.add("한식");
        menuExpected.add("커피");

        RoomDTO roomDTO = RoomDTO.builder()
                .owner(user)
                .capacity(4)
                .status("active")
                .title("hello_room")
                .location("서초")
                .meetTime(meetTime)
                .menus(menuExpected)
                .build();

        // roomDTO 를 Room 으로 바꿔서 DB 에 저장.
        long result = roomService.convertToRoom(roomDTO, user.getId());
        Assertions.assertThat(result).isGreaterThan(0);

        // DB에 저장된 정보를 Room 객체로 만들어서 정보 확인
        Room room = roomRepository.findById(result).get();
        Assertions.assertThat(room.getMeetTime().format(formatter))
                .isEqualTo(roomDTO.getMeetTime());

        List<String> menuActual = new ArrayList<>();
        for (RoomMenu roomMenu : room.getRoomMenuList()) {
            menuActual.add(roomMenu.getMenu().getName().toString());
        }
        Assertions.assertThat(menuActual).isEqualTo(menuActual);
    }

    /**
     * -1 입력된 시간의 형태가 형식에 맞지 않음.
     * -2 입력된 시간 + 1시간 이내에 유저가 방에 참여하고 있음.
     * -3 userId 가 DB에 등록되지 않은 id 임.
     * -4 enum 형 string 들 (ex : menus, location) 이 형식에 맞지 않음.
     */
    @Test
    void createRoom_NonParsableTimeTest() {
        String meetTime = "11:30:00 28/8/2021";
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        List<String> menuExpected = new ArrayList<>();
        menuExpected.add("중식");

        RoomDTO roomDTO = RoomDTO.builder()
                .owner(user)
                .capacity(4)
                .status("active")
                .title("hello_room")
                .location("서초")
                .meetTime(meetTime)
                .menus(menuExpected)
                .build();

        boolean exceptionCatch = false;
        try {
            long result = roomService.createRoom(roomDTO, user.getId());
        } catch (DateTimeParseException e) {
            exceptionCatch = true;
        }
        Assertions.assertThat(exceptionCatch).isEqualTo(true);
    }

    @Test
    void createRoom_DuplicateTimeTest() {
        String meetTime = "2021-08-28 11:30:00";
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        List<String> menuExpected = new ArrayList<>();
        menuExpected.add("중식");

        RoomDTO roomDTO = RoomDTO.builder()
                .owner(user)
                .capacity(4)
                .status("active")
                .title("hello_room")
                .location("서초")
                .meetTime(meetTime)
                .menus(menuExpected)
                .build();

        long result = roomService.createRoom(roomDTO, user.getId());
        Assertions.assertThat(result).isGreaterThan(0);

        roomDTO.setMeetTime("2021-08-28 12:00:00");
        long result2 = roomService.createRoom(roomDTO, user.getId());
        Assertions.assertThat(result2).isEqualTo(-2L);
        }

    @Test
    void createRoom_NotValidUserIdTest() {
        String meetTime = "2021-08-28 11:30:00";
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        List<String> menuExpected = new ArrayList<>();
        menuExpected.add("중식");

        RoomDTO roomDTO = RoomDTO.builder()
                .owner(user)
                .capacity(4)
                .status("active")
                .title("hello_room")
                .location("서초")
                .meetTime(meetTime)
                .menus(menuExpected)
                .build();
        long result = roomService.createRoom(roomDTO, "hello");
        Assertions.assertThat(result).isEqualTo(-3L);
    }

    @Test
    void createRoom_NotValidEnumFormatTest() {
        String meetTime = "2021-08-28 11:30:00";
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        // 메뉴이름
        List<String> menuExpected = new ArrayList<>();
        menuExpected.add("돈까스");

        RoomDTO roomDTO = RoomDTO.builder()
                .owner(user)
                .capacity(4)
                .status("active")
                .title("hello_room")
                .location("서초")
                .meetTime(meetTime)
                .menus(menuExpected)
                .build();
        long result = roomService.createRoom(roomDTO, "tjeong");
        Assertions.assertThat(result).isEqualTo(-4L);

        // 지역
        menuExpected.remove(0);
        menuExpected.add("중식");
        RoomDTO roomDTO2 = RoomDTO.builder()
                .owner(user)
                .capacity(4)
                .status("active")
                .title("hello_room")
                .location("강남")
                .meetTime(meetTime)
                .menus(menuExpected)
                .build();
        result = roomService.createRoom(roomDTO2, "tjeong");
        Assertions.assertThat(result).isEqualTo(-4L);
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