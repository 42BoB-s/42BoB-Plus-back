package com.example.projectprototype.dbcrudtest;

import com.example.projectprototype.domain.*;
import com.example.projectprototype.domain.enums.Location;
import com.example.projectprototype.domain.enums.MenuName;
import com.example.projectprototype.domain.enums.MessageType;
import com.example.projectprototype.domain.enums.RoomStatus;
import com.example.projectprototype.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class TotalTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BanRepository banRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomUserRepository roomUserRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoomMenuRepository roomMenuRepository;
    @Autowired
    private ChatMessageRepository messageRepository;


    @Test
    void UserCRUD() {
        User user = createTestUser("tjeong", "/img/tjeong", "1");

        // user 등록
        userRepository.save(user);

        // user 조회
        Optional<User> user2 = userRepository.findById("tjeong");
        Assertions.assertThat(user2.isEmpty()).isEqualTo(false);
        Assertions.assertThat(user2.get().getUserId()).isEqualTo("tjeong");

        // user 삭제
        userRepository.delete(user2.get());
        user2 = userRepository.findById("tjeong");
        Assertions.assertThat(user2.isEmpty()).isEqualTo(true);
    }

    @Test
    void RoomCrud() {
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        Room room = createTestRoom();
        room.setOwner(user);
        roomRepository.save(room);
        userRepository.save(user);

        Room room2 = roomRepository.findById(room.getId()).get();

        Assertions.assertThat(room2.getTitle()).isEqualTo("hello");
        Assertions.assertThat(room2.getLocation()).isEqualTo(Location.서초);
        Assertions.assertThat(room2.getOwner()).isEqualTo(user);
    }

    @Test
    void RoomStatusChangeTest() {
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        Room room = createTestRoom();
        room.setOwner(user);
        roomRepository.save(room);
        userRepository.save(user);

        Room room2 = roomRepository.findById(room.getId()).get();
        Assertions.assertThat(room2.getStatus()).isEqualTo(RoomStatus.active);

        room2.setStatus(RoomStatus.succeed);
        roomRepository.save(room2);

        Optional<Room> room3 = roomRepository.findById(room2.getId());

        Assertions.assertThat(room3.get().getStatus()).isEqualTo(RoomStatus.succeed);
    }

    @Test
    void  MessageCrudTest() {
        ChatMessage message = new ChatMessage();
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        Room room = createTestRoom();

        userRepository.save(user);
        room.setOwner(user);
        roomRepository.save(room);

        message.setWriter("tjeong");
        message.setMessage("hello");
        message.setMessageType(MessageType.enter);
        message.setRoom(room);
        messageRepository.save(message);

        List<ChatMessage> message2 = messageRepository.findByRoom(room);
        Assertions.assertThat(message2.size()).isGreaterThan(0);
        Assertions.assertThat(message2.get(0).getMessage()).isEqualTo("hello");
    }


    @Test
    void menuCrudTest(){
        Menu menu = menuRepository.findByMenuName(MenuName.중식);
        Assertions.assertThat(menu.getMenuName()).isEqualTo(MenuName.중식);
    }


    @Test
    void BanCrud() {
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        Ban ban = new Ban();

        ban.setSrc(user);
        ban.setDest("fake1");
        banRepository.save(ban);
        userRepository.save(user);

        Optional<User> user2 = userRepository.findById("tjeong");
        String src = user2.get().getBanList().get(0).getSrc().getUserId();
        String dest = user2.get().getBanList().get(0).getDest();
        Assertions.assertThat(src).isEqualTo("tjeong");
        Assertions.assertThat(dest).isEqualTo("fake1");
    }


    @Test
    void RoomAndMenuMappingTest() {
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        userRepository.save(user);

        Room room = createTestRoom();
        room.setOwner(user);
        roomRepository.save(room);
        userRepository.save(user);

        RoomMenu roomMenu = new RoomMenu();
        roomMenu.setRoom(room);

        roomMenu.setMenu(menuRepository.findByMenuName(MenuName.중식));
        roomMenuRepository.save(roomMenu);

        RoomMenu roomMenu2 = roomMenuRepository.findByRoom(room).get();
        Assertions.assertThat(roomMenu2.getMenu().getMenuName()).isEqualTo(MenuName.중식);

    }

    @Test
    void roomUserMappingTest() {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(createTestUser("tjeong", "/img/tjeong", "1"));
        userList.add(createTestUser("tjeong2", "/img/tjeong2", "1"));
        userList.add(createTestUser("tjeong3", "/img/tjeong3", "1"));

        userRepository.saveAll(userList);

        Room room = createTestRoom();

        room.setOwner(userList.get(0));
        roomRepository.save(room);

        RoomUser roomUser = new RoomUser();
        roomUser.setRoom(room);
        roomUser.setUser(userList.get(0));

        roomUserRepository.save(roomUser);

        roomUserRepository.findByRoom(room);

    }

    static User createTestUser(String userId, String profile, String role) {
        User user = new User();
        user.setUserId(userId);
        user.setProfile(profile);
        user.setRole(role);
        return user;
    }

    static Room createTestRoom() {
        Room room = new Room();
        room.setTitle("hello");
        room.setLocation(Location.서초);
        room.setStatus(RoomStatus.active);
        room.setMeetTime(new Date());
        return room;
    }
}

