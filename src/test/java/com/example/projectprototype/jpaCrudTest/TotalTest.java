package com.example.projectprototype.jpaCrudTest;

import com.example.projectprototype.entity.*;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.MessageType;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.*;
import com.example.projectprototype.service.ParticipantService;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private ParticipantRepository participantRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoomMenuRepository roomMenuRepository;
    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ParticipantService participantService;

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

        RoomMenu roomMenu2 = roomMenuRepository.findByRoom(room).get();
        Assertions.assertThat(roomMenu2.getMenu().getName()).isEqualTo(MenuName.중식);

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

        participantRepository.findByRoom(room);

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
/*-------------------------------------------------RoomControllerTest-----------------------------------------------------------------------*/
    @Test
    void checkValidTimetest()
    {
        List<LocalDateTime> timeList = new ArrayList<>();
        LocalDateTime newRoomTime = LocalDateTime.of(2020, 12, 20, 9, 30, 30);

        //LocalDateTime time1 = LocalDateTime.of(2020, 12, 21, 9, 30, 30); //true
        LocalDateTime time2 = LocalDateTime.of(2020, 12, 20, 19, 30, 30); //true
        //  LocalDateTime time3 = LocalDateTime.of(2020, 12, 20, 9, 45, 30); //false
        //  LocalDateTime time4 = LocalDateTime.of(2020, 12, 20, 8, 30, 30); //true


        User user1 = userService.createUser("user123","profileEx","normal");
        List<MenuName> menus= new ArrayList<>();
        menus.add(MenuName.분식);
        menus.add(MenuName.중식);
        Room room1 = roomService.createRoom("title(owner는 user1)",time2,Location.서초,5,user1,"default",menus);

        Participant participant = participantService.enterRoom(user1, room1);

        Assertions.assertThat(participantService.checkValidTime(user1.getParticipantList(),newRoomTime)).isEqualTo(true);
    }

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
}

