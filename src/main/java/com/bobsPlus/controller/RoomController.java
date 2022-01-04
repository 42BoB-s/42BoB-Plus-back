package com.bobsPlus.controller;

import com.bobsPlus.dto.*;
import com.bobsPlus.entity.Room;
import com.bobsPlus.service.RoomService;
import com.bobsPlus.service.TokenService;
import com.bobsPlus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bobs")
@RequiredArgsConstructor
public class RoomController {

    @Value("${baseUrl}")
    private String baseUrl;

    private final UserService userService;
    private final RoomService roomService;
    private final TokenService tokenService;

    // 특정 방 조회
    @GetMapping("/room/info")
    public ResponseEntity<HashMap<String, Object>> getRoomInfo(HttpServletRequest req, HttpServletResponse resp, @RequestParam long roomId) {
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        UserDto userDto = tokenService.getToken(req);

        SearchRoomResponseDto roomDto = roomService.getRoomInfo(roomId);
        if (roomDto == null)
        {
            resultMap.put("interCode", -3); // 방이 존재 하지 않음
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        else if (!roomDto.getParticipants().contains(userDto.getId()))
        {
            resultMap.put("interCode", -8); // 방에 입장하지 않은 유저가 해당 방 정보를 조회 시
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        }
        else
        {
            resultMap.put("interCode", roomId);
            resultMap.put("room", roomDto);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        return entity;
    }

    // 방 생성
    @PostMapping("/room")
    public ResponseEntity<HashMap<String, Object>> createRoom(HttpServletRequest req, @RequestBody RoomDto roomDTO) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        UserDto userDto = tokenService.getToken(req);

        long result = roomService.createRoom(roomDTO, userDto.getId());

        if (result < 0L) {
            resultMap.put("interCode", (int) result);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        } else {
            resultMap.put("interCode", 1);
            resultMap.put("roomId", (int) result);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        return entity;
    }

    // 예약한 방 조회
    @GetMapping("/myroom")
    public ResponseEntity<HashMap<String, Object>> searchMyRoom(HttpServletRequest req) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        UserDto userDto = tokenService.getToken(req);
        if (userDto == null) {
            resultMap.put("interCode", 1);
            resultMap.put("roomList", null);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
            return entity;
        }

        List<RoomDto> roomDtoList = roomService.searchMyRooms(userDto.getId());

        resultMap.put("interCode", 1);
        resultMap.put("roomList", roomDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    // 필터링된 방 조회
    @GetMapping("/rooms")
    public ResponseEntity<HashMap<String, Object>> searchRooms(HttpServletRequest req,
                                        Pageable pageable, SearchRoomsRequestDto reqDto) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long check = roomService.paramsCheck(reqDto);
        if (check < 0L) {
            resultMap.put("interCode", (int) check);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
            return entity;
        }

        List<RoomDto> roomDtoList;
        UserDto userDto = tokenService.getToken(req);
        if (userDto == null) {
            roomDtoList = roomService.searchRooms(reqDto, pageable);
        }
        else {
            roomDtoList = roomService.searchRooms(userDto.getId(), reqDto, pageable);
        }

        resultMap.put("interCode", 1);
        resultMap.put("roomList", roomDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @PatchMapping("/room/enter/{roomid}")
    public ResponseEntity<HashMap<String, Object>> enterRoom(HttpServletRequest req,
                                       @PathVariable String roomid){

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        UserDto userDto = tokenService.getToken(req);

        long result = roomService.enterRoom(userDto.getId(), roomid);
        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
        return entity;
    }

    @DeleteMapping("/room/exit/{roomid}")
    public ResponseEntity<HashMap<String, Object>> exitRoom(HttpServletRequest req,
                                @PathVariable String roomid) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        UserDto userDto = tokenService.getToken(req);

        long result = roomService.exitRoom(userDto.getId(), roomid);
        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
        return entity;
    }

    @PutMapping("room/title/{roomid}")
    public ResponseEntity<HashMap<String, Object>> updateTitle(HttpServletRequest req,
                                                                @RequestBody UpdateTitleRequestDto titleDto,
                                                                @PathVariable String roomid) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        UserDto userDto = tokenService.getToken(req);

        long result = roomService.updateTitle(titleDto.getTitle(), roomid, userDto.getId());

        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
        return entity;
    }


}
