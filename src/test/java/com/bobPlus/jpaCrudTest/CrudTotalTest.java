package com.bobPlus.jpaCrudTest;

import com.bobPlus.entity.*;
import com.bobPlus.entity.enums.Location;
import com.bobPlus.entity.enums.MenuName;
import com.bobPlus.entity.enums.MessageType;
import com.bobPlus.entity.enums.RoomStatus;
import com.bobPlus.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CrudTotalTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BanRepository banRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ParticipantRepository participantRepository;
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
        Assertions.assertThat(user2.get().getId()).isEqualTo("tjeong");

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
        Menu menu = menuRepository.findByName(MenuName.중식);
        Assertions.assertThat(menu.getName()).isEqualTo(MenuName.중식);
    }


    @Test
    void BanCrud() {
        User user = createTestUser("tjeong", "/img/tjeong", "1");
        User destUser = createTestUser("fake1", "/img/fake1", "1");
        userRepository.save(user);
        userRepository.save(destUser);

        Ban ban = new Ban();

        ban.setSrc(user);
        ban.setDest(destUser);
        banRepository.save(ban);
        userRepository.save(user);
        userRepository.save(destUser);

        Optional<User> user2 = userRepository.findById("tjeong");
        Optional<User> user3 = userRepository.findById("fake1");
        String src = user2.get().getBanSrcList().get(0).getSrc().getId();
        String dest = user3.get().getBanDestList().get(0).getDest().getId();
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
        roomMenu.setMenu(menuRepository.findByName(MenuName.중식));
        roomMenuRepository.save(roomMenu);

        roomMenu = new RoomMenu();
        roomMenu.setRoom(room);
        roomMenu.setMenu(menuRepository.findByName(MenuName.커피));
        roomMenuRepository.save(roomMenu);

        List<RoomMenu> roomMenu2 = roomMenuRepository.findByRoom(room).get();
        Assertions.assertThat(roomMenu2.get(0).getMenu().getName()).isEqualTo(MenuName.중식);

        Optional<Room> room2 = roomRepository.findById(room.getId());

        Assertions.assertThat(room2.get().getRoomMenuList().get(0).getMenu().getName())
                        .isEqualTo(MenuName.중식);

        Assertions.assertThat(room2.get().getRoomMenuList().get(1).getMenu().getName())
                .isEqualTo(MenuName.커피);
    }

    @Test
    void participantMappingTest() {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(createTestUser("tjeong", "/img/tjeong", "1"));
        userList.add(createTestUser("tjeong2", "/img/tjeong2", "1"));
        userList.add(createTestUser("tjeong3", "/img/tjeong3", "1"));

        userRepository.saveAll(userList);

        Room room = createTestRoom();

        room.setOwner(userList.get(0));
        roomRepository.save(room);

        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(userList.get(0));

        participantRepository.save(participant);

        participantRepository.findByRoomId(room.getId());
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
        room.setAnnouncement("hellohellohello~~");
        return room;
    }
}

