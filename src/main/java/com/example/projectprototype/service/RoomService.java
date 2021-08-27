package com.example.projectprototype.service;

import com.example.projectprototype.dto.ListDTO;
import com.example.projectprototype.dto.RoomDTO;
import com.example.projectprototype.entity.*;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMenuRepository roomMenuRepository;
    private final MenuRepository menuRepository;
    private final ParticipantRepository participantRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 주어진 userId 가 참여중인 active 상태의 방을 DTO로 변환하여 반환
     * -null 참여중인 방이 없으면
     */
    public ListDTO<RoomDTO> getMyRoomList(String userId) {
       // 참여한 방 정보 확인
        List<Participant> participants =
               participantRepository.findByUser(userRepository.findById(userId).get());
       if (participants.size() ==0)
           return null;

       // 참여한 방 정보 기반으로 Room 엔티티 추출
        // Room 엔티티를 RoomDTO 로 변환
        List<RoomDTO> roomDTOList = new ArrayList<>();
       for (Participant part : participants) {
           Room room = roomRepository.findByIdAndStatus(part.getRoom().getId(), RoomStatus.active);
           roomDTOList.add(convertToRoomDTO(room));
       }

       // 변형된 RoomDTO List 를 ListDTO 형태로 변환.
        ListDTO<RoomDTO> DTO = new ListDTO<>();
        DTO.setCode(200);
        DTO.setInterCode(1);
        DTO.setComponent(roomDTOList);

       return DTO;
    }

    /** 전달된 정보를 바탕으로
     * -양수 생성된 방의 id
     * -1 입력된 시간의 형태가 형식에 맞지 않음.
     * -2 입력된 시간 + 1시간 이내에 유저가 방에 참여하고 있음.
     * -3 userId 가 DB에 등록되지 않은 id 임.
     * -4 enum 형 string 들 (ex : menus, location) 이 형식에 맞지 않음.
     */
    public long createRoom(RoomDTO roomDTO, String userId) {
        LocalDateTime inputTime = LocalDateTime.parse(roomDTO.getMeetTime(), formatter);

        // 입력된 시간이 parsing 할 수 있는 형태인지
        try {
            LocalDateTime.parse(roomDTO.getMeetTime(), formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return -1L;
        }

        // user 정보가 DB에 없으면 -3
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return -3L;
        }

        // 기존에 만들어진 방이 없으면 방 생성해서 return
        List<Participant> participants =
                participantRepository.findByUser(userRepository.findById(userId).get());
        if (participants.size() == 0) {
            long result = convertToRoom(roomDTO, userId);
            if (result > 0L)
                participateRoom(roomRepository.findById(result).get(), userId);
            return result;
        }

        // DB에서 주어진 시간과 1시간 이내로 예약된 방이 있는지 확인
        for (Participant part : participants) {
            Room room = roomRepository.findByIdAndStatus(part.getRoom().getId(), RoomStatus.active);
            // input time 이 meetTime 보다 1시간 이후가 맞는지 않으면 -1
            LocalDateTime priorTime = room.getMeetTime();
            if (!isValidTime(priorTime, inputTime)) {
                return -2L;
            }
        }
        long result = convertToRoom(roomDTO, userId);
        if (result > 0L)
            participateRoom(roomRepository.findById(result).get(), userId);
        return result;
    }

    /** room 의 participate 하도록 함
     *
     */
    public long participateRoom(Room room, String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return -3L;
        }
        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(user.get());
        participantRepository.save(participant);
        return room.getId();
    }


    /** RoomDTO 객체를 Room 으로 변환.
     * -양수 생성된 방의 id
     * -3 userId 가 DB에 등록되지 않은 id
     * -4 RoomDTO 에서 넘어온 enum 형 string 들 (ex : menus, location) 이 형식에 맞지 않을 경우 오류
     * how to check if an enum contains String (https://www.javacodestuffs.com/2020/07/how-to-check-if-enum-contains-string.html)
     */
    public long convertToRoom(RoomDTO roomDTO, String userId) {
        Room room = new Room();

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            return -3;

        room.setTitle(roomDTO.getTitle());
        room.setOwner(user.get());
        room.setCapacity(roomDTO.getCapacity());
        room.setMeetTime(LocalDateTime.parse(roomDTO.getMeetTime(), formatter));
        room.setStatus(RoomStatus.active);
        // 임시
        room.setAnnouncement("hello");

        try {
            EnumUtils.findEnumInsensitiveCase(Location.class, roomDTO.getLocation());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return -4L;
        }
        room.setLocation(Location.valueOf(roomDTO.getLocation()));
        roomRepository.save(room);

        // Room 과 Menu를 매핑하는 RoomMenu 객체를 만들어서 roomMenuList 에 저장
        for (String menuName : roomDTO.getMenus()) {
            try {
                EnumUtils.findEnumInsensitiveCase(MenuName.class, menuName);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return -4L;
            }
        }

        for (String menuName : roomDTO.getMenus()) {
            RoomMenu roomMenu = new RoomMenu();
            roomMenu.setRoom(room);
            roomMenu.setMenu(menuRepository.findByName(MenuName.valueOf(menuName)));
            roomMenuRepository.save(roomMenu);
        }
        return room.getId();
    }

    // Room 객체를 RoomDTO 로 변환.
    public RoomDTO convertToRoomDTO(Room room) {

        // menu 이름 string 으로 변환
        List<String> menus = new ArrayList<>();
        for(RoomMenu roomMenu : room.getRoomMenuList()) {
            menus.add(roomMenu.getMenu().getName().toString());
        }

        // 방 참여자 List<User> 로 변환
        List<User> participants = new ArrayList<>();
        for (Participant part : room.getParticipantList()) {
            participants.add(part.getUser());
        }

        RoomDTO roomDTO = RoomDTO.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .menus(menus)
                .meetTime(room.getMeetTime().format(formatter))
                .location(room.getLocation().toString())
                .capacity(room.getCapacity())
                .owner(room.getOwner())
                .participants(participants)
                .status(room.getStatus().toString())
                .build();
        return roomDTO;
    }

    public boolean isValidTime(LocalDateTime priorTime, LocalDateTime inputTime) {
        LocalDateTime criteriaTime;
        LocalDateTime compareTime;
        if (priorTime.isAfter(inputTime)) {
            criteriaTime = inputTime;
            compareTime = priorTime;
        } else {
            criteriaTime = priorTime;
            compareTime = inputTime;
        }
        return criteriaTime.plusHours(1).isBefore(compareTime);
    }
}
