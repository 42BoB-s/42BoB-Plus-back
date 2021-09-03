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
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMenuRepository roomMenuRepository;
    private final MenuRepository menuRepository;
    private final ParticipantRepository partRepository;

    private final UserService userService;
    private final ParticipantService partService;
    private final RoomMapper roomMapper;

    // 방생성 : 채팅방 생성은 설정 안되어 있음.
    // 입력된 시간이 지금보다 전이면 fail 줘야함.
    public long createRoom(RoomDto roomDTO, String userId) {
        LocalDateTime inputTime = LocalDateTime.parse(roomDTO.getMeetTime(), formatter);

        if (!isParsableTime(roomDTO.getMeetTime())) return -1L;
        if (!userService.userIdCheck(userId)) return -3L;
        if (!isInLocation(roomDTO.getLocation())) return -4L;
        for (String menuName : roomDTO.getMenus()) {
            if (!isInMenuName(menuName)) return -4L;
        }
        // 기존에 만들어진 방이 없으면 방 생성해서 return
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        if (rooms.size() == 0) {
            Room room = convertToRoom(roomDTO, userId);
            partService.setParticipate(room, userId);
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
        partService.setParticipate(room, userId);
        return room.getId();
    }

    /** 주어진 userId 가 참여중인 active 상태의 방을 DTO로 변환하여 반환
     * -null 참여중인 방이 없으면
     */
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
        defaultTreat(reqDto);
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
        Long id = isParsableRoomId(roomId);
        if (id < 0L) return -1L;
        // roomId 로 DB 에서 room 이 조회되는지
        Optional<Room> room = roomRepository.findById(isParsableRoomId(roomId));
        if (room.isEmpty()) return -3L;
        // room.status = 'active' 인지 확인
        if (!room.get().getStatus().equals(RoomStatus.active)) return -6L;
        // 최대 인원 체크
        if (room.get().getCapacity() <= partRepository.countByRoomId(id))
            return -3L;

        // 기존에 참여하던 방과 지금 참여하려는 방이 시간이 겹치지 않는지
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        for (Room loopRoom : rooms) {
            if (!isValidTime(loopRoom.getMeetTime(), room.get().getMeetTime())) {
                return -2L;
            }
        }
        partService.setParticipate(room.get(), userId);
        return id;
    }

    public Long exitRoom(String userId, String roomId) {
        // roomId parse 할 수 있는지
        Long id = isParsableRoomId(roomId);
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

        // title 이 공백이면 안됨.
        if (title.equals("") || title == null) return -1L;
        // roomId parsing 여부 확인
        Long id = isParsableRoomId(roomId); if (id < 0L) return -1L;
        // room 조회 확인
        Optional<Room> room = roomRepository.findById(id); if (room.isEmpty()) return -3L;
        // 권한 조회
        if (!room.get().getOwner().equals(userId)) return -5L;

        roomRepository.updateTitle(id, title);
        return id;
    }

    // RoomDto 를 room 엔티티로 변환
    public Room convertToRoom(RoomDto roomDTO, String userId) {

        Room room = roomMapper.toEntity(roomDTO);

        room.setMeetTime(LocalDateTime.parse(roomDTO.getMeetTime(), formatter));
        room.setStatus(RoomStatus.valueOf(roomDTO.getStatus()));
        room.setOwner(userRepository.findById(userId).get());
        roomRepository.save(room);
        // Room 과 Menu 를 매핑하는 RoomMenu 객체를 만들어서 roomMenuList 에 저장
        for (String menuName : roomDTO.getMenus()) {
            RoomMenu roomMenu = new RoomMenu();
            roomMenu.setRoom(room);
            roomMenu.setMenu(menuRepository.findByName(MenuName.valueOf(menuName)));
            roomMenuRepository.save(roomMenu);
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

    public int paramsCheck(SearchRoomsRequestDto reqDto) {
        if (!isInLocation(reqDto.getLocation())) return -4;
        if (!isInMenuName(reqDto.getMenu())) return -4;
        if (!isParsableTime(reqDto.getStartTime())) return -1;
        if (!isParsableTime(reqDto.getEndTime())) return -1;

        return 0;
    }

    private void defaultTreat(SearchRoomsRequestDto reqDto) {

        if (reqDto.getKeyword().equals("default")) reqDto.setKeyword("%");
        else {
            String tmp = reqDto.getKeyword();
            reqDto.setKeyword("%" + tmp + "%");
        }
        if (reqDto.getLocation().equals("default")) reqDto.setLocation("%");
        if (reqDto.getStartTime().equals("dafault") || reqDto.getEndTime().equals("dafault")) {
            reqDto.setStartTime(LocalDateTime.now().minusMinutes(30).format(formatter));
            reqDto.setEndTime(LocalDateTime.now().plusHours(25).format(formatter));
        }
    }

    private void getMenuName(String menu, List<String> menuNameList) {
        if (menu.equals("default")) {
            for (String menuName : MenuName.getNames(MenuName.class)) {
                menuNameList.add(menuName);
            }
        } else {
            String[] menuArr = menu.split(",");
            for (String menuName : menuArr) {
                menuNameList.add(menuName);
            }
        }
    }

    /**
     * 입력된 시간 값이 기존 약속 시간에서 1시간 내외 안에 있는지 확인
     * 11:00, 12:00 : true
     * 11:00, 11:30 or 10:30 : false
     */
    public boolean isValidTime(LocalDateTime time1, LocalDateTime time2) {
        if (time1.isAfter(time2)) {
            return time2.plusHours(1).isBefore(time1);
        } else {
            return time1.plusHours(1).isBefore(time2);
        }
    }

    public boolean isParsableTime(String time) {
        if (time.equals("default")) return true;
        try {
            LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Long isParsableRoomId(String roomId) {
        try {
            return Long.parseLong(roomId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    // 주어진 String 이 Location enum 에 포함된 값인지 확인
    public boolean isInLocation(String location) {
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
    public boolean isInMenuName(String menuName) {
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
