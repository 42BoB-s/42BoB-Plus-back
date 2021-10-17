package com.bobPlus.service;

import com.bobPlus.entity.Room;
import com.bobPlus.mapper.RoomMapper;
import com.bobPlus.repository.RoomRepository;
import com.bobPlus.dto.RoomDto;
import com.bobPlus.dto.SearchRoomsRequestDto;
import com.bobPlus.entity.enums.RoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
public class RoomServiceImpl implements RoomService {
    private final RoomMapper roomMapper;
    private final RoomServiceUtils utils;
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final ParticipantService partService;

    public Long createRoom(RoomDto roomDTO, String userId) {
        LocalDateTime inputTime = LocalDateTime.parse(roomDTO.getMeetTime(), utils.formatter);

        if (!utils.isTimeFormat(roomDTO.getMeetTime()))
            return -1L;
        if (!userService.userIdCheck(userId))
            return -3L;
        if (!utils.isInLocation(roomDTO.getLocation()))
            return -4L;
        for (String menuName : roomDTO.getMenus()) {
            if (!utils.isInMenuName(menuName))
                return -4L;
        }
        // 기존에 만들어진 방이 없으면 방 생성해서 return
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        if (rooms.size() == 0) {
            Room room = roomMapper.convertToRoom(roomDTO, userId);
            partService.mappingRoomAndUser(room, userId);
            return room.getId();
        }
        // 기존에 만들어진 방이 있으면 기존 약속 시간과 비교하여 1시간 이내로 겹치면 -2 return;
        for (Room loopRoom : rooms) {
            LocalDateTime priorTime = loopRoom.getMeetTime();
            if (!utils.isValidTime(priorTime, inputTime))
                return -2L;
        }
        // 제약 조건을 모두 통과하면 roomDTO 를 Room 객체로 만들고 DB에 등록.
        Room room = roomMapper.convertToRoom(roomDTO, userId);
        // participate 테이블에도 mapping
        partService.mappingRoomAndUser(room, userId);
        return room.getId();
    }

    public List<RoomDto> searchMyRooms(String userId) {
       // 참여한 방 정보 확인, 참여한 정보가 없으면 null 반환
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        if (rooms.size() == 0)
            return null;

       // 참여한 방 정보 기반으로 Room 엔티티 추출, Room 엔티티를 RoomDTO 로 변환
        List<RoomDto> roomDtoList = new ArrayList<>();
       for (Room loopRoom : rooms) {
           roomDtoList.add(roomMapper.convertToRoomDTO(loopRoom));
       }
       return roomDtoList;
    }

    public List<RoomDto> searchRooms(String userId, SearchRoomsRequestDto reqDto, Pageable pageable) {

        List<RoomDto> roomDtoList = new ArrayList<>();

        // default 값 처리
        utils.defaultValueProcess(reqDto);
        // menu 값 처리
        List<String> menuNameList = new ArrayList<>();
        utils.getMenuName(reqDto.getMenu(), menuNameList);
        // DB 조회
        Page<Room> roomPage = roomRepository.searchRooms(reqDto.getLocation(), reqDto.getStartTime(),
                reqDto.getEndTime(), reqDto.getKeyword(), userId, menuNameList, pageable);
        // List<Dto> 변환
        for (Room room : roomPage.getContent())
            roomDtoList.add(roomMapper.convertToRoomDTO(room));
        return roomDtoList;
    }

    public List<RoomDto> searchRooms(SearchRoomsRequestDto reqDto, Pageable pageable) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        // default 값 처리
        utils.defaultValueProcess(reqDto);
        // menu 값 처리
        List<String> menuNameList = new ArrayList<>();
        utils.getMenuName(reqDto.getMenu(), menuNameList);

        Page<Room> roomPage = roomRepository.searchRooms(reqDto.getLocation(), reqDto.getStartTime(),
                reqDto.getEndTime(), reqDto.getKeyword(), menuNameList, pageable);
        // List<Dto> 변환
        for (Room room : roomPage.getContent())
            roomDtoList.add(roomMapper.convertToRoomDTO(room));
        return roomDtoList;
    }

    public Long enterRoom(String userId, String roomId) {

        // roomId parse 할 수 있는지
        Long id = utils.isRoomIdFormat(roomId);
        if (id < 0L)
            return -1L;
        // roomId 로 DB 에서 room 이 조회되는지
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty())
            return -3L;
        // room.status = 'active' 인지
        if (!room.get().getStatus().equals(RoomStatus.active))
            return -6L;
        // 참여할 자리가 남은 방인지
        if (!partService.capacityCheck(room.get()))
            return -3L;

        // 기존에 참여하던 방과 지금 참여하려는 방이 시간이 겹치지 않는지
        List<Room> rooms = roomRepository.findEnteredRoom(userId);
        for (Room loopRoom : rooms) {
            if (!utils.isValidTime(loopRoom.getMeetTime(), room.get().getMeetTime()))
                return -2L;
        }
        partService.mappingRoomAndUser(room.get(), userId);
        return id;
    }

    public Long exitRoom(String userId, String roomId) {
        // roomId parse 할 수 있는지
        Long id = utils.isRoomIdFormat(roomId);
        if (id < 0L)
            return -1L;
        // roomId 로 DB 에서 room 이 조회되는지
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty())
            return -3L;
        // 해당 room 에 userId 가 속해있는지
        if (!partService.isParticipant(room.get(), userId))
            return -5L;

        // 방장 넘기기, room.participantList 에서 삭제, participant 를 DB 에서 삭제
        // return : 남은 참여자 숫자.
        long result = partService.cancelParticipation(room.get(), userId);
        if (result == 0)
            roomRepository.updateStatus(id,RoomStatus.failed.toString());
        return room.get().getId();
    }

    public Long updateTitle(String title, String roomId, String userId) {

        // title 이 공백이면 안됨
        if (title.equals(""))
            return -1L;
        // roomId parsing 여부 확인
        Long id = utils.isRoomIdFormat(roomId); if (id < 0L)
            return -1L;
        // room 조회 확인
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty())
            return -3L;
        // 권한 조회
        if (!room.get().getOwner().getId().equals(userId))
            return -5L;
        roomRepository.updateTitle(id, title);
        return id;
    }

    public Long paramsCheck(SearchRoomsRequestDto reqDto) {
        if (!utils.isInLocation(reqDto.getLocation()))
            return -4L;
        if (!utils.isInMenuName(reqDto.getMenu()))
            return -4L;
        if (!utils.isTimeFormat(reqDto.getStartTime()))
            return -1L;
        if (!utils.isTimeFormat(reqDto.getEndTime()))
            return -1L;
        return 0L;
    }
}
