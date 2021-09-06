package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomMenuService;
import com.example.projectprototype.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RoomMapper {
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final RoomMenuService rmService;

    // RoomDto 를 Room 엔티티로 변환
    public Room convertToRoom(RoomDto roomDTO, String userId) {

        Room room = new Room();
        if (roomDTO.getId() != 0)
            room.setId(roomDTO.getId());
        room.setTitle(roomDTO.getTitle());
        room.setMeetTime(LocalDateTime.parse(roomDTO.getMeetTime(), formatter));
        room.setLocation(Location.valueOf(roomDTO.getLocation()));
        room.setOwner(userRepository.findById(userId).get());
        room.setCapacity(roomDTO.getCapacity());
        room.setStatus(RoomStatus.valueOf(roomDTO.getStatus()));
        room.setAnnouncement(roomDTO.getAnnouncement());
        roomRepository.save(room);
        // Room 과 Menu 를 매핑하는 RoomMenu 객체를 만들어서 roomMenuList 에 저장
        for (String menuName : roomDTO.getMenus()) {
            rmService.mappingRoomAndMenu(room, menuName);
        }
        return room;
    }

    // Room 객체를 RoomDTO 로 변환
    public RoomDto convertToRoomDTO(Room room) {
        RoomDto roomDTO = new RoomDto();
        roomDTO.setId(room.getId());
        roomDTO.setTitle(room.getTitle());
        roomDTO.setMeetTime(room.getMeetTime().format(formatter));
        roomDTO.setLocation(room.getLocation().name());
        roomDTO.setOwner(userMapper.convertToUserDto(room.getOwner()));
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setStatus(room.getStatus().name());
        roomDTO.setAnnouncement(room.getAnnouncement());
        // menu 이름 string 으로 변환
        for(RoomMenu roomMenu : room.getRoomMenuList()) {
            roomDTO.getMenus().add(roomMenu.getMenu().getName().toString());
        }
        // 방 참여자 List<User> 로 변환
        for (Participant part : room.getParticipantList()) {
            roomDTO.getParticipants().add(userMapper.convertToUserDto(part.getUser()));
        }
        return roomDTO;
    }
}
