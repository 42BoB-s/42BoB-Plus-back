package com.example.projectprototype.service;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.dto.SearchRoomsRequestDto;
import com.example.projectprototype.entity.*;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.mapper.RoomMapper;
import com.example.projectprototype.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Long return 값
 * 양수 생성된 방의 id
 * -1 : parse error (ex : 입력된 시간의 형태가 형식에 맞지 않음)
 * -2 : 잘못된 시간 (ex : + 1시간 이내에 유저가 방에 참여하려고함)
 * -3 : 잘못된 DB 조회 (ex : userId 가 DB에 등록되지 않은 id , roomId 가 DB에 등록되지 않은 id)
 * -4 : 잘못된 enum 형 (ex : menus, location 이 형식에 맞지 않음)
 * -5 : 권한 없음 (ex : 요청을 보낸 user 와 대상 room 간에 연관성 없음, owner 가 아닌데 title 을 바꾸려고 함)
 * -6 : status error (ex : active 상태가 아닌 방에 참여하려고함)
 */

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RoomMapper roomMapper;

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final ParticipantService partService;
    private final RoomMenuService rmService;


    public long createRoom(RoomDto roomDTO, String userId) {
        LocalDateTime inputTime = LocalDateTime.parse(roomDTO.getMeetTime(), formatter);

        if (!isTimeFormat(roomDTO.getMeetTime())) return -1L;
        if (!userService.userIdCheck(userId)) return -3L;
        if (!isInLocation(roomDTO.getLocation())) return -4L;
        for (String menuName : roomDTO.getMenus()) {
            if (!isInMenuName(menuName)) return -4L;
        }
        // 기존에 만들어진 방이 없으면 방 생성해서 return
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        if (rooms.size() == 0) {
            Room room = convertToRoom(roomDTO, userId);
            partService.mappingRoomAndUser(room, userId);
            return room.getId();
        }
        // 기존에 만들어진 방이 있으면 기존 약속 시간과 비교하여 1시간 이내로 겹치면 -2 return;
        for (Room loopRoom : rooms) {
            LocalDateTime priorTime = loopRoom.getMeetTime();
            if (!isValidTime(priorTime, inputTime)) {
                return -2L;
            }
        }
        // 제약 조건을 모두 통과하면 roomDTO 를 Room 객체로 만들고 DB에 등록.
        Room room = convertToRoom(roomDTO, userId);
        // participate 테이블에도 mapping
        partService.mappingRoomAndUser(room, userId);
        return room.getId();
    }

    public List<RoomDto> searchMyRooms(String userId) {
       // 참여한 방 정보 확인, 참여한 정보가 없으면 null 반환
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        if (rooms.size() == 0) return null;

       // 참여한 방 정보 기반으로 Room 엔티티 추출, Room 엔티티를 RoomDTO 로 변환
        List<RoomDto> roomDtoList = new ArrayList<>();
       for (Room loopRoom : rooms) {
           roomDtoList.add(convertToRoomDTO(loopRoom));
       }
       return roomDtoList;
    }

    public List<RoomDto> searchRooms(String userId, SearchRoomsRequestDto reqDto, Pageable pageable) {

        List<RoomDto> roomDtoList = new ArrayList<>();

        // default 값 처리
        defaultValueProcess(reqDto);
        // menu 값 처리
        List<String> menuNameList = new ArrayList<>();
        getMenuName(reqDto.getMenu(), menuNameList);
        // DB 조회
        Page<Room> roomPage = roomRepository.searchRooms(reqDto.getLocation(), reqDto.getStartTime(),
                reqDto.getEndTime(), reqDto.getKeyword(), userId, menuNameList, pageable);
        // List<Dto> 변환
        for (Room room : roomPage.getContent())
            roomDtoList.add(convertToRoomDTO(room));
        return roomDtoList;
    }

    public Long enterRoom(String userId, String roomId) {

        // roomId parse 할 수 있는지
        Long id = isRoomIdFormat(roomId);
        if (id < 0L) return -1L;
        // roomId 로 DB 에서 room 이 조회되는지
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty()) return -3L;
        // room.status = 'active' 인지
        if (!room.get().getStatus().equals(RoomStatus.active)) return -6L;
        // 참여할 자리가 남은 방인지
        if (!partService.capacityCheck(room.get())) return -3L;

        // 기존에 참여하던 방과 지금 참여하려는 방이 시간이 겹치지 않는지
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        for (Room loopRoom : rooms) {
            if (!isValidTime(loopRoom.getMeetTime(), room.get().getMeetTime())) {
                return -2L;
            }
        }
        partService.mappingRoomAndUser(room.get(), userId);
        return id;
    }

    public Long exitRoom(String userId, String roomId) {
        // roomId parse 할 수 있는지
        Long id = isRoomIdFormat(roomId);
        if (id < 0L) return -1L;
        // roomId 로 DB 에서 room 이 조회되는지
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty()) return -3L;
        // 해당 room 에 userId 가 속해있는지
        if (!partService.isParticipant(room.get(), userId)) return -5L;

        // 방장 넘기기, room.participantList 에서 삭제, participant 를 DB 에서 삭제
        // return : 남은 참여자 숫자.
        long result = partService.cancelParticipation(room.get(), userId);
        if (result == 0) {
            roomRepository.updateStatus(id,RoomStatus.failed.toString());
        }
        return room.get().getId();
    }

    public Long updateTitle(String title, String roomId, String userId) {

        // title 이 공백이면 안됨
        if (title.equals("")) return -1L;
        // roomId parsing 여부 확인
        Long id = isRoomIdFormat(roomId); if (id < 0L) return -1L;
        // room 조회 확인
        Optional<Room> room = roomRepository.findById(id); if (room.isEmpty()) return -3L;
        // 권한 조회
        if (!room.get().getOwner().getId().equals(userId)) return -5L;

        roomRepository.updateTitle(id, title);
        return id;
    }

    // RoomDto 를 Room 엔티티로 변환
    /* 이 하나의 메서드가 너무 많은 책임을 가지고 있지 않나?
     * 메서드 이름만 보면 RoomDto 를 Room 으로 바꿔주는 정도만 할것 같은데, 아래의 두 가지 초과 기능이 있다.
     * e1. Room 을 DB 에 저장하는 역할
     * e2. Room 과 roomMenu 를 mapping 하고 room_menu 테이블에 저장하는 역할
     *
     * 하지만 Room 엔티티는 적어도 Menu 가 매핑된 상태여야하는데 room 과 menu 를 매핑하려면 room 이 DB 에 저장된 상태여야한다.
     * 그렇기 때문에 RoomDto 를 Room 엔티티로 변환하는것은 암묵적으로 이러한 기능을 갖춰고 있어야 하는게 아닐까?
     */
    public Room convertToRoom(RoomDto roomDTO, String userId) {

        Room room = roomMapper.toEntity(roomDTO);

        room.setMeetTime(LocalDateTime.parse(roomDTO.getMeetTime(), formatter));
        room.setStatus(RoomStatus.valueOf(roomDTO.getStatus()));
        room.setOwner(userRepository.findById(userId).get());
        roomRepository.save(room);
        // Room 과 Menu 를 매핑하는 RoomMenu 객체를 만들어서 roomMenuList 에 저장
        for (String menuName : roomDTO.getMenus()) {
            rmService.mappingRoomAndMenu(room, menuName);
        }
        return room;
    }

    // Room 객체를 RoomDTO 로 변환
    public RoomDto convertToRoomDTO(Room room) {
        RoomDto roomDTO = roomMapper.toDto(room);
        roomDTO.setOwner(userService.convertToUserDto(room.getOwner()));
        // menu 이름 string 으로 변환
        for(RoomMenu roomMenu : room.getRoomMenuList()) {
            roomDTO.getMenus().add(roomMenu.getMenu().getName().toString());
        }
        // 방 참여자 List<User> 로 변환
        for (Participant part : room.getParticipantList()) {
            roomDTO.getParticipants().add(userService.convertToUserDto(part.getUser()));
        }
        return roomDTO;
    }

    public Long paramsCheck(SearchRoomsRequestDto reqDto) {
        if (!isInLocation(reqDto.getLocation())) return -4L;
        if (!isInMenuName(reqDto.getMenu())) return -4L;
        if (!isTimeFormat(reqDto.getStartTime())) return -1L;
        if (!isTimeFormat(reqDto.getEndTime())) return -1L;

        return 0L;
    }

    private void defaultValueProcess(SearchRoomsRequestDto reqDto) {

        if (reqDto.getKeyword().equals("default")) reqDto.setKeyword("%");
        else {
            String tmp = reqDto.getKeyword();
            reqDto.setKeyword("%" + tmp + "%");
        }
        if (reqDto.getLocation().equals("default")) reqDto.setLocation("%");
        if (reqDto.getStartTime().equals("default") || reqDto.getEndTime().equals("default")) {
            reqDto.setStartTime(LocalDateTime.now().minusMinutes(30).format(formatter));
            reqDto.setEndTime(LocalDateTime.now().plusHours(25).format(formatter));
        }
    }

    private void getMenuName(String menu, List<String> menuNameList) {
        if (menu.equals("default")) {
            Collections.addAll(menuNameList, MenuName.getNames(MenuName.class));
        } else {
            String[] menuArr = menu.split(",");
            Collections.addAll(menuNameList, menuArr);
        }
    }


     // 입력된 시간 값이 기존 약속 시간에서 1시간 내외 안에 있는지 확인
    private boolean isValidTime(LocalDateTime time1, LocalDateTime time2) {
        if (time1.isAfter(time2)) {
            return time2.plusHours(1).isBefore(time1);
        } else {
            return time1.plusHours(1).isBefore(time2);
        }
    }

    private boolean isTimeFormat(String time) {
        if (time.equals("default")) return true;
        try {
            LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Long isRoomIdFormat(String roomId) {
        try {
            return Long.parseLong(roomId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    // 주어진 String 이 Location enum 에 포함된 값인지 확인
    private boolean isInLocation(String location) {
        if (location.equals("default")) return true;
        try {
            EnumUtils.findEnumInsensitiveCase(Location.class, location);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 주어진 String 이 MenuName enum 에 포함된 값인지 확인
    private boolean isInMenuName(String menuName) {
        if (menuName.equals("default")) return true;
        try {
            String[] menuNameArr = menuName.split(",");
            for (String menuNameSplit : menuNameArr) {
                EnumUtils.findEnumInsensitiveCase(MenuName.class, menuNameSplit);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
